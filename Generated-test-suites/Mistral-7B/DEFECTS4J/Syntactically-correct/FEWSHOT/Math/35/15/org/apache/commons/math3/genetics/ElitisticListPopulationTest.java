package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithInitialChromosomes() {
        List<Chromosome> initialChromosomes = createChromosomesList(5);
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 10, 0.5);
        assertEquals(10, population.size());
        assertTrue(populationIsSorted(population));
    }

    @Test
    public void testElitisticListPopulationWithPopulationLimit() {
        List<Chromosome> initialChromosomes = createChromosomesList(6);
        ElitisticListPopulation population = new ElitisticListPopulation(6, 0.5);
        assertEquals(6, population.size());
        assertTrue(populationIsSorted(population));
    }

    @Test
    public void testNextGenerationWithInitialChromosomes() {
        List<Chromosome> initialChromosomes = createChromosomesList(5);
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 10, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(10, nextGeneration.size());
        assertTrue(nextGenerationIsElitistic(nextGeneration, population));
        assertTrue(populationIsSorted(population));
    }

    @Test
    public void testNextGenerationWithPopulationLimit() {
        List<Chromosome> initialChromosomes = createChromosomesList(6);
        ElitisticListPopulation population = new ElitisticListPopulation(6, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(6, nextGeneration.size());
        assertTrue(nextGenerationIsElitistic(nextGeneration, population));
        assertTrue(populationIsSorted(population));
    }

    @Test
    public void testElitismRateOutOfRange() {
        List<Chromosome> initialChromosomes = createChromosomesList(5);
        try {
            new ElitisticListPopulation(initialChromosomes, 10, 2.0);
            assert false;
        } catch (OutOfRangeException e) {
            assertEquals("Elitism rate is outside the [0, 1] range", e.getMessage());
        }
    }

    @Test
    public void testSetElitismRate() {
        List<Chromosome> initialChromosomes = createChromosomesList(5);
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate());
    }

    @Test
    public void testGetElitismRate() {
        List<Chromosome> initialChromosomes = createChromosomesList(5);
        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 10, 0.5);
        assertEquals(0.5, population.getElitismRate());
    }

    private List<Chromosome> createChromosomesList(int size) {
        return Collections.nCopies(size, new DummyChromosome());
    }

    private boolean populationIsSorted(List<Chromosome> population) {
        for (int i = 1; i < population.size(); i++) {
            Chromosome previous = population.get(i - 1);
            Chromosome current = population.get(i);
            return previous.compareTo(current) < 0;
        }
        return true;
    }

    private boolean nextGenerationIsElitistic(List<Chromosome> nextGeneration, List<Chromosome> population) {
        int elitismSize = (int) Math.ceil(population.size() * getElitismRate(population));
        List<Chromosome> elitisticChromosomes = Collections.nCopies(elitismSize, null);
        int index = 0;
        for (Chromosome chromosome : population) {
            if (elitisticChromosomes[index] == null) {
                elitisticChromosomes[index] = chromosome;
                index++;
            } else {
                if (chromosome.compareTo(elitisticChromosomes.get(index)) > 0) {
                    elitisticChromosomes[index] = chromosome;
                }
            }
        }
        for (Chromosome chromosome : nextGeneration) {
            boolean isElitistic = false;
            for (Chromosome elitisticChromosome : elitisticChromosomes) {
                if (chromosome.equals(elitisticChromosome)) {
                    isElitistic = true;
                    break;
                }
            }
            assertTrue(isElitistic);
        }
        return true;
    }

    private double getElitismRate(List<Chromosome> population) {
        return population.size() > 0 ? population.get(population.size() - 1).getFitness() : 0;
    }

    private static class DummyChromosome implements Chromosome {
        @Override
        public double getFitness() {
            return 0.0;
        }

        @Override
        public int compareTo(Chromosome o) {
            return 0;
        }
    }
}