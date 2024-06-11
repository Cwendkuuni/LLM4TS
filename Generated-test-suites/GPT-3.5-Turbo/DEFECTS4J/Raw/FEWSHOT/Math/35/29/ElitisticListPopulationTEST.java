package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        population = new ElitisticListPopulation(chromosomes, 5, 0.7);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        
        assertEquals(3, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.5);
        
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(1.5);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.7, population.getElitismRate(), 0.001);
    }
    
    // Add more test cases to comprehensively test all methods in the ElitisticListPopulation class

}