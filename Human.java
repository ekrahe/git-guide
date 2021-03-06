import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Human {
    // Human instance Variables
    public String name;
    private int age;
    private final int startingAge;
    public int health;
    public String ethnicity;
    public String gender;

    public Human spouse;
    public LinkedList<Human> children;
    public LinkedList<Human> friends;

    public boolean employed;
    public int salary;

    // Constants and variables used within the class
    private Random rand;
    private static final double AGE_PROBABILTY = 0.5;
    private static final double ETHNICITY_PROBABILITY = 0.2;
    private static final double GAY_MARRAGE_PROBABILITY = 0.03;
    private static final double STRAIGHT_MARRAGE_PROBABILITY = 0.10;
    private static final double BASE_FRIENDSHIP_PROBABILTY = 0.75;

    private static final int AGE_HEALTH_THRESHOLD = 40;

    private static final ArrayList<String> randNames = getRandomPeople();

    /**
     * Initialize the instance variables for a human
     */
    public Human(String name, int age, String ethnicity, String gender, 
            int salary) {
        this.name = name;
        this.startingAge = this.age = age;
        health = 1;
        this.ethnicity = ethnicity;
        this.gender = gender;

        spouse = null;
        children = new LinkedList<Human>();
        friends = new LinkedList<Human>();

        this.salary = salary;
        employed = salary > 0 ? true : false;

        rand = new Random();
        rand.setSeed(System.currentTimeMillis());
    }

    public Human(String name, int age, String ethnicity, String gender, 
                    int salary, long seed) {
        this(name, age, ethnicity, gender, salary);
        rand.setSeed(seed);
    }

    private static ArrayList<String> getRandomPeople() {
        ArrayList<String> lines = null;

        try {
            FileReader fileReader = new FileReader("names.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            lines = new ArrayList<String>();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } 

        return lines;
    }

    public int updateAge() {
        return age = Simulator.currentYear - Simulator.startYear + startingAge;
    }

    public String introduce() {
        // TODO GIT PROJECT
        return "";
    }

    public String marry(Human toWed) {
        // TODO GIT PROJECT
        return "";
    }

    public String divorce() {
        // TODO GIT PROJECT
        return "";
    }

    // assumes giveBirth is called with a married Human
    public String giveBirth(Human baby, ArrayList<Human> kids) {
        // TODO GIT PROJECT
        return "";
    }

    public String getJob(int money) {
        // TODO GIT PROJECT
        return "";
    }

    public String leaveJob() {
        // TODO GIT PROJECT
        return "";
    }

    private int ageDiff(int otherAge) {
        return otherAge >= age ? otherAge - age : age - otherAge;
    }

    private String makeNewFriend(Human newFriend) {
        friends.add(newFriend);
        newFriend.friends.add(this);
        return name + " is now friends with " + newFriend.name;
    }
    
    public String spendTimeWith(Human otherPerson) {
        if (otherPerson.name.equals(this.name)) {
            return name + " spent the year by " + (gender.equals("male") ? "himself" : "herself");
        } else if (friends.contains(otherPerson)) {
            return name + " had a great time hanging out with " + (gender.equals("male") ? "his" : "her") + " friend " + otherPerson.name;
        } else {
            // possibly make a friend!!
            double chanceOfBeingFriends = BASE_FRIENDSHIP_PROBABILTY;
            // for every year you differ in age you decrease your chances of being friends by 10%
            chanceOfBeingFriends -= AGE_PROBABILTY * ageDiff(otherPerson.age);
            // if you are a different ethnicity you decrease your changce of being friends by 30%
            int sameNationality = ethnicity.equals(otherPerson.ethnicity) ? 0 : 1;
            chanceOfBeingFriends -= ETHNICITY_PROBABILITY * sameNationality;
            
            if (rand.nextDouble() <= chanceOfBeingFriends) {
                return makeNewFriend(otherPerson);
            } else {
                return name + " did not become friends with " + otherPerson.name + 
                                " after spending a year together :("; 
            }
        }
    }
    
    public String pursueMarrage() {
        Human potentialSpouse;
        if (friends.size() > 0) {
            potentialSpouse = friends.get(rand.nextInt(friends.size()));
            if (potentialSpouse.gender.equals(gender)) {
                // see if a gay marrage could happen
                if (rand.nextDouble() < GAY_MARRAGE_PROBABILITY)
                    return marry(potentialSpouse);
            } else {
                // see if a straight marrage will happen
                if (rand.nextDouble() < STRAIGHT_MARRAGE_PROBABILITY)
                    return marry(friends.get(rand.nextInt(friends.size())));    
            }
            return name + " proposed to " + potentialSpouse.name + " but was rejected :(";
        } else {
            return name + " needs to make friends first before concidering marrage";
        }
    }
    
    private static double fertilityProbability(int wifes_age) {
        return -0.0016 * wifes_age * wifes_age + 0.0754 * wifes_age + 0.0352;
    }

    public String tryForKids(ArrayList<Human> listOfNewChildren) {
        if (spouse == null) {
            return name + " needs to be married to give birth";
        }

        Human wife = gender.equals("male") ? spouse : this;
        if (rand.nextDouble() < fertilityProbability(wife.age)) {
            String childsGender = rand.nextBoolean() ? "male" : "female";
            Human newChild = new Human(randNames.get(rand.nextInt(randNames.size())), 
                                            0, ethnicity, childsGender, 0);
            return wife.giveBirth(newChild, listOfNewChildren);
        } else {
            return name + " was not able to have kids";
        }
    }
    
    public String checkVitals(ArrayList<Human> deceased) {
        updateAge();
        int ageThresholdReached = (age >= AGE_HEALTH_THRESHOLD ? -1 : 1);     
        health += (rand.nextInt(3) + 1) * ageThresholdReached;

        if (health < 0) {
            deceased.add(this);
            return "Unfortunatly " + name + " has passed away at age " + age;
        } else {
            if (age >= AGE_HEALTH_THRESHOLD) {
                return name + "'s health is declining. Life expectancy: " + health + "-" + health*3 + " years";
            } else {
                return name + "'s health is getting stronger every year!"; 
            }
        }
    }
    
}
