import org.junit.Test;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // Create a population with 10 chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i=0; i<10; i++) {
            chromosomes.add(new Chromosome());
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        // Generate the next generation
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        // Assert that the size of the next generation is equal to the population limit
        assertEquals(10, nextGeneration.getChromosomes().size());

        // Assert that the best chromosomes are copied to the next generation
        assertTrue(nextGeneration.getChromosomes().containsAll(population.getChromosomes().subList(0, 9)));
    }

    @Test
    public void testSetElitismRate() {
        // Create a population with 10 chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i=0; i<10; i++) {
            chromosomes.add(new Chromosome());
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        // Set the elitism rate to 0.5
        population.setElitismRate(0.5);

        // Assert that the new elitism rate is 0.5
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }

}