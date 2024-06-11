package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithNullPopulationLimit() {
        try {
            new ListPopulation(null);
            fail("Should have thrown an exception");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithNegativePopulationLimit() {
        try {
            new ListPopulation(-1);
            fail("Should have thrown an exception");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithEmptyListOfChromosomes() {
        ListPopulation population = new ListPopulation(0);
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getFittestChromosome());
    }

    @Test
    public void testListPopulationWithPopulationLimitBiggerThanListOfChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);

        ListPopulation population = new ListPopulation(chromosomes, 3);
        assertEquals(3, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
        assertSame(chromosome1, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationWithPopulationLimitEqualsToListOfChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, chromosomes.size());
        assertEquals(2, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
        assertSame(chromosome2, population.getChromosomes().get(1));
        assertSame(chromosome1, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationWithPopulationLimitSmallerThanListOfChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        try {
            new ListPopulation(chromosomes, 2);
            fail("Should have thrown an exception");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithNullListOfChromosomes() {
        try {
            new ListPopulation(null, 3);
            fail("Should have thrown an exception");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithEmptyList() {
        try {
            new ListPopulation(Collections.emptyList(), 3);
            fail("Should have thrown an exception");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationWithPopulationLimitBiggerThanTotalNumberOfChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        try {
            new ListPopulation(chromosomes, chromosomes.size() + 1);
            fail("Should have thrown an exception");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationSetChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        population.setChromosomes(chromosomes);

        assertEquals(2, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
        assertSame(chromosome2, population.getChromosomes().get(1));
        assertSame(chromosome1, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationAddChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        population.addChromosomes(Arrays.asList(chromosome1, chromosome2));

        assertEquals(2, population.getPopulationSize());
        assertSame(chromosome1, population.getChromosomes().get(0));
        assertSame(chromosome2, population.getChromosomes().get(1));
        assertSame(chromosome1, population.getFittestChromosome());
    }

    @Test
    public void testListPopulationAddChromosomesBiggerThanPopulationLimit() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        ListPopulation population = new ListPopulation(Collections.emptyList(), 2);

        try {
            population.addChromosomes(chromosomes);
            fail("Should have thrown an exception");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationGetChromosomes() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 3);

        List<Chromosome> list = population.getChromosomes();
        assertTrue(list.containsAll(chromosomes));
        assertFalse(list.remove(new DummyChromosome(3)));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        Chromosome chromosome3 = new DummyChromosome(3);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        ListPopulation population = new ListPopulation(chromosomes, 3);

        Chromosome fittest = population.getFittestChromosome();
        assertSame(chromosome1, fittest);

        population.setFittestChromosome(chromosome3);
        fittest = population.getFittestChromosome();
        assertSame(chromosome3, fittest);
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 3);

        population.setPopulationLimit(5);
        assertEquals(3, population.getPopulationSize());
        assertEquals(3, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationWithPopulationLimitSmallerThanCurrentPopulationSize() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 3);

        try {
            population.setPopulationLimit(1);
            fail("Should have thrown an exception");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationToString() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 3);

        assertEquals("[Chromosome@" + chromosome1.hashCode() + ", Chromosome@" + chromosome2.hashCode() + "]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 3);

        Iterator<Chromosome> iterator = population.iterator();

        assertTrue(iterator.hasNext());
        assertSame(chromosome1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(chromosome2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public int getFitness() {
            return id;
        }

        @Override
        public int compareTo(Chromosome o) {
            return this.getFitness() - o.getFitness();
        }

        @Override
        public String toString() {
            return "Chromosome{" + id + '}';
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            DummyChromosome other = (DummyChromosome) obj;
            return id == other.id;
        }
    }
}