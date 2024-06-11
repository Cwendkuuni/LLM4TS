package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
    public void testListPopulationInitializationWithPositivePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
        assertNotNull(listPopulation.getChromosomes());
        assertTrue(listPopulation.getChromosomes().isEmpty());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationInitializationWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationInitializationWithEmptyList() {
        List<Chromosome> emptyList = Collections.emptyList();
        ListPopulation listPopulation = new ListPopulation(emptyList, 10);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
        assertSame(emptyList, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationInitializationWithNullEmptyList() {
        new ListPopulation(null, 10);
    }

    @Test
    public void testListPopulationInitializationWithListExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new MockChromosome(i));
        }

        try {
            new ListPopulation(chromosomes, 10);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testListPopulationInitializationWithListLessThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new MockChromosome(i));
        }
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(5, listPopulation.getPopulationSize());
        assertSame(chromosomes, listPopulation.getChromosomes());
    }

    @Test
    public void testListPopulationSetChromosomesNullList() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        try {
            listPopulation.setChromosomes(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesListExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        List<Chromosome> chromosomes = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new MockChromosome(i));
        }
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationSetChromosomesListLessThanPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        List<Chromosome> chromosomes = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            chromosomes.add(new MockChromosome(i));
        }
        listPopulation.setChromosomes(chromosomes);
        assertEquals(5, listPopulation.getPopulationLimit());
        assertEquals(3, listPopulation.getPopulationSize());
        assertSame(chromosomes, listPopulation.getChromosomes());
    }

    @Test
    public void testListPopulationAddChromosomesWithNullCollection() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        try {
            listPopulation.addChromosomes(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesCollectionExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        List<Chromosome> chromosomes = new ArrayList<>(11);
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new MockChromosome(i));
        }
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosomesCollectionLessThanPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        List<Chromosome> chromosomes = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            chromosomes.add(new MockChromosome(i));
        }
        listPopulation.addChromosomes(chromosomes);
        assertEquals(5, listPopulation.getPopulationLimit());
        assertEquals(8, listPopulation.getPopulationSize());
        assertSame(Arrays.asList(listPopulation.getChromosomes().toArray(new Chromosome[0])), chromosomes);
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        List<Chromosome> chromosomes = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new MockChromosome(i));
            listPopulation.addChromosome(chromosomes.get(i));
        }
        List<Chromosome> list = listPopulation.getChromosomes();
        assertEquals(5, listPopulation.getPopulationSize());
        assertSame(list, listPopulation.getChromosomes());
        assertSame(chromosomes, list);
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        List<Chromosome> chromosomes = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new MockChromosome(i));
            listPopulation.addChromosome(chromosomes.get(i));
        }
        List<Chromosome> list = listPopulation.getChromosomeList();
        assertEquals(5, listPopulation.getPopulationSize());
        assertEquals(chromosomes, listPopulation.getChromosomeList());
        assertSame(chromosomes, list);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        List<Chromosome> chromosomes = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new MockChromosome(i));
            listPopulation.addChromosome(chromosomes.get(i));
        }
        Chromosome fittest = listPopulation.getFittestChromosome();
        Chromosome best = chromosomes.get(0);
        assertSame(best, fittest);
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        listPopulation.setPopulationLimit(6);
        assertEquals(6, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitNegativeNumber() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        listPopulation.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitSmallerCurrentPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        listPopulation.setPopulationLimit(3);
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new MockChromosome(i);
            listPopulation.addChromosome(chromosome);
        }
        assertEquals("[Chromosome@1a6d137 Chromosome@3ca4214 Chromosome@5192bbb Chromosome@6000000 Chromosome@6e6d6c5]",
                listPopulation.toString());
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation listPopulation = new ListPopulation(new ArrayList<>(5));
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = new MockChromosome(i);
            listPopulation.addChromosome(chromosome);
        }
        Iterator<Chromosome> iterator = listPopulation.iterator();
        Chromosome fittest = listPopulation.getFittestChromosome();
        List<Chromosome> chromosomes = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            chromosomes.add(iterator.next());
        }
        assertEquals(fittest, chromosomes.get(0));
    }

    private static class MockChromosome implements Chromosome {
        private final int id;

        public MockChromosome(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Chromosome o) {
            if (o instanceof MockChromosome && this.id < ((MockChromosome) o).id) {
                return -1;
            }
            if (o instanceof MockChromosome && this.id > ((MockChromosome) o).id) {
                return 1;
            }
            if (o != null && getClass() != o.getClass()) {
                throw new IllegalArgumentException("The compared chromosomes are of different types.");
            }
            return 0;
        }
    }
}