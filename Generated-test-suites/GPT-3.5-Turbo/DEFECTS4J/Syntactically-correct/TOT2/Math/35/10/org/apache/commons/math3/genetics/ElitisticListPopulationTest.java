import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        // Initialize chromosomes list and population limit
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;

        // Test ElitisticListPopulation constructor with chromosomes
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, populationLimit, 0.8);

        assertEquals(chromosomes, elitisticListPopulation.getChromosomes());
        assertEquals(populationLimit, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.0);
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        // Initialize population limit
        int populationLimit = 20;

        // Test ElitisticListPopulation constructor without chromosomes
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(populationLimit, 0.7);

        assertEquals(populationLimit, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.7, elitisticListPopulation.getElitismRate(), 0.0);
    }

    @Test
    public void testNextGeneration() {
        // Initialize chromosomes list and population limit
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;

        // Initialize ElitisticListPopulation
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, populationLimit, 0.8);

        // Test nextGeneration method
        Population nextGeneration = elitisticListPopulation.nextGeneration();

        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        // Make assertions for the next generation population
    }

    @Test
    public void testSetElitismRateWithinRange() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);

        elitisticListPopulation.setElitismRate(0.7);

        assertEquals(0.7, elitisticListPopulation.getElitismRate(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);

        elitisticListPopulation.setElitismRate(1.5);
    }

    // Add more test cases for edge cases and error scenarios

}