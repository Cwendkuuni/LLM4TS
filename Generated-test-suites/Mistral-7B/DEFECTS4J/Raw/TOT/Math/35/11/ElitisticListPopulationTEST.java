package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.ElitisticListPopulationBuilder;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {

    /**
     * Test method for {@link ElitisticListPopulation#ElitisticListPopulation(List, int, double)}.
     */
    @org.junit.Test
    public void testElitisticListPopulationConstructorWithList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertNotNull(population);
    }

    /**
     * Test method for {@link ElitisticListPopulation#ElitisticListPopulation(int, double)}.
     */
    @org.junit.Test
    public void testElitisticListPopulationConstructorWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertNotNull(population);
    }

    /**
     * Test method for {@link ElitisticListPopulation#ElitisticListPopulation()}.
     */
    @org.junit.Test
    public void testElitisticListPopulationDefaultConstructor() {
        ElitisticListPopulation population = new ElitisticListPopulation();
        assertNotNull(population);
    }

    /**
     * Test method for {@link ElitisticListPopulation#nextGeneration()}.
     */
    @org.junit.Test
    public void testNextGeneration() {
        ElitisticListPopulationBuilder builder = new ElitisticListPopulationBuilder();
        builder.size(10);
        builder.elitismRate(0.1);
        ElitisticListPopulation population = builder.build();
        population.addChromosome(new FooChromosome(1));
        population.addChromosome(new FooChromosome(2));
        population.addChromosome(new FooChromosome(3));
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
        assertEquals(1, nextGeneration.getChromosome(0).getFitness(), 0.0);
        assertEquals(2, nextGeneration.getChromosome(1).getFitness(), 0.0);
        assertNull(nextGeneration.getChromosome(2));
        assertNull(nextGeneration.getChromosome(3));
        assertNull(nextGeneration.getChromosome(4));
        assertNull(nextGeneration.getChromosome(5));
        assertNull(nextGeneration.getChromosome(6));
        assertNull(nextGeneration.getChromosome(7));
        assertNull(nextGeneration.getChromosome(8));
        assertNull(nextGeneration.getChromosome(9));
    }

    /**
     * Test method for {@link ElitisticListPopulation#setElitismRate(double)}.
     */
    @org.junit.Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation();
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-0.1));
        population.setElitismRate(0.1);
        assertEquals(0.1, population.getElitismRate(), 0.0);
    }

    /**
     * Test method for {@link ElitisticListPopulation#getElitismRate()}.
     */
    @org.junit.Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation();
        population.setElitismRate(0.1);
        assertEquals(0.1, population.getElitismRate(), 0.0);
    }

    /**
     * Test method for {@link ElitisticListPopulation#getElitismRate()}.
     */
    @org.junit.Test(expected = OutOfRangeException.class)
    public void testGetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation();
        population.setElitismRate(1.1);
    }

}

class FooChromosome implements Chromosome {
    private final double fitness;

    public FooChromosome(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return this.fitness;
    }
}