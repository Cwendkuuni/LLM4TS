import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    /*
     * Test for nextGeneration() method.
     */
    @Test
    public void testNextGeneration() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("A", 10));
        chromosomes.add(new Chromosome("B", 20));
        chromosomes.add(new Chromosome("C", 30));
        chromosomes.add(new Chromosome("D", 40));

        // Create a ElitisticListPopulation object
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);

        // Call the nextGeneration() method
        Population nextGeneration = population.nextGeneration();

        // Check if the size of the next generation is correct
        assertEquals(3, nextGeneration.getChromosomes().size());

        // Check if the chromosomes in the next generation are correct
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(new Chromosome("C", 30));
        expectedChromosomes.add(new Chromosome("D", 40));

        List<Chromosome> actualChromosomes = nextGeneration.getChromosomes();

        assertEquals(expectedChromosomes, actualChromosomes);
    }

    /*
     * Test for setElitismRate() method.
     */
    @Test
    public void testSetElitismRate() {
        // Create a ElitisticListPopulation object
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);

        // Set the elitism rate
        population.setElitismRate(0.5);

        // Check if the elitism rate is set correctly
        assertEquals(0.5, population.getElitismRate(), 0);
    }
}