package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithEmptyConstructor() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(0, population.getPopulationSize());
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testListPopulationWithCollectionConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome("chromosome1"));
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithLargeNumberOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new DummyChromosome("chromosome" + i));
        }
        ListPopulation population = new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome("chromosome1"));
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetNullChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetLargeNumberOfChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new DummyChromosome("chromosome" + i));
        }
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(new DummyChromosome("chromosome1"));
        population.addChromosomes(chromosomesToAdd);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosomesToAdd, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomesToAdd() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationLargeNumberOfChromosomesToAdd() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomesToAdd.add(new DummyChromosome("chromosome" + i));
        }
        population.addChromosomes(chromosomesToAdd);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosome(new DummyChromosome("fittest"));
        population.addChromosome(new DummyChromosome("notFittest"));
        Chromosome fittest = population.getFittestChromosome();
        assertEquals("fittest", fittest.toString());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationPopulationLimitSmallerThanCurrentSize() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.addChromosome(new DummyChromosome("chromosome1"));
        population.addChromosome(new DummyChromosome("chromosome2"));
        population.setPopulationLimit(2);
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosome(new DummyChromosome("chromosome1"));
        population.addChromosome(new DummyChromosome("chromosome2"));
        assertEquals("[chromosome1, chromosome2]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.addChromosome(new DummyChromosome("chromosome1"));
        population.addChromosome(new DummyChromosome("chromosome2"));
        Iterator<Chromosome> iterator = population.iterator();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(iterator.next());
        chromosomes.add(iterator.next());
        assertEquals(chromosomes, population.getChromosomes());
    }
}

class DummyChromosome implements Chromosome {
    private String name;

    public DummyChromosome(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Chromosome o) {
        if (o instanceof DummyChromosome) {
            DummyChromosome other = (DummyChromosome) o;
            return this.name.compareTo(other.name);
        }
        throw new UnsupportedOperationException();
    }
}