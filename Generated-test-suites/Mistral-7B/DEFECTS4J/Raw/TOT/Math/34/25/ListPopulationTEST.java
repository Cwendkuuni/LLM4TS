package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Tests for the {@link ListPopulation} class.
 */
public class ListPopulationTest {

    /**
     * Test of the constructor when population limit is positive.
     */
    @Test
    public void testListPopulationConstructionWithPositiveLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
        // test adding chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.setChromosomes(chromosomes);
        assertEquals(5, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
        // test adding more chromosomes
        Chromosome chromosome3 = new DummyChromosome(3);
        population.addChromosome(chromosome3);
        assertEquals(6, population.getPopulationSize());
        assertEquals(6, population.getPopulationLimit());
    }

    /**
     * Test of the constructor when population limit is not positive.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructionWithNegativeLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test of the setChromosomes method with a null list of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    /**
     * Test of the setChromosomes method with more chromosomes than the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithMoreChromosomesThanLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new DummyChromosome(3)); // one too many
        population.setChromosomes(chromosomes);
    }

    /**
     * Test of the addChromosomes method with a null collection of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    /**
     * Test of the addChromosomes method with more chromosomes than the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithMoreChromosomesThanLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        Collection<Chromosome> additionalChromosomes = Arrays.asList(new DummyChromosome(3), new DummyChromosome(4));
        population.addChromosomes(additionalChromosomes);
    }

    /**
     * Test of the getFittestChromosome method.
     */
    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3);
        Chromosome chromosome1 = new DummyChromosome(10);
        Chromosome chromosome2 = new DummyChromosome(5);
        Chromosome chromosome3 = new DummyChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        Chromosome expectedFittestChromosome = chromosome1;
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(expectedFittestChromosome, fittestChromosome);
    }

    /**
     * Test of the getPopulationLimit method.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test of the setPopulationLimit method with a negative limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    /**
     * Test of the setPopulationLimit method with a limit smaller than the current population size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
    }

    /**
     * Test of the getPopulationSize method.
     */
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(0, population.getPopulationSize());
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
        assertEquals(3, population.getPopulationSize());
    }

    /**
     * Test of the toString method.
     */
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        String expectedToString = "[Chromosome{fitness=0.0, genotype=[1, 2, 3]}, Chromosome{fitness=0.0, genotype=[4, 5, 6]}]";
        String toString = population.toString();
        assertEquals(expectedToString, toString);
    }

    /**
     * Test of the iterator method.
     */
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new DummyChromosome(1);
        Chromosome chromosome2 = new DummyChromosome(2);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        Iterator<Chromosome> iterator = population.iterator();
        Chromosome expectedChromosome1 = chromosome1;
        Chromosome expectedChromosome2 = chromosome2;
        Chromosome chromosome;
        assertTrue(iterator.hasNext());
        chromosome = iterator.next();
        assertEquals(expectedChromosome1, chromosome);
        assertTrue(iterator.hasNext());
        chromosome = iterator.next();
        assertEquals(expectedChromosome2, chromosome);
        assertFalse(iterator.hasNext());
    }

    /**
     * Dummy implementation of the {@link Chromosome} interface.
     */
    private static class DummyChromosome implements Chromosome {

        private final int fitness;
        private final int[] genotype;

        public DummyChromosome(int fitness) {
            this.fitness = fitness;
            this.genotype = new int[0];
        }

        public DummyChromosome(int fitness, int[] genotype) {
            this.fitness = fitness;
            this.genotype = genotype;
        }

        @Override
        public double getFitness() {
            return this.fitness;
        }

        @Override
        public int[] getGenotype() {
            return this.genotype;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DummyChromosome that = (DummyChromosome) o;
            return Arrays.equals(this.genotype, that.genotype);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.genotype);
        }
    }
}