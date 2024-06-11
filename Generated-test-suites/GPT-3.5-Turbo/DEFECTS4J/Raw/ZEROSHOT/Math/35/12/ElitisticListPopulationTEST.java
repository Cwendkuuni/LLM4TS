package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // Create a list of chromosomes
        List<Chromosome> chromosomeList = new ArrayList<>();
        chromosomeList.add(new Chromosome());
        chromosomeList.add(new Chromosome());
        chromosomeList.add(new Chromosome());
        
        // Create a population
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomeList, 3, 0.9);
        
        // Generate next generation
        Population nextGeneration = population.nextGeneration();
        
        // Check if next generation is of correct size
        assertEquals(3, nextGeneration.getChromosomes().size());
        
        // Check if best chromosomes are present in next generation
        assertTrue(nextGeneration.getChromosomes().contains(chromosomeList.get(0)));
        assertTrue(nextGeneration.getChromosomes().contains(chromosomeList.get(1)));
        assertTrue(nextGeneration.getChromosomes().contains(chromosomeList.get(2)));
    }

    @Test
    public void testSetElitismRate() {
        // Create a population
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.9);
        
        // Set elitism rate
        population.setElitismRate(0.75);
        
        // Check if elitism rate is set correctly
        assertEquals(0.75, population.getElitismRate(), 0.001);
    }

}