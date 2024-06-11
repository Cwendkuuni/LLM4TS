package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        chromosomes.add(new Chromosome(4));
        this.population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testConstructorWithListAndLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(this.population.getChromosomes(), 10, 0.5);
        assertEquals(this.population.getSize(), population.getSize());
        assertEquals(this.population.getElitismRate(), population.getElitismRate());
    }

    @Test
    public void testConstructorWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(this.population.getSize(), population.getSize());
        assertEquals(this.population.getElitismRate(), population.getElitismRate());
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);
        int boundIndex = (int) Math.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size());
        assertEquals(oldChromosomes.size() - boundIndex, nextGeneration.getSize());
        for (int i = 0; i < boundIndex; i++) {
            assertEquals(oldChromosomes.get(i), nextGeneration.getChromosome(i));
        }
        for (int i = oldChromosomes.size() - 1; i >= oldChromosomes.size() - boundIndex; i--) {
            assertNotSame(oldChromosomes.get(i), nextGeneration.getChromosome(i + boundIndex));
            assertNotSame(oldChromosomes.get(i), nextGeneration.getChromosome(0));
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-1);
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.001);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }
}