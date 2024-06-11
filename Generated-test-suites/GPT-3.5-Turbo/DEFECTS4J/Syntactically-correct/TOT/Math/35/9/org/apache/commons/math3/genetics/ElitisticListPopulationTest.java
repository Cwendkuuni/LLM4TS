import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {
    
    @Test
    public void testElitisticListPopulationWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.5, population.getElitismRate(), 0.001);
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test
    public void testElitisticListPopulationWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.5, population.getElitismRate(), 0.001);
        Assert.assertNotNull(population.getChromosomes());
        Assert.assertEquals(0, population.getChromosomes().size());
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithOutOfRangeElitismRate() {
        new ElitisticListPopulation(10, -0.5);
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        
        Assert.assertEquals(10, nextGeneration.getPopulationLimit());
        Assert.assertEquals(0.5, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.001);
        Assert.assertEquals(2, nextGeneration.getChromosomes().size());
    }
    
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(0.7);
        
        Assert.assertEquals(0.7, population.getElitismRate(), 0.001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithOutOfRangeValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(-0.5);
    }
}