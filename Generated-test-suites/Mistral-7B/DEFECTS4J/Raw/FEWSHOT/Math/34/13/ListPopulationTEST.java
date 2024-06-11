package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;

@RunWith(Parameterized.class)
public class ListPopulationTest {

    private int populationLimit;
    private ListPopulation population;

    public ListPopulationTest(int populationLimit) {
        this.populationLimit = populationLimit;
    }

    @Parameterized.Parameters(name = "Population with limit {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{1},
                new Object[]{5},
                new Object[]{10},
                new Object[]{20},
                new Object[]{100},
        });
    }

    @Test
    public void testConstructorWithLimit() {
        population = new ListPopulation(populationLimit);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(Collections.emptyList(), population.getChromosomes());
        assertEquals(populationLimit, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeLimit() {
        population = new ListPopulation(-1);
    }

    @Test
    public void testConstructorWithEmptyList() {
        Chromosome[] chromosomes = new Chromosome[0];
        population = new ListPopulation(chromosomes, populationLimit);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(Arrays.asList(chromosomes), population.getChromosomes());
        assertEquals(chromosomes.length, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullList() {
        population = new ListPopulation(null, populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithBigList() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome[populationLimit + 1]);
        population = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomes() {
        Chromosome[] chromosomes = new Chromosome[5];
        population = new ListPopulation(Collections.emptyList(), populationLimit);
        population.setChromosomes(Arrays.asList(chromosomes));
        assertEquals(Arrays.asList(chromosomes), population.getChromosomes());
        assertEquals(chromosomes.length, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        population = new ListPopulation(Collections.emptyList(), populationLimit);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithBigList() {
        Chromosome[] chromosomes = new Chromosome[populationLimit + 1];
        population = new ListPopulation(Collections.emptyList(), populationLimit);
        population.setChromosomes(Arrays.asList(chromosomes));
    }

    @Test
    public void testAddChromosomes() {
        Chromosome[] chromosomes = new Chromosome[5];
        population = new ListPopulation(Collections.emptyList(), populationLimit);
        population.addChromosomes(Arrays.asList(chromosomes[0], chromosomes[1]));
        List<Chromosome> expected = new ArrayList<>(populationLimit);
        expected.add(chromosomes[0]);
        expected.add(chromosomes[1]);
        assertEquals(expected, population.getChromosomes());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesWithNullList() {
        population = new ListPopulation(Collections.emptyList(), populationLimit);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithBigList() {
        Chromosome[] chromosomes = new Chromosome[populationLimit + 1];
        population = new ListPopulation(Collections.emptyList(), populationLimit);
        population.addChromosomes(Arrays.asList(chromosomes));
    }

    @Test
    public void testGetChromosomes() {
        Chromosome[] chromosomes = new Chromosome[5];
        population = new ListPopulation(Arrays.asList(chromosomes[0], chromosomes[1]), populationLimit);
        List<Chromosome> expected = Arrays.asList(chromosomes[0], chromosomes[1]);
        assertEquals(expected, population.getChromosomes());
    }

    @Test
    public void testGetPopulationSize() {
        Chromosome[] chromosomes = new Chromosome[5];
        population = new ListPopulation(Arrays.asList(chromosomes[0], chromosomes[1]), populationLimit);
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testGetPopulationLimit() {
        Chromosome[] chromosomes = new Chromosome[5];
        population = new ListPopulation(Arrays.asList(chromosomes[0], chromosomes[1]), populationLimit);
        assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeNumber() {
        population = new ListPopulation(Collections.emptyList(), populationLimit);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerSize() {
        population = new ListPopulation(Collections.emptyList(), populationLimit);
        population.setPopulationLimit(population.getPopulationSize() - 1);
    }

    @Test
    public void testFittestChromosome() {
        Chromosome[] chromosomes = new Chromosome[5];
        Chromosome best = new DummyChromosome(0);
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new DummyChromosome(i + 1);
        }
        population = new ListPopulation(Arrays.asList(chromosomes), populationLimit);
        Chromosome expected = best;
        for (Chromosome chromosome : population.getChromosomes()) {
            if (chromosome.compareTo(best) > 0) {
                expected = chromosome;
            }
        }
        assertSame(expected, population.getFittestChromosome());
    }

    private static class DummyChromosome implements Chromosome {

        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Chromosome another) {
            return Integer.compare(this.id, ((DummyChromosome) another).id);
        }

        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }
}