package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;

/**
 * Test class for ListPopulation
 */
public class ListPopulationTest {

    /**
     * Tests constructor ListPopulation(int).
     */
    @Test
    public void testListPopulationInt() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(Collections.emptyList(), population.getChromosomes());
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Tests constructor ListPopulation(List, int).
     */
    @Test
    public void testListPopulationListInt() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Tests method setChromosomes(List).
     */
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);

        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Tests method addChromosomes(Collection).
     */
    @Test
    public void testAddChromosomesCollection() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Tests method getChromosomes().
     */
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(1, chromosomes.size());
        assertEquals(new DummyChromosome(1), chromosomes.get(0));
    }

    /**
     * Tests method getChromosomeList().
     */
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(1, chromosomes.size());
        assertEquals(new DummyChromosome(1), chromosomes.get(0));
    }

    /**
     * Tests method addChromosome(Chromosome).
     */
    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new DummyChromosome(1);

        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Tests method getFittestChromosome().
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        Chromosome fittest = population.getFittestChromosome();
        assertEquals(new DummyChromosome(1), fittest);
    }

    /**
     * Tests method getPopulationLimit().
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Tests method setPopulationLimit(int).
     */
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Tests method getPopulationSize().
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        assertEquals(1, population.getPopulationSize());
    }

    /**
     * Tests method toString().
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        assertEquals("[DummyChromosome@" + population.hashCode() + "]", population.toString());
    }

    /**
     * Tests method iterator().
     */
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new DummyChromosome(1), iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test constructor ListPopulation(int) with negative population limit.
     */
    @Test
    public void testListPopulationIntNegativePopulationLimit() {
        assertThrows(NotPositiveException.class, () -> new ListPopulation(-1));
    }

    /**
     * Test constructor ListPopulation(int) with zero population limit.
     */
    @Test
    public void testListPopulationIntZeroPopulationLimit() {
        assertThrows(NotPositiveException.class, () -> new ListPopulation(0));
    }

    /**
     * Test constructor ListPopulation(List, int) with null list of chromosomes.
     */
    @Test
    public void testListPopulationListIntNullChromosomes() {
        assertThrows(NullArgumentException.class, () -> new ListPopulation(null, 5));
    }

    /**
     * Test constructor ListPopulation(List, int) with a list of chromosomes bigger than population limit.
     */
    @Test
    public void testListPopulationListIntTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new DummyChromosome(3));

        assertThrows(NumberIsTooLargeException.class, () -> new ListPopulation(chromosomes, 2));
    }

    /**
     * Test method setChromosomes(List) with null list of chromosomes.
     */
    @Test
    public void testSetChromosomesNullList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        assertThrows(NullArgumentException.class, () -> population.setChromosomes(null));
    }

    /**
     * Test method addChromosomes(Collection) with null collection of chromosomes.
     */
    @Test
    public void testAddChromosomesNullCollection() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        assertThrows(NullArgumentException.class, () -> population.addChromosomes(null));
    }

    /**
     * Test method addChromosomes(Collection) with a collection of chromosomes bigger than population limit.
     */
    @Test
    public void testAddChromosomesCollectionTooManyChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new DummyChromosome(3));

        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosomes(chromosomes));
    }

    /**
     * Test method setPopulationLimit(int) with negative population limit.
     */
    @Test
    public void testSetPopulationLimitNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        assertThrows(NotPositiveException.class, () -> population.setPopulationLimit(-1));
    }

    /**
     * Test method setPopulationLimit(int) with population limit smaller than current population size.
     */
    @Test
    public void testSetPopulationLimitSmallerCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(Collections.singletonList(new DummyChromosome(1)), 5);
        assertThrows(NumberIsTooSmallException.class, () -> population.setPopulationLimit(3));
    }

    private static class DummyChromosome implements Chromosome {
        private int value;

        public DummyChromosome(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(Chromosome o) {
            return this.value - ((DummyChromosome) o).value;
        }
    }

    private static class DummyRandom implements Random {
        @Override
        public int nextInt(int n) {
            return 0;
        }

        @Override
        public long nextLong() {
            return 0;
        }
    }
}