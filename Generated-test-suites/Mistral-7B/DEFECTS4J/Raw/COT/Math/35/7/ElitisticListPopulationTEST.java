package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;

/**
 * Test suite for ElitisticListPopulation class
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and limit
     */
    @org.junit.Test
    public void testElitisticListPopulationWithListAndLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    /**
     * Test constructor with limit and elitism rate
     */
    @org.junit.Test
    public void testElitisticListPopulationWithLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertNotNull(population);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    /**
     * Test nextGeneration method with valid input
     */
    @org.junit.Test
    public void testNextGenerationWithValidInput() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new Chromosome());
        }

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Population nextGeneration = population.nextGeneration();

        assertEquals(7, nextGeneration.getSize());
    }

    /**
     * Test nextGeneration method with empty population
     */
    @org.junit.Test
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        assertThrows(UnsupportedOperationException.class, () -> population.nextGeneration());
    }

    /**
     * Test nextGeneration method with invalid elitism rate
     */
    @org.junit.Test
    public void testNextGenerationWithInvalidElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new Chromosome());
        }

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 2.5);
        assertThrows(OutOfRangeException.class, () -> population.nextGeneration());
    }

    /**
     * Test setElitismRate method with valid input
     */
    @org.junit.Test
    public void testSetElitismRateWithValidInput() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.01);
    }

    /**
     * Test setElitismRate method with invalid input
     */
    @org.junit.Test
    public void testSetElitismRateWithInvalidInput() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-0.1));
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(1.1));
    }
}