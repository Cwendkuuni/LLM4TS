import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // create initial population
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        
        // create chromosomes for initial population
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        
        // create expected next generation population
        ElitisticListPopulation expectedNextGeneration = new ElitisticListPopulation(10, 0.9);
        expectedNextGeneration.addChromosome(chromosome1);
        expectedNextGeneration.addChromosome(chromosome2);
        expectedNextGeneration.addChromosome(chromosome3);
        
        // get next generation
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        
        // assert equal
        assertEquals(expectedNextGeneration, nextGeneration);
    }

    @Test
    public void testSetElitismRate() {
        // create population
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        
        // set elitism rate to a different value
        population.setElitismRate(0.8);
        
        // assert equal
        assertEquals(0.8, population.getElitismRate(), 0.00001);
    }

}