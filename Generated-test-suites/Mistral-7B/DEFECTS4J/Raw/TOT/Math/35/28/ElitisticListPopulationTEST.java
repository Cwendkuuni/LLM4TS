package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        List<Chromosome> chromosomes = Collections.singletonList(new DummyChromosome(1.0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testConstructorInvalidElitismRate() {
        List<Chromosome> chromosomes = Collections.singletonList(new DummyChromosome(1.0));
        new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    /**
     * Test nextGeneration method with valid arguments.
     */
    @Test
    public void testNextGenerationValidArguments() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1.0), new DummyChromosome(2.0), new DummyChromosome(3.0),
                new DummyChromosome(4.0), new DummyChromosome(5.0));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Population nextGeneration = population.nextGeneration();

        int expectedSize = (int) Math.ceil(0.1 * chromosomes.size());
        Assert.assertEquals(10 - expectedSize, nextGeneration.getSize());
        Assert.assertFalse(nextGeneration.hasSameChromosomesAs(population));
    }

    /**
     * Test setElitismRate method with valid arguments.
     */
    @Test
    public void testSetElitismRateValidArguments() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with invalid arguments.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidArguments() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(-0.1);
    }
}

class DummyChromosome implements Chromosome {
    private double fitness = 0;

    public DummyChromosome(double value) {
        this.fitness = value;
    }

    @Override
    public double getFitness() {
        return fitness;
    }
}