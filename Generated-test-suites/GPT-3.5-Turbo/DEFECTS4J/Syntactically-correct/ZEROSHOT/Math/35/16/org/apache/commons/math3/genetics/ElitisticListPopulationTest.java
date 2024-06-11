import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.5;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        Assert.assertEquals(population.getPopulationLimit(), populationLimit);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithOutOfRangeElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 1.5;
        
        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }
    
    @Test
    public void testElitisticListPopulationWithDefaultConstructor() {
        int populationLimit = 10;
        double elitismRate = 0.5;
        
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        
        Assert.assertEquals(population.getPopulationLimit(), populationLimit);
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        int populationLimit = 5;
        double elitismRate = 0.4;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        Population nextGeneration = population.nextGeneration();
        
        Assert.assertEquals(nextGeneration.getPopulationLimit(), populationLimit);
        Assert.assertEquals(nextGeneration.getChromosomes().size(), population.getChromosomes().size() - (int)Math.ceil((1.0 - elitismRate) * population.getChromosomes().size()));
    }
    
    @Test
    public void testSetElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.5;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        double newElitismRate = 0.3;
        population.setElitismRate(newElitismRate);
        
        Assert.assertEquals(population.getElitismRate(), newElitismRate, 0.0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithOutOfRangeRate() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.5;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        double newElitismRate = 1.5;
        population.setElitismRate(newElitismRate);
    }
    
    @Test
    public void testGetElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.5;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        Assert.assertEquals(population.getElitismRate(), elitismRate, 0.0);
    }

}