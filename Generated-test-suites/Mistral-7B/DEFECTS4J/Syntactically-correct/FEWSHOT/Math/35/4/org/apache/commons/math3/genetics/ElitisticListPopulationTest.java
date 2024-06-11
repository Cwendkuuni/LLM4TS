package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationSize() {
        List<Chromosome> chromosomes = Collections.nCopies(5, new DummyChromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationElitismRate() {
        List<Chromosome> chromosomes = Collections.nCopies(5, new DummyChromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationNextGeneration_EmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 0.5);
        assertNull(population.nextGeneration());
    }

    @Test
    public void testElitisticListPopulationNextGeneration_NoElites() {
        List<Chromosome> chromosomes = Collections.nCopies(5, new DummyChromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        Collections.sort(chromosomes);
        population.nextGeneration();
        assertEquals(5, population.size());
    }

    @Test
    public void testElitisticListPopulationNextGeneration_SomeElites() {
        List<Chromosome> chromosomes = Collections.nCopies(10, new DummyChromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);
        Collections.sort(chromosomes);
        Population nextGeneration = population.nextGeneration();
        assertEquals(15, nextGeneration.size());
        List<Chromosome> elites = population.getChromosomes();
        List<Chromosome> newElites = nextGeneration.getChromosomes();
        for (int i = 0; i < 5; i++) {
            assertEquals(elites.get(i), newElites.get(i));
        }
    }

    @Test
    public void testElitisticListPopulationNextGeneration_AllElites() {
        List<Chromosome> chromosomes = Collections.nCopies(10, new DummyChromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 1.0);
        Collections.sort(chromosomes);
        Population nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
        List<Chromosome> elites = population.getChromosomes();
        List<Chromosome> newElites = nextGeneration.getChromosomes();
        for (int i = 0; i < 10; i++) {
            assertEquals(elites.get(i), newElites.get(i));
        }
    }

    @Test
    public void testElitisticListPopulationNextGeneration_OutOfRangeElitismRate() {
        assertThrows(OutOfRangeException.class, () -> new ElitisticListPopulation(Collections.nCopies(5, new DummyChromosome()), 5, 2.0));
    }

    @Test
    public void testElitisticListPopulationNextGeneration_NegativeElitismRate() {
        assertThrows(OutOfRangeException.class, () -> new ElitisticListPopulation(Collections.nCopies(5, new DummyChromosome()), 5, -0.5));
    }

    @Test
    public void testElitisticListPopulationNextGeneration_ZeroElitismRate() {
        List<Chromosome> chromosomes = Collections.nCopies(5, new DummyChromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.0);
        Population nextGeneration = population.nextGeneration();
        assertEquals(5, nextGeneration.size());
        for (int i = 0; i < 5; i++) {
            assertFalse(nextGeneration.getChromosome(i).equals(chromosomes.get(i)));
        }
    }

    @Test
    public void testElitisticListPopulationElitismRateChange() {
        List<Chromosome> chromosomes = Collections.nCopies(5, new DummyChromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationElitismRateOutOfRange() {
        List<Chromosome> chromosomes = Collections.nCopies(5, new DummyChromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.5);
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(2.0));
        assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-0.5));
    }

    private static class DummyChromosome implements Chromosome {
        @Override
        public double[] getGenes() {
            return new double[0];
        }

        @Override
        public void setGenes(double[] genes) {
        }

        @Override
        public int compareTo(Chromosome o) {
            return 0;
        }

        @Override
        public double getFitness() {
            return 0.0;
        }

        @Override
        public void setFitness(double fitness) {
        }

        @Override
        public String toString() {
            return "DummyChromosome{}";
        }
    }
}