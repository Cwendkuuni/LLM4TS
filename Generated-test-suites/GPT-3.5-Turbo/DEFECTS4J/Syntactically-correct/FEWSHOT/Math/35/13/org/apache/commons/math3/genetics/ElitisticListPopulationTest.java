
package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population = new ElitisticListPopulation(chromosomes, 5, 0.5);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.getPopulationLimit());
        assertEquals(0.5, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.001);
        assertEquals(3, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalid() {
        population.setElitismRate(1.5);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testAddChromosome() {
        population.addChromosome(new Chromosome());

        assertEquals(6, population.getChromosomes().size());
    }
}
