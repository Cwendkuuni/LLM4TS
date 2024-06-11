package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests for the ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomesListAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.size());
    }

    @Test
    public void testConstructorWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(5, population.size());
    }

    @Test
    public void testElitismRateOutOfRange() {
        try {
            new ElitisticListPopulation(new ArrayList<>(), 10, 1.1);
            fail();
        } catch (OutOfRangeException e) {
            assertEquals("Elitism rate must be between 0 and 1, got 1.1", e.getMessage());
        }
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new FitnessEvaluatedChromosome(i));
        }

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Population nextGeneration = population.nextGeneration();

        assertEquals(15, nextGeneration.size());
        List<Chromosome> oldChromosomes = population.getChromosomes();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();

        // the first 5 chromosomes should be the same
        for (int i = 0; i < 5; i++) {
            assertEquals(oldChromosomes.get(i), newChromosomes.get(i));
        }

        // the next 5 chromosomes should be the best ones from the old population (excluding the first 5)
        for (int i = 5; i < 10; i++) {
            assertEquals(oldChromosomes.get(i), newChromosomes.get(i+5));
        }

        // the last 5 chromosomes should be randomly chosen from the old population
        for (int i = 10; i < 15; i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i);
            assertNotSame(oldChromosome, newChromosome);
            assertTrue(oldChromosomes.contains(newChromosome));
        }
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.5);
        population.setElitismRate(1.1);
    }
}