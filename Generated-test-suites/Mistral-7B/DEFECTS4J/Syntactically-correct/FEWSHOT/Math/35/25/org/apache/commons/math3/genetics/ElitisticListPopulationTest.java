package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationWithInitialChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new FitnessScaledChromosome(1.0, new byte[10]);
        Chromosome c2 = new FitnessScaledChromosome(2.0, new byte[10]);
        Chromosome c3 = new FitnessScaledChromosome(3.0, new byte[10]);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        List<Chromosome> sortedChromosomes = new ArrayList<>(population);
        Collections.sort(sortedChromosomes);

        assertEquals(3, population.size());
        assertEquals(c3, population.get(2));
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new FitnessScaledChromosome(1.0, new byte[10]);
        Chromosome c2 = new FitnessScaledChromosome(2.0, new byte[10]);
        Chromosome c3 = new FitnessScaledChromosome(3.0, new byte[10]);
        Chromosome c4 = new FitnessScaledChromosome(4.0, new byte[10]);
        Chromosome c5 = new FitnessScaledChromosome(1.5, new byte[10]);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        chromosomes.add(c4);
        chromosomes.add(c5);

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        List<Chromosome> oldChromosomes = new ArrayList<>(population);
        Collections.sort(oldChromosomes);

        ElitisticListPopulation nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.size());
        assertTrue(nextGeneration.contains(population.getChromosome(3)));
        assertTrue(nextGeneration.contains(population.getChromosome(4)));
        assertThrows(IndexOutOfBoundsException.class, () -> nextGeneration.get(4));
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.01);

        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }
}