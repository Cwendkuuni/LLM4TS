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
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithPopulationLimit() {
        ListPopulation population = new ListPopulation(3);
        assertEquals(3, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());

        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));

        assertEquals(3, population.getPopulationSize());
        assertSame(population.getChromosomeList().get(0), population.getChromosomeList().get(2));

        try {
            population.addChromosome(new DummyChromosome(4));
            fail("Should not be able to add a chromosome when the population limit is reached.");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithInitialChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new DummyChromosome(1));
        initialChromosomes.add(new DummyChromosome(2));
        initialChromosomes.add(new DummyChromosome(3));

        ListPopulation population = new ListPopulation(10, initialChromosomes);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(3, population.getPopulationSize());

        Iterator<Chromosome> iterator = population.iterator();
        Chromosome chromosome = iterator.next();
        assertEquals(1, chromosome.getId());

        chromosome = iterator.next();
        assertEquals(2, chromosome.getId());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(5);

        List<Chromosome> newChromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2));

        population.setChromosomes(newChromosomes);
        assertEquals(2, population.getPopulationSize());
        assertSame(population.getChromosomeList().get(0), newChromosomes.get(0));
        assertSame(population.getChromosomeList().get(1), newChromosomes.get(1));

        try {
            population.setChromosomes(Arrays.asList(new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5)));
            fail("Should not be able to set chromosomes when the population limit is exceeded.");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);

        List<Chromosome> newChromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2));
        population.addChromosomes(newChromosomes);
        assertEquals(5, population.getPopulationSize());
        assertSame(population.getChromosomeList().get(2), newChromosomes.get(0));
        assertSame(population.getChromosomeList().get(3), newChromosomes.get(1));

        try {
            population.addChromosomes(Arrays.asList(new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5)));
            fail("Should not be able to add chromosomes when the population limit is exceeded.");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(5);

        List<Chromosome> newChromosomes = Arrays.asList(new DummyChromosome(1), new DummyChromosome(2));
        population.addChromosomes(newChromosomes);

        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals(5, populationChromosomes.size());
        assertSame(populationChromosomes.get(2), newChromosomes.get(0));
        assertSame(populationChromosomes.get(3), newChromosomes.get(1));
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(0, population.getPopulationSize());

        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));

        assertEquals(3, population.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));

        String result = population.toString();
        assertTrue(result.contains("[Chromosome"));
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("3"));
    }

    @Test
    public void testListPopulationEmptyConstructor() {
        ListPopulation population = new ListPopulation();
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullConstructorList() {
        ListPopulation population = new ListPopulation((List<Chromosome>) null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationZeroPopulationLimit() {
        ListPopulation population = new ListPopulation(0);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullPopulationLimit() {
        ListPopulation population = new ListPopulation(null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSizeSmallerThanCurrentList() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(4));
        population.addChromosome(new DummyChromosome(5));

        population.setPopulationLimit(3);
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Chromosome o) {
            return Integer.compare(this.id, ((DummyChromosome) o).id);
        }

        @Override
        public int getFitness() {
            return this.id;
        }

        @Override
        public int getId() {
            return this.id;
        }
    }
}