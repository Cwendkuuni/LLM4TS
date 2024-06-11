package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        Chromosome chromosome1 = mock(Chromosome.class);
        Chromosome chromosome2 = mock(Chromosome.class);
        Chromosome chromosome3 = mock(Chromosome.class);
        Chromosome chromosome4 = mock(Chromosome.class);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        chromosomes.add(chromosome4);

        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testConstructorWithListAndLimit() {
        assertEquals(4, population.size());
        verify(population, times(1)).setElitismRate(0.5);
    }

    @Test
    public void testConstructorWithLimit() {
        assertEquals(0, population.size());
        verify(population, times(1)).setElitismRate(0.5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithNegativeElitismRate() {
        new ElitisticListPopulation(new ArrayList<>(), 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConstructorWithElitismRateGreaterThanOne() {
        new ElitisticListPopulation(new ArrayList<>(), 10, 1.1);
    }

    @Test
    public void testNextGenerationWithDefaultElitismRate() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(4, nextGeneration.size());
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        assertEquals(chromosomes.get(0), newChromosomes.get(0));
        assertEquals(chromosomes.get(1), newChromosomes.get(1));

        int indexOfLastGoodEnoughChromosome = (int) Math.ceil((1.0 - 0.5) * chromosomes.size());
        for (int i = indexOfLastGoodEnoughChromosome; i < chromosomes.size(); i++) {
            assertEquals(chromosomes.get(i), newChromosomes.get(indexOfLastGoodEnoughChromosome + i - indexOfLastGoodEnoughChromosome));
        }
    }

    @Test
    public void testNextGenerationWithCustomElitismRate() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(6, nextGeneration.size());
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();

        int indexOfLastGoodEnoughChromosome = (int) Math.ceil((1.0 - 0.7) * chromosomes.size());
        for (int i = indexOfLastGoodEnoughChromosome; i < chromosomes.size(); i++) {
            assertEquals(chromosomes.get(i), newChromosomes.get(i));
        }

        for (int i = 0; i < indexOfLastGoodEnoughChromosome; i++) {
            assertNotSame(chromosomes.get(i), newChromosomes.get(i));
        }
    }

    @Test
    public void testSetElitismRateWithValidValue() {
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithNegativeValue() {
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithValueGreaterThanOne() {
        population.setElitismRate(1.1);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }
}