package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for ElitisticListPopulation
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testElitisticListPopulationValidConstruction() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome());
        initialChromosomes.add(new Chromosome());

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 0.9);
        assertEquals(5, population.size());
    }

    /**
     * Test constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationInvalidConstruction() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 1.1);
    }

    /**
     * Test nextGeneration method with valid population.
     */
    @Test
    public void testNextGenerationValidPopulation() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome(1.0));
        initialChromosomes.add(new Chromosome(2.0));
        initialChromosomes.add(new Chromosome(3.0));
        initialChromosomes.add(new Chromosome(4.0));
        initialChromosomes.add(new Chromosome(5.0));

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 0.9);

        List<Chromosome> sortedChromosomes = new ArrayList<>(population);
        Collections.sort(sortedChromosomes);

        List<Chromosome> newPopulation = population.nextGeneration().getChromosomes();
        for (int i = 0; i < Math.ceil(sortedChromosomes.size() * 0.1); i++) {
            Chromosome chromosome = sortedChromosomes.get(i);
            assertTrue(newPopulation.contains(chromosome));
        }
    }

    /**
     * Test setElitismRate method with valid elitism rate.
     */
    @Test
    public void testSetElitismRateValidRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    /**
     * Test setElitismRate method with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.9);
        population.setElitismRate(1.1);
    }
}