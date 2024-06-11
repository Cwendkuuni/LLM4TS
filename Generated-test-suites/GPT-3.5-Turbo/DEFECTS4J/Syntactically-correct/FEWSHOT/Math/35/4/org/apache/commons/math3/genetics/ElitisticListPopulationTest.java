package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomesAndPopulationLimitAndElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0);
        assertEquals(3, population.getChromosomes().size());
    }
    
    @Test
    public void testConstructorWithPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0);
        assertNotNull(population.getChromosomes());
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(3));
        chromosomes.add(new Chromosome(2));
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 2, 0.5);
        
        Population nextGeneration = population.nextGeneration();
        
        assertEquals(2, nextGeneration.getPopulationLimit());
        assertEquals(0.5, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0);
        assertEquals(2, nextGeneration.getChromosomes().size());
        assertEquals(2, ((ElitisticListPopulation) nextGeneration).getChromosomes().get(0).getValue());
        assertEquals(3, ((ElitisticListPopulation) nextGeneration).getChromosomes().get(1).getValue());
    }
    
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(0.3);
        
        assertEquals(0.3, population.getElitismRate(), 0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRangeException() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(1.5);
    }
    
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        double elitismRate = population.getElitismRate();
        
        assertEquals(0.5, elitismRate, 0);
    }
}