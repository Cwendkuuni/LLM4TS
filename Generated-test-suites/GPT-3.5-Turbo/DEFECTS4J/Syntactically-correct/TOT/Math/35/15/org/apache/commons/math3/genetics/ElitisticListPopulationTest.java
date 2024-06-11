import org.apache.commons.math3.genetics.*;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    // Test case for the constructor with a list of chromosomes
    @Test
    public void testConstructor1() {
        // Prepare input data
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.9;

        // Create the ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Check the populationLimit
        assertEquals(populationLimit, population.getPopulationLimit());

        // Check the elitismRate
        assertEquals(elitismRate, population.getElitismRate(), 0.0);

        // Check the chromosomes
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertEquals(chromosomes.size(), actualChromosomes.size());
        for (int i = 0; i < chromosomes.size(); i++) {
            assertEquals(chromosomes.get(i), actualChromosomes.get(i));
        }
    }

    // Test case for the constructor with population limit only
    @Test
    public void testConstructor2() {
        // Prepare input data
        int populationLimit = 10;
        double elitismRate = 0.9;

        // Create the ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        // Check the populationLimit
        assertEquals(populationLimit, population.getPopulationLimit());

        // Check the elitismRate
        assertEquals(elitismRate, population.getElitismRate(), 0.0);

        // Check the chromosomes (should be empty)
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertEquals(0, actualChromosomes.size());
    }

    // Test case for the nextGeneration() method
    @Test
    public void testNextGeneration() {
        // Prepare input data
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Create the next generation population
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        // Check the populationLimit of the next generation
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());

        // Check the elitismRate of the next generation
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0.0);

        // Check the chromosomes of the next generation (should be the same as the last (1-elitismRate)% of the original population)
        List<Chromosome> actualChromosomes = nextGeneration.getChromosomes();
        assertEquals((int) (chromosomes.size() * (1 - elitismRate)), actualChromosomes.size());
        for (int i = (int) (chromosomes.size() * elitismRate); i < chromosomes.size(); i++) {
            assertEquals(chromosomes.get(i), actualChromosomes.get(i - chromosomes.size() * elitismRate));
        }
    }

    // Test case for setting a valid elitism rate
    @Test
    public void testSetElitismRateValid() {
        // Prepare input data
        double elitismRate = 0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Set the elitismRate
        population.setElitismRate(elitismRate);

        // Check the elitismRate
        assertEquals(elitismRate, population.getElitismRate(), 0.0);
    }

    // Test case for setting an invalid elitism rate
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalid() {
        // Prepare input data
        double elitismRate = -0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        // Set the elitismRate (should throw an exception)
        population.setElitismRate(elitismRate);
    }

}