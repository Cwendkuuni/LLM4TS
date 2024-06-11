package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for the ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test the initialization of ElitisticListPopulation with a list of chromosomes and a population limit.
     */
    @Test
    public void testElitisticListPopulationInitializationWithList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);

        Assert.assertEquals(5, population.size());
        Assert.assertTrue(population.getChromosomes().containsAll(chromosomes));
    }

    /**
     * Test the initialization of ElitisticListPopulation with a population limit and elitism rate.
     */
    @Test
    public void testElitisticListPopulationInitializationWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);

        Assert.assertEquals(10, population.size());
    }

    /**
     * Test that the elitism rate is properly set and get.
     */
    @Test
    public void testSetAndGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.7);
        Assert.assertEquals(0.7, population.getElitismRate(), 0.001);
        population.setElitismRate(0.6);
        Assert.assertEquals(0.6, population.getElitismRate(), 0.001);
    }

    /**
     * Test that an OutOfRangeException is thrown when the elitism rate is not within [0, 1].
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 1.1);
    }

    /**
     * Test that the nextGeneration method correctly copies the elitism rate percentage of the best chromosomes.
     */
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        chromosomes.add(new DummyChromosome(4));
        chromosomes.add(new DummyChromosome(5));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);
        int boundIndex = (int) FastMath.ceil((1.0 - 0.5) * oldChromosomes.size());

        Population nextGeneration = population.nextGeneration();

        Assert.assertEquals(5, nextGeneration.size());
        Assert.assertTrue(nextGeneration.getChromosomes().containsAll(oldChromosomes.subList(boundIndex, oldChromosomes.size())));
    }
}

/**
 * Dummy chromosome for testing purposes.
 */
class DummyChromosome implements Chromosome {

    private int value;

    public DummyChromosome(int value) {
        this.value = value;
    }

    @Override
    public int getFitness() {
        return value;
    }
}