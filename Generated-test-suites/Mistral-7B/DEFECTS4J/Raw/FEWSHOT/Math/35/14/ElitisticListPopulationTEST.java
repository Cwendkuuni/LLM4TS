package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreation() {
        List<Chromosome> chromosomes = Collections.nCopies(5, new DummyChromosome(1));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationCreationWithPopulationLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(5, population.size());
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = List.of(new DummyChromosome(1), new DummyChromosome(2), new DummyChromosome(3), new DummyChromosome(4), new DummyChromosome(5), new DummyChromosome(6), new DummyChromosome(7), new DummyChromosome(8), new DummyChromosome(9), new DummyChromosome(10));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.5);

        List<Chromosome> sortedChromosomes = Collections.unmodifiableList(Collections.sortedList(Collections.copyOf(population.getChromosomes()), new ChromosomeComparator()));
        int boundIndex = (int) Math.ceil((1.0 - population.getElitismRate()) * chromosomes.size());

        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(15, nextGeneration.size());

        for (int i = 0; i < boundIndex; i++) {
            Chromosome chromosome = sortedChromosomes.get(i);
            assertTrue(nextGeneration.contains(chromosome));
        }

        assertNull(nextGeneration.getChromosome(boundIndex));
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.01);

        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        population.setElitismRate(-0.1);
    }
}

class DummyChromosome implements Chromosome {
    int fitness;

    public DummyChromosome(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public int getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "DummyChromosome{" +
                "fitness=" + fitness +
                '}';
    }
}

class ChromosomeComparator implements Comparator<Chromosome> {
    @Override
    public int compare(Chromosome o1, Chromosome o2) {
        return o1.getFitness() - o2.getFitness();
    }
}