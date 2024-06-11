package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {

    private List<Chromosome> chromosomes;
    private int populationLimit;
    private double elitismRate;
    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<Chromosome>(Arrays.asList(new Chromosome(), new Chromosome()));
        populationLimit = 100;
        elitismRate = 0.5;
        population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testConstructor() {
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0);
    }

    @Test
    public void testSetElitismRate() {
        double newElitismRate = 0.8;
        population.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, population.getElitismRate(), 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        double invalidElitismRate = 1.2;
        population.setElitismRate(invalidElitismRate);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        List<Chromosome> expectedChromosomes = chromosomes.subList(1, chromosomes.size());
        assertEquals(expectedChromosomes, nextGeneration.getChromosomes());
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0);
    }

    @Test
    public void testNextGenerationSortsChromosomes() {
        chromosomes.get(0).setFitness(5);
        chromosomes.get(1).setFitness(10);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        List<Chromosome> expectedChromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosomes.get(1)));
        assertEquals(expectedChromosomes, nextGeneration.getChromosomes());
    }
}