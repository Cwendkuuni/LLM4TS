package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ElitisticListPopulationTest {
    
    @Test
    public void testNextGeneration() {
        // create chromosomes for testing
        Chromosome chr1 = new Chromosome();
        Chromosome chr2 = new Chromosome();
        Chromosome chr3 = new Chromosome();
        
        // create a population with chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chr1);
        chromosomes.add(chr2);
        chromosomes.add(chr3);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        
        // get the next generation
        Population nextGeneration = population.nextGeneration();
        
        // check if the next generation has the correct size
        assertEquals(3, population.getChromosomes().size());
        assertEquals(1, nextGeneration.getChromosomes().size());
        
        // check if the correct chromosomes are in the next generation
        assertEquals(chr3, nextGeneration.getChromosomes().get(0));
    }

    @Test
    public void testSetElitismRate() {
        // create a population with default elitism rate
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        
        // set a new elitism rate
        population.setElitismRate(0.8);
        
        // check if the new elitism rate is set correctly
        assertEquals(0.8, population.getElitismRate(), 0.0);
    }
}