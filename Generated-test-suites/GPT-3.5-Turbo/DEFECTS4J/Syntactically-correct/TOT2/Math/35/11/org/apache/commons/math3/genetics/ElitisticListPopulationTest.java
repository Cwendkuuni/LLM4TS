import static org.junit.Assert.*;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomes() {
        // Test constructor with chromosomes
        // Edge case: empty list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        try {
            new ElitisticListPopulation(chromosomes, 10, 0.9);
            fail("Expected OutOfRangeException due to empty list of chromosomes");
        } catch (OutOfRangeException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        // Test constructor without chromosomes
        try {
            ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
            assertNotNull(population);
        } catch (OutOfRangeException e) {
            fail("Unexpected OutOfRangeException");
        }
    }

    @Test
    public void testNextGeneration() {
        // Test next generation method
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Population nextGen = population.nextGeneration();
        assertEquals(1, nextGen.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        // Test set elitism rate method
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);

        // Exception handling
        try {
            population.setElitismRate(-0.1);
            fail("Expected OutOfRangeException for negative elitism rate");
        } catch (OutOfRangeException e) {
            // Exception caught, test passed
        }
    }

}