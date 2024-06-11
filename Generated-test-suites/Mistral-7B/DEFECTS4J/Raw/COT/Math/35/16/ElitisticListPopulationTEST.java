package org.apache.commons.math3.genetics.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.OutOfRangeException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and population limit.
     */
    @org.junit.Test
    public void testConstructorWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    /**
     * Test constructor with population limit.
     */
    @org.junit.Test
    public void testConstructorWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    /**
     * Test nextGeneration method with sorting.
     */
    @org.junit.Test
    public void testNextGenerationWithSorting() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome bestChromosome = new DummyChromosome(1.0);
        chromosomes.add(new DummyChromosome(0.5));
        chromosomes.add(bestChromosome);
        chromosomes.add(new DummyChromosome(0.3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        assertEquals(bestChromosome, nextGeneration.getChromosome(0));
    }

    /**
     * Test nextGeneration method without sorting.
     */
    @org.junit.Test
    public void testNextGenerationWithoutSorting() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome bestChromosome = new DummyChromosome(1.0);
        chromosomes.add(new DummyChromosome(0.5));
        chromosomes.add(bestChromosome);
        chromosomes.add(new DummyChromosome(0.3));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        assertThrows(IllegalStateException.class, () -> nextGeneration.getChromosome(0));
    }

    /**
     * Test setElitismRate method.
     */
    @org.junit.Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with out of range exception.
     */
    @org.junit.Test
    public void testSetElitismRateWithOutOfRangeException() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-0.1));
    }

    /**
     * Test getElitismRate method.
     */
    @org.junit.Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

}

class DummyChromosome implements Chromosome {
    private double fitness;

    public DummyChromosome(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }
}