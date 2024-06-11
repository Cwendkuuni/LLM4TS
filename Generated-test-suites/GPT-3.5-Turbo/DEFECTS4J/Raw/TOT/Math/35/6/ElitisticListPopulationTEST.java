import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    // Test case for ElitisticListPopulation(List<Chromosome>, int, double)

    // Typical use case
    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        // add chromosomes to the list
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        Assert.assertEquals(chromosomes, population.getChromosomes());
        Assert.assertEquals(100, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Edge case
    @Test
    public void testConstructorWithChromosomesEmptyList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        Assert.assertEquals(chromosomes, population.getChromosomes());
        Assert.assertEquals(100, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Error scenario
    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithChromosomesInvalidElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        // add chromosomes to the list
        new ElitisticListPopulation(chromosomes, 100, 1.5);
    }

    // Test case for ElitisticListPopulation(int, double)

    // Typical use case
    @Test
    public void testConstructorPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        Assert.assertEquals(new ArrayList<Chromosome>(), population.getChromosomes());
        Assert.assertEquals(100, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Edge case
    @Test
    public void testConstructorPopulationLimitZero() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 0.9);
        Assert.assertEquals(new ArrayList<Chromosome>(), population.getChromosomes());
        Assert.assertEquals(0, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    // Error scenario
    @Test(expected = OutOfRangeException.class)
    public void testConstructorInvalidElitismRate() {
        new ElitisticListPopulation(100, -0.5);
    }

    // Test case for nextGeneration()

    // Typical use case
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        // add chromosomes to the list
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(90, nextGeneration.getChromosomes().size());
    }

    // Test case for setElitismRate(double)

    // Typical use case
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    // Error scenario
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        population.setElitismRate(1.5);
    }

    // Test case for getElitismRate()

    // Typical use case
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
    }

}