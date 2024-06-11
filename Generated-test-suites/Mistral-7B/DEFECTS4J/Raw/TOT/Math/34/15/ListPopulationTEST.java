package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidParameters() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationSize());
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        assertEquals(7, population.getPopulationSize());
        assertEquals(population.getFittestChromosome(), population.getChromosomes().get(0));
    }

    @Test
    public void testListPopulationWithNullChromosomes() {
        try {
            new ListPopulation(null, 5);
            fail("Should have thrown NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationWithNegativePopulationLimit() {
        try {
            new ListPopulation(-1);
            fail("Should have thrown NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationWithPopulationLimitBiggerThanSizeOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        try {
            new ListPopulation(chromosomes, 10);
            fail("Should have thrown NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationWithPopulationLimitSmallerThanSizeOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        try {
            new ListPopulation(chromosomes, 1);
            fail("Should have thrown NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ListPopulation population = new ListPopulation(population -> {});
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes.get(0), population.getChromosomes().get(0));
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Collections.singletonList(new DummyChromosome(1)));
        assertEquals(6, population.getPopulationSize());
        population.addChromosomes(Collections.singletonList(new DummyChromosome(2)));
        assertEquals(7, population.getPopulationSize());
    }

    @Test
    public void testListPopulationGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomes());
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Chromosome o) {
            return this.id > o.getId() ? 1 : -1;
        }

        public int getId() {
            return id;
        }
    }
}