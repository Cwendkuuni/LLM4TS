import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithChromosomes() {
        // Create a list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("12345"));
        chromosomes.add(new Chromosome("67890"));

        // Create a population with elitism rate of 0.9
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
        Assert.assertEquals(5, population.getPopulationLimit());

        // Test nextGeneration method
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(2, nextGeneration.getChromosomes().size());

        // Test setElitismRate method
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);

        // Test getElitismRate method
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationWithoutChromosomes() {
        // Create a population without chromosomes
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
        Assert.assertEquals(10, population.getPopulationLimit());

        // Test nextGeneration method
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(0, nextGeneration.getChromosomes().size());

        // Test setElitismRate method with out of range value
        try {
            population.setElitismRate(1.5);
            Assert.fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // Exception is expected
        }

        // Test getElitismRate method
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
    }
}