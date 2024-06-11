package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {
    private ElitisticListPopulation population;
    private ArrayList<Chromosome> chromosomes;
    private double elitismRate;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<Chromosome>();
        elitismRate = 0.9;
        population = new ElitisticListPopulation(chromosomes, 100, elitismRate);
    }

    @After
    public void tearDown() {
        population = null;
        chromosomes = null;
    }

    @Test
    public void testConstructorWithChromosomesAndLimit() {
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }

    @Test
    public void testConstructorWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.5);
        assertNotNull(population.getChromosomes());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    @Test
    public void testNextGeneration() {
        population.addChromosome(new TestChromosome(1));
        population.addChromosome(new TestChromosome(2));
        population.addChromosome(new TestChromosome(3));
        population.addChromosome(new TestChromosome(4));
        population.addChromosome(new TestChromosome(5));
        population.addChromosome(new TestChromosome(6));

        Population nextGeneration = population.nextGeneration();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        
        assertEquals(2, nextGenerationChromosomes.size());
        assertTrue(nextGenerationChromosomes.contains(new TestChromosome(5)));
        assertTrue(nextGenerationChromosomes.contains(new TestChromosome(6)));
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.01);
    }

    @Test(expected=OutOfRangeException.class)
    public void testSetElitismRateException() {
        population.setElitismRate(-0.5);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(elitismRate, population.getElitismRate(), 0.01);
    }
}

class TestChromosome extends Chromosome {
    private int value;
    
    public TestChromosome(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public double fitness() {
        return 0;
    }
}