package org.apache.commons.math3.genetics.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

/**
 * Tests for the ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstruction() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Population population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = java.lang.OutOfBoundsException.class)
    public void testInvalidElitismRateConstruction() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, 2.0);
    }

    /**
     * Test next generation with valid population.
     */
    @Test
    public void testNextGenerationValidPopulation() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Chromosome bestChromosome = new DummyChromosome(0);
        Chromosome secondBestChromosome = new DummyChromosome(1);
        Chromosome worstChromosome = new DummyChromosome(2);
        chromosomes.add(bestChromosome);
        chromosomes.add(secondBestChromosome);
        chromosomes.add(worstChromosome);

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Population nextGeneration = population.nextGeneration();

        assertEquals(10, nextGeneration.getPopulationLimit());
        assertEquals(0.9, nextGeneration.getElitismRate(), 0.01);

        assertTrue(nextGeneration.contains(bestChromosome));
        assertTrue(nextGeneration.contains(secondBestChromosome));
        assertFalse(nextGeneration.contains(worstChromosome));
    }

    /**
     * Test next generation with empty population.
     */
    @Test(expected = java.lang.IllegalStateException.class)
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new java.util.ArrayList<>(), 10, 0.9);
        population.nextGeneration();
    }

    /**
     * Test setting elitism rate.
     */
    @Test
    public void testSetElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);
        population.setElitismRate(0.9);
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    /**
     * Test setting elitism rate with invalid value.
     */
    @Test(expected = org.apache.commons.math3.exception.OutOfRangeException.class)
    public void testSetElitismRateInvalidValue() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);
        population.setElitismRate(1.5);
    }

}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double getFitness() {
        return id;
    }

    @Override
    public String toString() {
        return "DummyChromosome{" + id + '}';
    }
}