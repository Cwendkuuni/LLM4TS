package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Tests for the ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor ElitisticListPopulation(List<Chromosome>, int, double).
     */
    @Test
    public void testElitisticListPopulationConstructorWithList() {
        List<Chromosome> chromosomes = createTestChromosomes();
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
    }

    /**
     * Test constructor ElitisticListPopulation(int, double).
     */
    @Test
    public void testElitisticListPopulationConstructorWithLimit() {
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
    }

    /**
     * Test method nextGeneration().
     */
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = createTestChromosomes();
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation parentPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        ElitisticListPopulation nextGeneration = parentPopulation.nextGeneration();

        assertEquals(populationLimit, nextGeneration.size());
    }

    /**
     * Test method setElitismRate().
     */
    @Test
    public void testSetElitismRate() {
        int populationLimit = 10;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, 0.5);

        population.setElitismRate(0.9);

        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    /**
     * Test constructor with OutOfRangeException.
     */
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithOutOfRangeElitismRate() {
        List<Chromosome> chromosomes = createTestChromosomes();
        int populationLimit = 10;
        double elitismRate = 1.1;

        new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    /**
     * Test method getElitismRate().
     */
    @Test
    public void testGetElitismRate() {
        int populationLimit = 10;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, 0.5);

        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    /**
     * Test chromosomes creation.
     * @return List of test chromosomes.
     */
    private List<Chromosome> createTestChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        Chromosome chromosome4 = new DummyChromosome(4);
        Chromosome chromosome5 = new DummyChromosome(5);

        return List.of(chromosome1, chromosome2, chromosome3, chromosome4, chromosome5);
    }

}