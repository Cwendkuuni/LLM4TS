package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ElitisticListPopulationTest {
    
    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        
        Population newGeneration = population.nextGeneration();
        
        assertEquals(90, newGeneration.getChromosomes().size());
        assertTrue(newGeneration.getChromosomes().contains(chromosomes.get(1)));
        assertTrue(newGeneration.getChromosomes().contains(chromosomes.get(2)));
    }
    
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        population.setElitismRate(0.8);
        
        assertEquals(0.8, population.getElitismRate(), 0.00001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalid() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        population.setElitismRate(1.5);
    }
}