package org.apache.commons.math3.genetics;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ElitisticListPopulation.FitnessComparator;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreation() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationCreationWithInitialChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.size());
        assertEquals(chromosomes.get(0), population.get(0));
        assertEquals(chromosomes.get(1), population.get(1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationWithNegativeElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationWithElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = new ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double fitness() {
                    return i;
                }
            });
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.9);
        Collections.sort(population, new FitnessComparator());

        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(15, nextGeneration.size());
        for (int i = 0; i < 12; i++) {
            Chromosome chromosome = population.get(i);
            Chromosome nextChromosome = nextGeneration.get(i);
            assertEquals(chromosome, nextChromosome);
        }
        // the three worst chromosomes are replaced by new ones
        for (int i = 12; i < 15; i++) {
            Chromosome chromosome = population.get(i);
            assertNotSame(chromosome, nextGeneration.get(i));
        }
    }

}