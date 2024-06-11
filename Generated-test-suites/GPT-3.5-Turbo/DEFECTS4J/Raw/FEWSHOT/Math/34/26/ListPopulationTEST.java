package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
    private ListPopulation population;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testConstructor() {
        assertThrows(NotPositiveException.class, () -> new ListPopulation(0));
        assertThrows(NotPositiveException.class, () -> new ListPopulation(-1));
        assertThrows(NullArgumentException.class, () -> new ListPopulation(null));
        assertThrows(NumberIsTooLargeException.class, () -> new ListPopulation(Collections.singletonList(new Chromosome()), 0));
        assertThrows(NumberIsTooLargeException.class, () -> new ListPopulation(Collections.singletonList(new Chromosome()), -1));
        assertThrows(NumberIsTooLargeException.class, () -> new ListPopulation(Collections.singletonList(new Chromosome()), 1));
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        population.setChromosomes(newChromosomes);
        assertEquals(newChromosomes, population.getChromosomes());
    }

    @Test
    public void testSetChromosomesNull() {
        assertThrows(NullArgumentException.class, () -> population.setChromosomes(null));
    }

    @Test
    public void testSetChromosomesTooLarge() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        assertThrows(NumberIsTooLargeException.class, () -> population.setChromosomes(newChromosomes));
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        population.addChromosomes(newChromosomes);
        assertEquals(4, population.getPopulationSize());
    }

    @Test
    public void testAddChromosomesExceedLimit() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosomes(newChromosomes));
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = population.getChromosomes();
        assertTrue(chromosomes.contains(new Chromosome()));
        assertTrue(chromosomes.contains(new Chromosome()));
        assertTrue(chromosomes.contains(new Chromosome()));
    }

    @Test
    public void testAddChromosome() {
        population.addChromosome(new Chromosome());
        assertEquals(4, population.getPopulationSize());
    }

    @Test
    public void testAddChromosomeExceedLimit() {
        assertThrows(NumberIsTooLargeException.class, () -> {
            for (int i = 0; i < 11; i++) {
                population.addChromosome(new Chromosome());
            }
        });
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittest = population.getFittestChromosome();
        assertTrue(population.getChromosomes().contains(fittest));
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        assertThrows(NumberIsTooSmallException.class, () -> population.setPopulationLimit(2));
    }

    @Test
    public void testSetPopulationLimitNotPositive() {
        assertThrows(NotPositiveException.class, () -> population.setPopulationLimit(0));
        assertThrows(NotPositiveException.class, () -> population.setPopulationLimit(-1));
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(3, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
        }
        assertEquals(3, count);
    }
}