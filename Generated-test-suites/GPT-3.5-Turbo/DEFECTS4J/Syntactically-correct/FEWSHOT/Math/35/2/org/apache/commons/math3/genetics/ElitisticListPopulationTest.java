package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ElitisticListPopulationTest {
    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.5);
        
        assertNotNull(population);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }
    
    @Test
    public void testConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.5);
        
        assertNotNull(population);
        assertNotNull(population.getChromosomes());
        assertEquals(0, population.getChromosomes().size());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome(10));
        chromosomes.add(new Chromosome(20));
        chromosomes.add(new Chromosome(30));
        chromosomes.add(new Chromosome(40));
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        
        assertNotNull(nextGeneration);
        assertEquals(3, nextGeneration.getChromosomes().size());
        assertTrue(nextGeneration.getChromosomes().contains(chromosomes.get(2)));
        assertTrue(nextGeneration.getChromosomes().contains(chromosomes.get(3)));
        assertFalse(nextGeneration.getChromosomes().contains(chromosomes.get(0)));
        assertFalse(nextGeneration.getChromosomes().contains(chromosomes.get(1)));
    }
    
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.5);
        population.setElitismRate(0.3);
        assertEquals(0.3, population.getElitismRate(), 0.0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.5);
        population.setElitismRate(1.5);
    }
    
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }
}