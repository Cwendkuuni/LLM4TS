package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setup() {
        elitisticListPopulation = new ElitisticListPopulation(10, 0.5);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.5, elitisticListPopulation.getElitismRate(), 0.0);
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        elitisticListPopulation.addChromosome(chromosome);

        assertTrue(elitisticListPopulation.getChromosomes().contains(chromosome));
    }

    @Test
    public void testNextGeneration() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        Chromosome chromosome3 = new Chromosome(3);
        elitisticListPopulation.addChromosome(chromosome1);
        elitisticListPopulation.addChromosome(chromosome2);
        elitisticListPopulation.addChromosome(chromosome3);

        Population nextGeneration = elitisticListPopulation.nextGeneration();

        assertEquals(2, nextGeneration.getPopulationLimit());
        assertTrue(nextGeneration.getChromosomes().contains(chromosome2));
        assertTrue(nextGeneration.getChromosomes().contains(chromosome3));
    }

    @Test
    public void testSetElitismRate() {
        elitisticListPopulation.setElitismRate(0.8);

        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        elitisticListPopulation.setElitismRate(-0.1);
    }

    @Test
    public void testGetElitismRate() {
        double elitismRate = elitisticListPopulation.getElitismRate();

        assertEquals(0.5, elitismRate, 0.0);
    }
}