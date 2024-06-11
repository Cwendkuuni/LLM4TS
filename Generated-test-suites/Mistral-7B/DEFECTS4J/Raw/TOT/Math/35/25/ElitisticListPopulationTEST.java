package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link ElitisticListPopulation}.
 */
public class ElitisticListPopulationTest {

    /**
     * Test the ElitisticListPopulation constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        List<Chromosome> chromosomes = Collections.singletonList(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.size());
    }

    /**
     * Test the ElitisticListPopulation constructor with invalid elitism rate.
     */
    @Test(expected = OutOfRangeException.class)
    public void testInvalidElitismRate() {
        List<Chromosome> chromosomes = Collections.singletonList(new Chromosome());
        new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    /**
     * Test the population initialization with valid arguments.
     */
    @Test
    public void testInitializePopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.size());
    }

    /**
     * Test the next generation method with valid arguments.
     */
    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.addChromosome(new Chromosome(1));
        population.addChromosome(new Chromosome(2));
        population.addChromosome(new Chromosome(3));
        population.addChromosome(new Chromosome(4));
        population.addChromosome(new Chromosome(5));
        population.addChromosome(new Chromosome(6));

        Collections.sort(population);
        ElitisticListPopulation nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        for (int i = 0; i < 2; i++) {
            Chromosome chromosome = population.get(population.size() - i - 1);
            Chromosome nextChromosome = nextGeneration.get(i);
            assertEquals(chromosome, nextChromosome);
        }
    }

    /**
     * Test the setElitismRate method with valid arguments.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    /**
     * Test the setElitismRate method with invalid arguments.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetInvalidElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(1.1);
    }
}