import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {

    // Test cases for constructor 1

    @Test
    public void testConstructor1() {
        // Typical use case: creating a population with some chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        
        assertEquals(chromosomes.size(), population.getChromosomes().size());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructor1EmptyChromosomes() {
        // Error scenario: creating a population with no chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        
        assertEquals(chromosomes.size(), population.getChromosomes().size());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Test cases for constructor 2
    
    @Test
    public void testConstructor2() {
        // Typical use case: creating an empty population with a population limit and elitism rate
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        assertEquals(0, population.getChromosomes().size());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Test cases for nextGeneration()
    
    @Test
    public void testNextGeneration() {
        // Typical use case: creating the next generation with the specified elitism rate
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        
        Population nextGeneration = population.nextGeneration();
        
        assertEquals(InterruptFactory.isPopulation(nextGeneration), true);
        assertEquals(population.getPopulationLimit(), nextGeneration.getPopulationLimit());
        assertEquals(population.getElitismRate(), ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.001);
    }

    // Test cases for setElitismRate()
    
    @Test
    public void testSetElitismRate() {
        // Typical use case: setting a valid elitism rate
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(0.8);
        
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateLowerBound() {
        // Error scenario: setting an elitism rate below the valid range
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateUpperBound() {
        // Error scenario: setting an elitism rate above the valid range
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(1.1);
    }

    // Test cases for getElitismRate()
    
    @Test
    public void testGetElitismRate() {
        // Typical use case: getting the elitism rate
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        double elitismRate = population.getElitismRate();
        
        assertEquals(0.9, elitismRate, 0.001);
    }

}