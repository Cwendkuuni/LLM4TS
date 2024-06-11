package org.apache.commons.math3.genetics;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        List<Chromosome> chromosomes = Collections.nEmptyList();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testConstructorInvalidElitismRate() {
        List<Chromosome> chromosomes = Collections.nEmptyList();
        int populationLimit = 10;
        double elitismRate = 1.1;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    /**
     * Test nextGeneration method with valid arguments.
     */
    @Test
    public void testNextGenerationValidArguments() {
        List<Chromosome> chromosomes = Collections.nEmptyList();
        int populationLimit = 10;
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        for (int i = 0; i < populationLimit; i++) {
            population.addChromosome(new DummyChromosome(i));
        }

        ElitisticListPopulation nextGeneration = population.nextGeneration();

        assertEquals(populationLimit, nextGeneration.size());
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        int boundIndex = (int) FastMath.ceil((1.0 - elitismRate) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome chromosome = oldChromosomes.get(i);
            assertTrue(nextGeneration.contains(chromosome));
        }
    }

    /**
     * Test nextGeneration method with empty population.
     */
    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 10, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(0, nextGeneration.size());
    }

    /**
     * Test nextGeneration method with population size < elitism rate * population size.
     */
    @Test
    public void testNextGenerationPopulationSizeLessThanElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 10, 0.6);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.size());
    }

    /**
     * Test setElitismRate method with valid arguments.
     */
    @Test
    public void testSetElitismRateValidArguments() {
        ElitisticListPopulation population = new ElitisticListPopulation(new Random().nextInt(10), 10, 0.5);
        double newElitismRate = 0.8;
        population.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with invalid arguments.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidArguments() {
        ElitisticListPopulation population = new ElitisticListPopulation(new Random().nextInt(10), 10, 0.5);
        double newElitismRate = -0.1;
        population.setElitismRate(newElitismRate);
    }

}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double[] getGenes() {
        return new double[0];
    }

    @Override
    public int compareTo(Chromosome other) {
        return Integer.compare(this.id, ((DummyChromosome) other).id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}