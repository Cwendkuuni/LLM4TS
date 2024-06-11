package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private List<Chromosome> chromosomes;

    @Before
    public void setup() {
        chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        Chromosome chromosome3 = new MockChromosome(3);
        Chromosome chromosome4 = new MockChromosome(4);
        Chromosome chromosome5 = new MockChromosome(5);
        Chromosome chromosome6 = new MockChromosome(6);
        Chromosome chromosome7 = new MockChromosome(7);
        Chromosome chromosome8 = new MockChromosome(8);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);
        chromosomes.add(chromosome5);
        chromosomes.add(chromosome6);
        chromosomes.add(chromosome7);
        chromosomes.add(chromosome8);

        population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    }

    @Test
    public void testElitisticListPopulationConstructorWithList() {
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.size());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testElitisticListPopulationConstructorWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithNegativeElitismRate() {
        new ElitisticListPopulation(chromosomes, 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithElitismRateGreaterThanOne() {
        new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> newGeneration = population.nextGeneration().getChromosomes();
        Collections.sort(newGeneration);
        int boundIndex = (int) Math.ceil((1 - population.getElitismRate()) * chromosomes.size());
        for (int i = 0; i < boundIndex; i++) {
            assertEquals(chromosomes.get(i), newGeneration.get(i));
        }
        for (int i = boundIndex; i < chromosomes.size(); i++) {
            assertNotEquals(chromosomes.get(i), newGeneration.get(i));
        }
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithNegativeElitismRate() {
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithElitismRateGreaterThanOne() {
        population.setElitismRate(1.1);
    }
}

class MockChromosome implements Chromosome {
    private final int fitness;

    public MockChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public double getFitness() {
        return fitness;
    }
}