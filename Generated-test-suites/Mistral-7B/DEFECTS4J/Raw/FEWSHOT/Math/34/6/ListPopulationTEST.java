package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPopulationLimit() {
        ListPopulation pop = new ListPopulation(10);
        assertEquals(10, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
    }

    @Test
    public void testListPopulationConstructorWithEmptyList() {
        List<Chromosome> chromosomes = Collections.emptyList();
        ListPopulation pop = new ListPopulation(chromosomes, 10);
        assertEquals(10, pop.getPopulationLimit());
        assertEquals(0, pop.getPopulationSize());
        assertSame(Collections.emptyList(), pop.getChromosomes());
    }

    @Test
    public void testListPopulationConstructorWithListOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FooChromosome(1));
        ListPopulation pop = new ListPopulation(chromosomes, 10);
        assertEquals(10, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosomes, pop.getChromosomes());
    }

    @Test
    public void testListPopulationConstructorWithListOfChromosomesBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FooChromosome(1));
        chromosomes.add(new FooChromosome(2));
        ListPopulation pop = assertThrows(NumberIsTooLargeException.class, () -> new ListPopulation(chromosomes, 1));
        assertEquals(1, pop.getPopulationLimit());
    }

    @Test
    public void testSetChromosomesDeprecated() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FooChromosome(1));
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 10);
        pop.setChromosomes(chromosomes);
        assertEquals(10, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosomes, pop.getChromosomes());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FooChromosome(1));
        pop.addChromosomes(chromosomes);
        assertEquals(10, pop.getPopulationLimit());
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosomes, pop.getChromosomes());
    }

    @Test
    public void testAddChromosomesBiggerThanPopulationLimit() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FooChromosome(1));
        chromosomes.add(new FooChromosome(2));
        assertThrows(NumberIsTooLargeException.class, () -> pop.addChromosomes(chromosomes));
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FooChromosome(1));
        ListPopulation pop = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, pop.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new FooChromosome(1));
        ListPopulation pop = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, pop.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 1);
        Chromosome chromosome = new FooChromosome(1);
        pop.addChromosome(chromosome);
        assertEquals(1, pop.getPopulationSize());
        assertEquals(chromosome, pop.getChromosomes().get(0));
    }

    @Test
    public void testAddChromosomeBiggerThanPopulationLimit() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 1);
        Chromosome chromosome = new FooChromosome(1);
        assertThrows(NumberIsTooLargeException.class, () -> pop.addChromosome(chromosome));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 2);
        pop.addChromosome(new FooChromosome(1));
        pop.addChromosome(new FooChromosome(2));
        Chromosome fittest = pop.getFittestChromosome();
        assertTrue(fittest.compareTo(pop.getChromosomes().get(1)) <= 0);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 10);
        assertEquals(10, pop.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitLessThanCurrentSize() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.addChromosome(new FooChromosome(1));
        pop.addChromosome(new FooChromosome(2));
        assertThrows(NumberIsTooSmallException.class, () -> pop.setPopulationLimit(3));
    }

    @Test
    public void testSetPopulationLimitGreaterThanCurrentSize() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.addChromosome(new FooChromosome(1));
        pop.addChromosome(new FooChromosome(2));
        pop.setPopulationLimit(10);
        assertEquals(5, pop.getPopulationSize());
    }

    @Test
    public void testSetPopulationLimitWithCurrentSize() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.addChromosome(new FooChromosome(1));
        pop.addChromosome(new FooChromosome(2));
        pop.setPopulationLimit(5);
        assertEquals(5, pop.getPopulationSize());
    }

    @Test
    public void testSetPopulationLimitWithNullPopulation() {
        assertThrows(NullArgumentException.class, () -> new ListPopulation(null, 5).setPopulationLimit(5));
    }

    @Test
    public void testToString() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.addChromosome(new FooChromosome(1));
        pop.addChromosome(new FooChromosome(2));
        assertEquals("[FooChromosome@7f5e7d, FooChromosome@68b83b]", pop.toString());
    }

    @Test
    public void testIterator() {
        ListPopulation pop = new ListPopulation(Collections.emptyList(), 5);
        pop.addChromosome(new FooChromosome(1));
        pop.addChromosome(new FooChromosome(2));

        Iterator<Chromosome> iterator = pop.iterator();

        List<Chromosome> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        assertEquals(pop.getChromosomes(), list);

        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    private static class FooChromosome implements Chromosome {
        private final int value;

        public FooChromosome(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(Chromosome o) {
            if (o == null) {
                return 1;
            }
            return Integer.compare(this.value, ((FooChromosome) o).value);
        }
    }
}