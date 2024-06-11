import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // Create chromosomes for testing
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();

        // Create initial population
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.9);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        // Generate next generation
        Population nextGeneration = population.nextGeneration();

        // Test if the correct number of chromosomes is transferred to the next generation
        assertEquals(2, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);
        population.setElitismRate(0.8);

        // Test if the elitism rate is set correctly
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.5);
        population.setElitismRate(1.5);
    }

}