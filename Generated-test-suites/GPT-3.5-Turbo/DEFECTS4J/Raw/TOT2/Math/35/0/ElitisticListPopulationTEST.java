import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomes() {
        // Test case for constructor with list of chromosomes
        // Initialize the list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome()); // Dummy chromosome for testing
        int populationLimit = 10;
        double elitismRate = 0.9;

        // Create ElitisticListPopulation instance
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Check if chromosomes list is initialized correctly
        assertEquals(chromosomes, population.getChromosomes());
        // Check if population limit is set correctly
        assertEquals(populationLimit, population.getPopulationLimit());
        // Check if elitism rate is set correctly
        assertEquals(elitismRate, population.getElitismRate(), 0.0);
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        // Test case for constructor without list of chromosomes
        int populationLimit = 20;
        double elitismRate = 0.8;

        // Create ElitisticListPopulation instance
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        // Check if chromosomes list is initialized as empty list
        assertTrue(population.getChromosomes().isEmpty());
        // Check if population limit is set correctly
        assertEquals(populationLimit, population.getPopulationLimit());
        // Check if elitism rate is set correctly
        assertEquals(elitismRate, population.getElitismRate(), 0.0);
    }

    @Test
    public void testNextGeneration() {
        // Test case for generating next generation
        // Create a population with some chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome()); // Dummy chromosome for testing
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.7;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        Population nextGeneration = population.nextGeneration();

        // Check if the number of chromosomes in next generation is as expected
        assertEquals(populationLimit, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRateValid() {
        // Test case for setting elitism rate within valid range
        double elitismRate = 0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.7);
        population.setElitismRate(elitismRate);

        // Check if elitism rate is set correctly
        assertEquals(elitismRate, population.getElitismRate(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalid() {
        // Test case for setting elitism rate outside valid range
        double elitismRate = 1.5;
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.7);
        population.setElitismRate(elitismRate);
    }
}