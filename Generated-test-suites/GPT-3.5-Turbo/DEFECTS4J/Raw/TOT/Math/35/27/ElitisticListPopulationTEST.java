import org.junit.Test;
import org.apache.commons.math3.genetics.*;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    private static final double ELITISM_RATE = 0.9;

    /**
     * Test case for ElitisticListPopulation constructor with a list of chromosomes and population limit.
     */
    @Test
    public void testElitisticListPopulationWithChromosomesAndPopulationLimit() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, ELITISM_RATE);

        // Verify that the chromosomes are correctly set
        assertEquals(chromosomes, population.getChromosomes());

        // Verify that the population limit is correctly set
        assertEquals(10, population.getPopulationLimit());

        // Verify that the elitism rate is correctly set
        assertEquals(ELITISM_RATE, population.getElitismRate(), 0.0);
    }

    /**
     * Test case for ElitisticListPopulation constructor with a population limit.
     */
    @Test
    public void testElitisticListPopulationWithPopulationLimit() {
        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(10, ELITISM_RATE);

        // Verify that the population limit is correctly set
        assertEquals(10, population.getPopulationLimit());

        // Verify that the elitism rate is correctly set
        assertEquals(ELITISM_RATE, population.getElitismRate(), 0.0);
    }

    /**
     * Test case for nextGeneration() method.
     */
    @Test
    public void testNextGeneration() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(10));
        chromosomes.add(new Chromosome(20));
        chromosomes.add(new Chromosome(30));
        chromosomes.add(new Chromosome(40));

        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, ELITISM_RATE);

        // Generate the next generation
        Population nextGeneration = population.nextGeneration();

        // Verify that the next generation has the correct size
        assertEquals(3, nextGeneration.getPopulationLimit());

        // Verify that the best chromosomes are correctly copied to the next generation
        assertTrue(nextGeneration.getChromosomes().contains(new Chromosome(30)));
        assertTrue(nextGeneration.getChromosomes().contains(new Chromosome(40)));
    }

    /**
     * Test case for setElitismRate() method with valid elitism rate.
     */
    @Test
    public void testSetElitismRateWithValidRate() {
        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(10, ELITISM_RATE);

        // Set a new elitism rate
        population.setElitismRate(0.8);

        // Verify that the new elitism rate is correctly set
        assertEquals(0.8, population.getElitismRate(), 0.0);
    }

    /**
     * Test case for setElitismRate() method with an invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithInvalidRate() {
        // Create an instance of ElitisticListPopulation
        ElitisticListPopulation population = new ElitisticListPopulation(10, ELITISM_RATE);

        // Set an invalid elitism rate
        population.setElitismRate(1.5);
    }

}