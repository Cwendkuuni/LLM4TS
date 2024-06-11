
import org.junit.Test;
import org.apache.commons.math3.genetics.*;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructor1() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(c1, c2);
        int populationLimit = 5;
        double elitismRate = 0.9;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        Assert.assertEquals(chromosomes, population.getChromosomes());
        Assert.assertEquals(populationLimit, population.getPopulationLimit());
        Assert.assertEquals(elitismRate, population.getElitismRate(), 0.0);
    }

    @Test
    public void testConstructor2() {
        int populationLimit = 5;
        double elitismRate = 0.9;
        
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        
        Assert.assertEquals(populationLimit, population.getPopulationLimit());
        Assert.assertEquals(elitismRate, population.getElitismRate(), 0.0);
    }

    @Test
    public void testNextGeneration() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();
        c1.setFitness(10.0);
        c2.setFitness(5.0);
        c3.setFitness(8.0);
        List<Chromosome> chromosomes = Arrays.asList(c1, c2, c3);
        int populationLimit = 5;
        double elitismRate = 0.9;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        Population nextGen = population.nextGeneration();
        List<Chromosome> nextGenChromosomes = nextGen.getChromosomes();
        
        Assert.assertEquals(populationLimit, nextGenChromosomes.size());
        Assert.assertEquals(c2, nextGenChromosomes.get(0));
        Assert.assertEquals(c3, nextGenChromosomes.get(1));
        Assert.assertTrue(nextGenChromosomes.contains(c1));
    }

    @Test
    public void testSetElitismRate() {
        int populationLimit = 5;
        double elitismRate = 0.8;
        
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        
        double newElitismRate = 0.7;
        population.setElitismRate(newElitismRate);
        
        Assert.assertEquals(newElitismRate, population.getElitismRate(), 0.0);
    }
}
