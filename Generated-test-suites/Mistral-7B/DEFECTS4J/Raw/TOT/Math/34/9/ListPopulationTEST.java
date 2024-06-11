package org.apache.commons.math3.genetics;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidArguments() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 5);

        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(population.getChromosomes(), initialChromosomes);

        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosome1.compareTo(chromosome2) >= 0 ? chromosome1 : chromosome2, fittestChromosome);

        population.addChromosome(new DummyChromosome(3));
        assertEquals(3, population.getPopulationSize());
        List<Chromosome> updatedChromosomes = population.getChromosomes();
        assertTrue(updatedChromosomes.contains(chromosome1));
        assertTrue(updatedChromosomes.contains(chromosome2));
        assertTrue(updatedChromosomes.contains(new DummyChromosome(3)));
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, -1);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomesList() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithTooManyChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 2);
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(4));
    }

    @Test
    public void testListPopulationWithEmptyInitialChromosomesList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
        assertTrue(population.getChromosomes().isEmpty());
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Chromosome o) {
            if (o instanceof DummyChromosome) {
                return this.id - ((DummyChromosome) o).id;
            }
            throw new IllegalArgumentException("Invalid chromosome type passed in.");
        }
    }
}