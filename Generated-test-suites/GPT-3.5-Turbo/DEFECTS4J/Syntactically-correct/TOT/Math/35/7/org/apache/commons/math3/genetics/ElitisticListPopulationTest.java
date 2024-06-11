import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    // Test case for ElitisticListPopulation(List<Chromosome>, int, double)
    @Test
    public void testElitisticListPopulationWithChromosomes() {
        // Typical use case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome()); // Adding chromosomes for testing
        int populationLimit = 100;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    // Test case for ElitisticListPopulation(int, double)
    @Test
    public void testElitisticListPopulation() {
        // Typical use case
        int populationLimit = 100;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    // Test case for nextGeneration()
    @Test
    public void testNextGeneration() {
        // Typical use case
        int populationLimit = 100;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        Population nextGeneration = population.nextGeneration();

        assertNotNull(nextGeneration);
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0.001);

        // Edge case: When elitism rate is 0, nextGeneration should return a population with no chromosomes.
        population.setElitismRate(0);
        nextGeneration = population.nextGeneration();

        assertNotNull(nextGeneration);
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        assertEquals(0, nextGeneration.getChromosomes().size());
    }

    // Test case for setElitismRate()
    @Test
    public void testSetElitismRate() {
        // Typical use case
        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(100, elitismRate);
        double newElitismRate = 0.8;
        population.setElitismRate(newElitismRate);

        assertEquals(newElitismRate, population.getElitismRate(), 0.001);

        // Error scenario: Setting elitism rate outside the [0, 1] range should throw an exception
        double invalidElitismRate = -0.5;

        try {
            population.setElitismRate(invalidElitismRate);
        } catch (OutOfRangeException e) {
            assertEquals("Elitism rate is outside the [0, 1] range: -0.5", e.getMessage());
        }
    }

    // Test case for getElitismRate()
    @Test
    public void testGetElitismRate() {
        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(100, elitismRate);
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }
}