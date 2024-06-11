package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithEmptyListAndLimit() {
        ListPopulation population = new ListPopulation(1);
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationWithNullListAndLimit() {
        assertThrows(NullArgumentException.class, () -> new ListPopulation(null, 1));
    }

    @Test
    public void testListPopulationWithEmptyListAndNegativeLimit() {
        assertThrows(NotPositiveException.class, () -> new ListPopulation(Collections.emptyList(), -1));
    }

    @Test
    public void testListPopulationWithNullListAndNegativeLimit() {
        assertThrows(NullArgumentException.class, () -> new ListPopulation(null, -1));
    }

    @Test
    public void testListPopulationWithEmptyListAndZeroLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 0);
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationWithEmptyListAndPositiveLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 1);
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getChromosomes().iterator().next());

        population.addChromosome(new DummyChromosome("First"));
        assertEquals(1, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
    }

    @Test
    public void testListPopulationWithListAndLimit() {
        List<DummyChromosome> chromosomes = Arrays.asList(new DummyChromosome("First"), new DummyChromosome("Second"));
        ListPopulation population = new ListPopulation(chromosomes, 3);
        assertEquals(2, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
        assertEquals("Second", population.getChromosomes().get(1).toString());

        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosome(new DummyChromosome("Third")));
    }

    @Test
    public void testListPopulationWithNullCollectionAndLimit() {
        assertThrows(NullArgumentException.class, () -> new ListPopulation(null, 1));
    }

    @Test
    public void testListPopulationWithListAndNegativeLimit() {
        List<DummyChromosome> chromosomes = Arrays.asList(new DummyChromosome("First"), new DummyChromosome("Second"));
        assertThrows(NotPositiveException.class, () -> new ListPopulation(chromosomes, -1));
    }

    @Test
    public void testListPopulationWithListAndZeroLimit() {
        List<DummyChromosome> chromosomes = Arrays.asList(new DummyChromosome("First"), new DummyChromosome("Second"));
        ListPopulation population = new ListPopulation(chromosomes, 0);
        assertEquals(2, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
        assertEquals("Second", population.getChromosomes().get(1).toString());

        population.clear();
        assertEquals(0, population.getPopulationSize());
        assertNull(population.getChromosomes().iterator().next());
    }

    @Test
    public void testListPopulationWithListAndPositiveLimit() {
        List<DummyChromosome> chromosomes = Arrays.asList(new DummyChromosome("First"), new DummyChromosome("Second"));
        ListPopulation population = new ListPopulation(chromosomes, 3);
        assertEquals(2, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
        assertEquals("Second", population.getChromosomes().get(1).toString());

        population.addChromosome(new DummyChromosome("Third"));
        assertEquals(3, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
        assertEquals("Second", population.getChromosomes().get(1).toString());
        assertEquals("Third", population.getChromosomes().get(2).toString());
    }

    @Test
    public void testSetChromosomes() {
        List<DummyChromosome> chromosomes = Arrays.asList(new DummyChromosome("First"), new DummyChromosome("Second"));
        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
        assertEquals("Second", population.getChromosomes().get(1).toString());

        assertThrows(NumberIsTooLargeException.class, () -> population.addChromosome(new DummyChromosome("Third")));
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 3);
        population.addChromosomes(Arrays.asList(new DummyChromosome("First"), new DummyChromosome("Second")));
        assertEquals(2, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
        assertEquals("Second", population.getChromosomes().get(1).toString());

        population.addChromosomes(Arrays.asList(new DummyChromosome("Third"), new DummyChromosome("Fourth")));
        assertEquals(4, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
        assertEquals("Second", population.getChromosomes().get(1).toString());
        assertEquals("Third", population.getChromosomes().get(2).toString());
        assertEquals("Fourth", population.getChromosomes().get(3).toString());
    }

    @Test
    public void testGetFittestChromosome() {
        List<DummyChromosome> chromosomes = Arrays.asList(new DummyChromosome("First", 10), new DummyChromosome("Second", 15),
                new DummyChromosome("Third", 5));
        ListPopulation population = new ListPopulation(chromosomes, 3);
        assertEquals("Second", population.getFittestChromosome().toString());
    }

    @Test
    public void testSetPopulationLimit() {
        List<DummyChromosome> chromosomes = Arrays.asList(new DummyChromosome("First"), new DummyChromosome("Second"),
                new DummyChromosome("Third"), new DummyChromosome("Fourth"));
        ListPopulation population = new ListPopulation(chromosomes, 4);

        population.setPopulationLimit(3);
        assertEquals(3, population.getPopulationSize());
        assertEquals("First", population.getChromosomes().get(0).toString());
        assertEquals("Second", population.getChromosomes().get(1).toString());
        assertEquals("Third", population.getChromosomes().get(2).toString());

        assertThrows(NotPositiveException.class, () -> population.setPopulationLimit(0));

        population.setPopulationLimit(5);
        assertEquals(4, population.getPopulationSize());
    }
}

class DummyChromosome implements Chromosome {
    private String name;
    private int fitness;

    public DummyChromosome(String name) {
        this.name = name;
        this.fitness = 0;
    }

    public DummyChromosome(String name, int fitness) {
        this.name = name;
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Chromosome o) {
        DummyChromosome other = (DummyChromosome) o;
        return Integer.compare(this.fitness, other.fitness);
    }

    @Override
    public String toString() {
        return this.name;
    }
}