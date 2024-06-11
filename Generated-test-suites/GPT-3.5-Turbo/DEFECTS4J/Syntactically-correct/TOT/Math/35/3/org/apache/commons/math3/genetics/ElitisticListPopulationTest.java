
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.genetics.*;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {
    
    @Test
    public void testElitisticListPopulationConstructor1() {
        // Typical Use Case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
        Assert.assertEquals(3, population.getChromosomes().size());
    }
    
    @Test
    public void testElitisticListPopulationConstructor2() {
        // Typical Use Case
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
        Assert.assertEquals(0, population.getChromosomes().size());
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorInvalidElitismRate() {
        // Error Scenario - Elitism rate outside [0, 1] range
        new ElitisticListPopulation(10, 1.5);
    }
    
    @Test
    public void testNextGeneration() {
        // Typical Use Case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(5));
        chromosomes.add(new Chromosome(10));
        chromosomes.add(new Chromosome(15));
        chromosomes.add(new Chromosome(20));
        chromosomes.add(new Chromosome(25));
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.6);
        
        Population nextGeneration = population.nextGeneration();
        
        Assert.assertEquals(5, nextGeneration.getPopulationLimit());
        Assert.assertEquals(0.6, population.getElitismRate(), 0.001);
        Assert.assertEquals(3, nextGeneration.getChromosomes().size());
        
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(new Chromosome(15));
        expectedChromosomes.add(new Chromosome(20));
        expectedChromosomes.add(new Chromosome(25));
        
        Assert.assertEquals(expectedChromosomes, nextGeneration.getChromosomes());
    }
    
    @Test
    public void testSetElitismRate() {
        // Typical Use Case
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(0.8);
        
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalid() {
        // Error Scenario - Elitism rate outside [0, 1] range
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(1.5);
    }
    
}

