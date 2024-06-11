import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // Create a population with 4 chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        // Create an ElitisticListPopulation instance
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 4, 0.9);
        
        // Get the next generation
        Population nextGeneration = population.nextGeneration();
        
        // Check the size of the next generation
        assertEquals(4, nextGeneration.getPopulationLimit());
        
        // Check that the correct number of chromosomes were copied to the next generation
        assertEquals(3, nextGeneration.getChromosomes().size());
    }
    
    @Test
    public void testSetElitismRate() {
        // Create an ElitisticListPopulation instance
        ElitisticListPopulation population = new ElitisticListPopulation(4, 0.9);
        
        // Set the elitism rate
        population.setElitismRate(0.8);
        
        // Check that the elitism rate was set correctly
        assertEquals(0.8, population.getElitismRate(), 0.0001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        // Create an ElitisticListPopulation instance
        ElitisticListPopulation population = new ElitisticListPopulation(4, 0.9);
        
        // Set an elitism rate outside the valid range (0 to 1)
        population.setElitismRate(1.5);
    }

}