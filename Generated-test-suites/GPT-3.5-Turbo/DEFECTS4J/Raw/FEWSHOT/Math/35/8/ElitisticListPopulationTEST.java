package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class ElitisticListPopulationTest {
    
    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        chromosomes.add(c1);
        chromosomes.add(c2);
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.0001);
    }
    
    @Test
    public void testConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        assertEquals(new ArrayList<Chromosome>(), population.getChromosomes());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.0001);
    }
    
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.0001);
    }
    
    @Test(expected = Exception.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(1.5);
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome(1.0);
        Chromosome c2 = new Chromosome(0.5);
        Chromosome c3 = new Chromosome(0.75);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.5);
        Population nextGeneration = population.nextGeneration();
        
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(c1);
        
        assertEquals(expectedChromosomes, nextGeneration.getChromosomes());
        assertEquals(3, nextGeneration.getPopulationLimit());
        assertEquals(0.5, ((ElitisticListPopulation)nextGeneration).getElitismRate(), 0.0001);
    }
}