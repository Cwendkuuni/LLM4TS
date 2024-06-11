package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link ListPopulation} class.
 */
public class ListPopulationTest {

    /**
     * Tests constructor with valid population limit.
     */
    @Test
    public void testListPopulationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Tests constructor with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Tests adding chromosomes to the population with valid list of chromosomes.
     */
    @Test
    public void testAddChromosomesWithValidList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new MockChromosome(1));
        population.addChromosomes(chromosomes);
        assertEquals(5, population.getPopulationSize());
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertEquals(1, actualChromosomes.get(0).getValue());
    }

    /**
     * Tests adding chromosomes to the population with invalid list of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    /**
     * Tests adding chromosomes to the population with list of chromosomes bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new MockChromosome(1));
        chromosomes.add(new MockChromosome(2));
        population.addChromosomes(chromosomes);
    }

    /**
     * Tests setting chromosomes to the population with valid list of chromosomes.
     */
    @Test
    public void testSetChromosomesWithValidList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new MockChromosome(1));
        population.setChromosomes(chromosomes);
        assertEquals(5, population.getPopulationSize());
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertEquals(1, actualChromosomes.get(0).getValue());
    }

    /**
     * Tests setting chromosomes to the population with invalid list of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    /**
     * Tests setting chromosomes to the population with list of chromosomes bigger than population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithListBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new MockChromosome(1));
        chromosomes.add(new MockChromosome(2));
        population.setChromosomes(chromosomes);
    }

    /**
     * Mock chromosome for testing.
     */
    private static class MockChromosome implements Chromosome {
        private final int value;

        public MockChromosome(int value) {
            this.value = value;
        }

        @Override
        public int getFitness() {
            return value;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MockChromosome other = (MockChromosome) obj;
            if (this.value != other.value) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + this.value;
            return hash;
        }
    }
}