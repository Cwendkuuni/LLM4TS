package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testEmptyListPopulation() {
        ListPopulation population = new ListPopulation(0);
        assertTrue(population.isEmpty());
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getFittestChromosome());
        assertNotNull(population.getPopulationLimit());
        assertEquals(0, population.getPopulationLimit());
        assertNull(population.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationWithCapacity() {
        ListPopulation population = new ListPopulation(3);
        assertFalse(population.isEmpty());
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getFittestChromosome());
        assertNotNull(population.getPopulationLimit());
        assertEquals(3, population.getPopulationLimit());
        assertNull(population.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationWithInitialChromosomes() {
        Chromosome chromosome1 = new MockChromosome("Chromosome1");
        Chromosome chromosome2 = new MockChromosome("Chromosome2");
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 5);
        assertFalse(population.isEmpty());
        assertEquals(initialChromosomes.size(), population.getPopulationSize());
        assertEquals(chromosome1, population.getFittestChromosome());
        assertSame(initialChromosomes, population.getChromosomes());
        assertNotNull(population.getPopulationLimit());
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testSetChromosomes() {
        Chromosome chromosome1 = new MockChromosome("Chromosome1");
        Chromosome chromosome2 = new MockChromosome("Chromosome2");
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 5);
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new MockChromosome("Chromosome3"));
        newChromosomes.add(new MockChromosome("Chromosome4"));

        population.setChromosomes(newChromosomes);
        assertFalse(population.isEmpty());
        assertEquals(newChromosomes.size(), population.getPopulationSize());
        assertEquals(newChromosome("Chromosome3"), population.getFittestChromosome());
        assertSame(newChromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(2);
        population.addChromosomes(Collections.singletonList(new MockChromosome("Chromosome3")));
        assertFalse(population.isEmpty());
        assertEquals(3, population.getPopulationSize());
        assertEquals(new MockChromosome("Chromosome3"), population.getFittestChromosome());
    }

    @Test
    public void testAddChromosomesWithLimit() {
        ListPopulation population = new ListPopulation(2);
        population.addChromosomes(Collections.singletonList(new MockChromosome("Chromosome3")));
        population.addChromosomes(Collections.singletonList(new MockChromosome("Chromosome4")));
        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosomes(Collections.singletonList(new MockChromosome("Chromosome5"))));
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(2);
        population.setPopulationLimit(3);
        assertEquals(3, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitWithCurrentSizeBigger() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
        assertThrows(NumberIsTooSmallException.class, () -> population.setPopulationLimit(3));
    }

    @Test
    public void testSetPopulationLimitWithCurrentSizeSmaller() {
        ListPopulation population = new ListPopulation(2);
        population.setPopulationLimit(3);
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new MockChromosome("Chromosome1");
        Chromosome chromosome2 = new MockChromosome("Chromosome2");
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals("[Chromosome1, Chromosome2]", population.toString());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new MockChromosome("Chromosome1");
        Chromosome chromosome2 = new MockChromosome("Chromosome2");
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 5);
        Iterator<Chromosome> iterator = population.iterator();
        assertEquals(chromosome1, iterator.next());
        assertEquals(chromosome2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    private static class MockChromosome implements Chromosome {
        private final String name;

        public MockChromosome(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(Chromosome o) {
            if (o == null) {
                return 1;
            }
            return name.compareTo(o.getName());
        }

        @Override
        public String getName() {
            return name;
        }
    }
}