package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = Arrays.asList(
                new Chromosome(new RealValuedChromosome(new double[]{1.0, 2.0, 3.0})),
                new Chromosome(new RealValuedChromosome(new double[]{2.5, 3.5, 4.5})),
                new Chromosome(new RealValuedChromosome(new double[]{3.1, 4.1, 5.1})),
                new Chromosome(new RealValuedChromosome(new double[]{1.1, 2.1, 3.1})),
                new Chromosome(new RealValuedChromosome(new double[]{0.5, 1.5, 2.5}))
        );
        population = new ElitisticListPopulation(chromosomes, 5, 0.9);
    }

    /**
     * Test constructor with correct parameters.
     */
    @Test
    public void testElitisticListPopulationConstruction() {
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with incorrect elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionInvalidElitismRate() {
        new ElitisticListPopulation(this.population.getChromosomes(), this.population.getPopulationLimit(), 1.2);
    }

    /**
     * Test nextGeneration() method with correct parameters.
     */
    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(5, nextGeneration.size());
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();

        // Elitism rate is 0.9 which means 4 best chromosomes will be copied to the next generation.
        Assert.assertEquals(Collections.singletonList(population.getChromosomes().get(4)), nextGenerationChromosomes.get(0).getGenes());
        Assert.assertEquals(Collections.singletonList(population.getChromosomes().get(3)), nextGenerationChromosomes.get(1).getGenes());
        Assert.assertEquals(Collections.singletonList(population.getChromosomes().get(2)), nextGenerationChromosomes.get(2).getGenes());
        Assert.assertEquals(Collections.singletonList(population.getChromosomes().get(1)), nextGenerationChromosomes.get(3).getGenes());

        // The remaining chromosomes in next generation are created through some other method.
        Assert.assertNotSame(population.getChromosomes().get(0).getGenes(), nextGenerationChromosomes.get(4).getGenes());
        Assert.assertNotSame(population.getChromosomes().get(0).getGenes(), nextGenerationChromosomes.get(4).getGenes());
    }

    /**
     * Test nextGeneration() method with empty population.
     */
    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation emptyPopulation = new ElitisticListPopulation(Collections.emptyList(), 5, 0.9);
        Population nextGeneration = emptyPopulation.nextGeneration();
        Assert.assertEquals(5, nextGeneration.size());
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();

        // The first five chromosomes in next generation are created through some other method since the old population was empty.
        Assert.assertNotSame(Collections.emptyList(), nextGenerationChromosomes.get(0).getGenes());
        Assert.assertNotSame(Collections.emptyList(), nextGenerationChromosomes.get(1).getGenes());
        Assert.assertNotSame(Collections.emptyList(), nextGenerationChromosomes.get(2).getGenes());
        Assert.assertNotSame(Collections.emptyList(), nextGenerationChromosomes.get(3).getGenes());
        Assert.assertNotSame(Collections.emptyList(), nextGenerationChromosomes.get(4).getGenes());
    }

    /**
     * Test setElitismRate() method.
     */
    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.7);
        Assert.assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate() method with incorrect elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidElitismRate() {
        population.setElitismRate(1.2);
    }
}