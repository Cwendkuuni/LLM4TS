package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.math3.util.FastMath.nextUp;
import static org.apache.commons.math3.util.Precision.abs;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test method for {@link ElitisticListPopulation#ElitisticListPopulation(List, int, double)}.
     */
    @Test
    public void testElitisticListPopulationConstructionWithList() {
        List<Chromosome> initialPopulation = new ArrayList<>();
        initialPopulation.add(new Chromosome(new RealVector(new double[]{1.0})));
        initialPopulation.add(new Chromosome(new RealVector(new double[]{2.0})));
        initialPopulation.add(new Chromosome(new RealVector(new double[]{3.0})));
        ElitisticListPopulation population = new ElitisticListPopulation(initialPopulation, 5, 0.5);

        Assert.assertEquals(5, population.size());
        Assert.assertFalse(population.containsAll(initialPopulation));
        Assert.assertTrue(population.getChromosomes().containsAll(initialPopulation.subList(0, 3)));
    }

    /**
     * Test method for {@link ElitisticListPopulation#ElitisticListPopulation(int, double)}.
     */
    @Test
    public void testElitisticListPopulationConstructionWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        Assert.assertEquals(5, population.size());

        for (int i = 0; i < population.size(); i++) {
            Chromosome chromosome = new Chromosome(new RealVector(new double[]{i}));
            population.addChromosome(chromosome);
        }

        Collections.sort(population.getChromosomes());
        Assert.assertEquals(3, population.getSizeOfEliteSubPopulation());
        Assert.assertEquals(2.0, population.getChromosome(0).getFitness(), 0.001);
        Assert.assertEquals(3.0, population.getChromosome(1).getFitness(), 0.001);
        Assert.assertEquals(4.0, population.getChromosome(2).getFitness(), 0.001);
    }

    /**
     * Test method for {@link ElitisticListPopulation#nextGeneration()}.
     */
    @Test
    public void testNextGeneration() throws OutOfRangeException {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.addChromosome(new Chromosome(new RealVector(new double[]{1.0})));
        population.addChromosome(new Chromosome(new RealVector(new double[]{2.0})));
        population.addChromosome(new Chromosome(new RealVector(new double[]{3.0})));
        population.addChromosome(new Chromosome(new RealVector(new double[]{4.0})));
        population.addChromosome(new Chromosome(new RealVector(new double[]{5.0})));

        Collections.sort(population.getChromosomes());

        ElitisticListPopulation nextGeneration = population.nextGeneration();

        Assert.assertEquals(5, nextGeneration.size());

        int eliteSize = (int) nextUp(population.size() * population.getElitismRate());
        for (int i = 0; i < eliteSize; i++) {
            Assert.assertEquals(population.getChromosome(i), nextGeneration.getChromosome(i));
        }

        for (int i = eliteSize; i < nextGeneration.size(); i++) {
            Assert.assertNotSame(population.getChromosome(i), nextGeneration.getChromosome(i));
        }
    }

    /**
     * Test method for {@link ElitisticListPopulation#setElitismRate(double)}.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        Assert.assertEquals(0.5, population.getElitismRate(), 0.0);

        population.setElitismRate(0.75);
        Assert.assertEquals(0.75, population.getElitismRate(), 0.0);

        population.setElitismRate(-0.1);
        Assert.assertEquals(0.75, population.getElitismRate(), 0.0);
    }
}