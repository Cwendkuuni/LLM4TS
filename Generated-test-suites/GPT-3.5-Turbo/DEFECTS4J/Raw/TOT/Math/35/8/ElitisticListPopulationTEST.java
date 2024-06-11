import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {
    
    private ElitisticListPopulation population;
    
    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(Arrays.asList(1, 2, 3)));
        chromosomes.add(new Chromosome(Arrays.asList(4, 5, 6)));
        chromosomes.add(new Chromosome(Arrays.asList(7, 8, 9)));
        
        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }
    
    @Test
    public void testConstructorAndGetters() {
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(new Chromosome(Arrays.asList(1, 2, 3)));
        expectedChromosomes.add(new Chromosome(Arrays.asList(4, 5, 6)));
        expectedChromosomes.add(new Chromosome(Arrays.asList(7, 8, 9)));
        
        assertEquals(expectedChromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.0001);
    }
    
    @Test
    public void testConstructorWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.3);
        
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.3, population.getElitismRate(), 0.0001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithInvalidElitismRateLow() {
        new ElitisticListPopulation(10, -0.1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithInvalidElitismRateHigh() {
        new ElitisticListPopulation(10, 1.1);
    }
    
    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        
        assertEquals(2, nextGeneration.getChromosomes().size());
        assertEquals(new Chromosome(Arrays.asList(7, 8, 9)), nextGeneration.getChromosomes().get(0));
    }
    
    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.7);
        
        assertEquals(0.7, population.getElitismRate(), 0.0001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetInvalidElitismRateLow() {
        population.setElitismRate(-0.1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetInvalidElitismRateHigh() {
        population.setElitismRate(1.1);
    }

}