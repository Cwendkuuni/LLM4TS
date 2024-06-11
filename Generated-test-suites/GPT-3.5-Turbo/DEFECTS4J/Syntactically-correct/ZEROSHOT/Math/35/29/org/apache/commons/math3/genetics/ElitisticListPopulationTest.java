import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // Test nextGeneration() method

        // Create a population with 10 chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, elitismRate);

        // Call nextGeneration() method
        Population nextGeneration = population.nextGeneration();

        // Check if the size of the next generation is correct
        assertEquals(90, nextGeneration.getPopulationLimit());

        // Check if the elite chromosomes are properly transferred to the next generation
        List<Chromosome> eliteChromosomes = population.getChromosomes().subList(0, (int) (eliteRate * population.getChromosomes().size()));
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        for (Chromosome eliteChromosome : eliteChromosomes) {
            assertTrue(nextGenerationChromosomes.contains(eliteChromosome));
        }
    }

    @Test
    public void testSetAndGetElitismRate() {
        // Test setElitismRate() and getElitismRate() methods

        double elitismRate = 0.9;
        ElitisticListPopulation population = new ElitisticListPopulation(100, elitismRate);

        // Check if the initial elitism rate is correct
        assertEquals(elitismRate, population.getElitismRate(), 0.001);

        // Set a new elitism rate
        double newElitismRate = 0.8;
        population.setElitismRate(newElitismRate);

        // Check if the new elitism rate is correctly set
        assertEquals(newElitismRate, population.getElitismRate(), 0.001);
    }

}