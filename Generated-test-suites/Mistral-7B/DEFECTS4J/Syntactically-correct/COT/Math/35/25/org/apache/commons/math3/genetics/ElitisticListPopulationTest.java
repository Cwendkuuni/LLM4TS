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
import org.apache.commons.math3.util.FastMath;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        Chromosome chromosome1 = new FakeChromosome(1);
        Chromosome chromosome2 = new FakeChromosome(2);
        Chromosome chromosome3 = new FakeChromosome(3);
        Chromosome chromosome4 = new FakeChromosome(4);
        Chromosome chromosome5 = new FakeChromosome(5);

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);
        chromosomes.add(chromosome5);

        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testConstructorWithList() {
        assertNotNull(population);
    }

    @Test
    public void testConstructorWithIntAndDouble() {
        assertNotNull(population);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(5, nextGeneration.size());
    }

    @Test
    public void testSetElitismRatePositive() {
        population.setElitismRate(0.9);
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testSetElitismRateZero() {
        assertThrows(OutOfRangeException.class, () -> {
            population.setElitismRate(0.0);
        });
    }

    @Test
    public void testSetElitismRateOnePlus() {
        assertThrows(OutOfRangeException.class, () -> {
            population.setElitismRate(1.1);
        });
    }

}

class FakeChromosome implements Chromosome {
    private final int fitness;

    public FakeChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }
}