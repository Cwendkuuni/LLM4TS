import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomes() {
        // Prepare test data
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.5;

        // Create new population
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Verify population properties
        Assert.assertEquals(chromosomes, population.getChromosomes());
        Assert.assertEquals(populationLimit, population.getPopulationLimit());
        Assert.assertEquals(elitismRate, population.getElitismRate(), 0);
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        // Prepare test data
        int populationLimit = 10;
        double elitismRate = 0.5;

        // Create new population
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        // Verify population properties
        Assert.assertEquals(0, population.getChromosomes().size());
        Assert.assertEquals(populationLimit, population.getPopulationLimit());
        Assert.assertEquals(elitismRate, population.getElitismRate(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidElitismRate() {
        // Prepare test data
        int populationLimit = 10;
        double elitismRate = -0.5;

        // Create new population (this should throw an exception)
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
    }

    @Test
    public void testNextGeneration() {
        // Prepare test data
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(10));
        chromosomes.add(new Chromosome(20));
        chromosomes.add(new Chromosome(30));
        chromosomes.add(new Chromosome(40));
        chromosomes.add(new Chromosome(50));
        int populationLimit = 3;
        double elitismRate = 0.4;

        // Create new population
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        // Generate next generation
        Population nextGeneration = population.nextGeneration();

        // Verify next generation properties
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        Assert.assertEquals(populationLimit, nextGenerationChromosomes.size());
        Assert.assertTrue(nextGenerationChromosomes.contains(new Chromosome(10)));
        Assert.assertTrue(nextGenerationChromosomes.contains(new Chromosome(20)));
        Assert.assertTrue(nextGenerationChromosomes.contains(new Chromosome(30)));
    }

    @Test
    public void testSetElitismRate() {
        // Prepare test data
        int populationLimit = 10;
        double elitismRate = 0.5;

        // Create new population
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        // Set new elitism rate
        double newElitismRate = 0.8;
        population.setElitismRate(newElitismRate);

        // Verify updated elitism rate
        Assert.assertEquals(newElitismRate, population.getElitismRate(), 0);
    }

}