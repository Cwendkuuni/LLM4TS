package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

/**
 * Tests for the abstract {@link ListPopulation} class.
 */
public class ListPopulationTest {

    /**
     * Tests constructor with valid population limit.
     */
    @Test
    public void testListPopulationConstructorValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Tests constructor with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Tests empty constructor and setting chromosomes.
     */
    @Test
    public void testListPopulationEmptyConstructorAndSettingChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());

        List<Chromosome> chromosomes = new ArrayList<>(2);
        Chromosome chromosome1 = new MockChromosome(1);
        Chromosome chromosome2 = new MockChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
        assertSame(chromosome2, population.getChromosomes().get(1));
    }

    /**
     * Tests setting chromosomes with size larger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSettingChromosomesSizeLargerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>(10);
        Chromosome chromosome = new MockChromosome(1);
        chromosomes.add(chromosome);
        population.setChromosomes(chromosomes);
    }

    /**
     * Tests adding chromosomes to existing population.
     */
    @Test
    public void testListPopulationAddingChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.addChromosomes(Arrays.asList(new MockChromosome(1), new MockChromosome(2)));
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Tests adding chromosomes to population larger than limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddingChromosomesToPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(Arrays.asList(new MockChromosome(1), new MockChromosome(2), new MockChromosome(3)));
    }

    /**
     * Tests getChromosomes method.
     */
    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.addChromosomes(Arrays.asList(new MockChromosome(1), new MockChromosome(2)));
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(2, chromosomes.size());
        assertSame(population.getChromosomes().get(0), chromosomes.get(0));
        assertSame(population.getChromosomes().get(1), chromosomes.get(1));
    }

    /**
     * Tests getChromosomeList method.
     */
    @Test
    public void testListPopulationGetChromosomeList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.addChromosomes(Arrays.asList(new MockChromosome(1), new MockChromosome(2)));
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(2, chromosomes.size());
        assertSame(population.getChromosomeList().get(0), chromosomes.get(0));
        assertSame(population.getChromosomeList().get(1), chromosomes.get(1));
    }

    /**
     * Tests addChromosome method.
     */
    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.addChromosome(new MockChromosome(1));
        assertEquals(1, population.getPopulationSize());
    }

    /**
     * Tests addChromosome method with population limit exceeded.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 1);
        population.addChromosome(new MockChromosome(1));
        population.addChromosome(new MockChromosome(2));
    }

    /**
     * Tests getFittestChromosome method.
     */
    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2)), 2);
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(1, fittestChromosome.compareTo(population.getChromosomes().get(0)));
    }

    /**
     * Tests getPopulationLimit method.
     */
    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2)), 2);
        assertEquals(2, population.getPopulationLimit());
    }

    /**
     * Tests setPopulationLimit method with valid value.
     */
    @Test
    public void testListPopulationSetPopulationLimitValidValue() {
        ListPopulation population = new ListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2)), 2);
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Tests setPopulationLimit method with invalid value.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitInvalidValue() {
        ListPopulation population = new ListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2)), 2);
        population.setPopulationLimit(-1);
    }

    /**
     * Tests setPopulationLimit method with value smaller than current number of chromosomes.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitValueSmallerThanCurrentNumberOfChromosomes() {
        ListPopulation population = new ListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2)), 2);
        population.setPopulationLimit(1);
    }

    /**
     * Tests getPopulationSize method.
     */
    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2)), 2);
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Tests toString method.
     */
    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2)), 2);
        assertEquals("[MockChromosome@" + Long.toHexString(System.identityHashCode(population.getChromosomes().get(0))) + ", MockChromosome@"
                + Long.toHexString(System.identityHashCode(population.getChromosomes().get(1))) + "]", population.toString());
    }

    /**
     * Tests iterator method.
     */
    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(Arrays.asList(new MockChromosome(1), new MockChromosome(2)), 2);
        Iterator<Chromosome> iterator = population.iterator();
        assertEquals(population.getChromosomes().get(0), iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Mock chromosome for testing.
     */
    private static class MockChromosome implements Chromosome {

        private final int id;

        public MockChromosome(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Chromosome o) {
            if (o instanceof MockChromosome) {
                MockChromosome other = (MockChromosome) o;
                return Integer.compare(this.id, other.id);
            }
            throw new ClassCastException();
        }
    }
}