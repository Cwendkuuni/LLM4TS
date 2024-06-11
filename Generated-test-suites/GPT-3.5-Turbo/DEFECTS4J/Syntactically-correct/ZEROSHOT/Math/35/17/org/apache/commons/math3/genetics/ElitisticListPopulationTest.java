package org.apache.commons.math3.genetics;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private List<Chromosome> chromosomes;
    private int populationLimit;
    private double elitismRate;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        populationLimit = 100;
        elitismRate = 0.9;
        population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        assertNotNull(nextGeneration);
        assertTrue(nextGeneration instanceof ElitisticListPopulation);
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        assertEquals(elitismRate, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.001);
    }

    @Test
    public void testSetElitismRate() {
        double newElitismRate = 0.8;
        population.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        double newElitismRate = -0.1;
        population.setElitismRate(newElitismRate);    }

    @Test
    public void testGetElitismRate() {
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }
}