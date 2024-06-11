package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreation() {
        // Test empty constructor
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);

        // Test non-empty constructor
        List<Chromosome> initialChromosomes = createChromosomesList(5);
        ElitisticListPopulation population1 = new ElitisticListPopulation(initialChromosomes, 15, 0.9);
        assertEquals(15, population1.getPopulationLimit());
        assertEquals(0.9, population1.getElitismRate(), 0.01);
        assertEquals(initialChromosomes, population1.getChromosomes());

        // Test exceptions in constructor
        try {
            new ElitisticListPopulation(null, 5, 0.9);
            fail("Expected IllegalArgumentException when passing null to constructor");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new ElitisticListPopulation(new Chromosome[0], 5, 0.9);
            fail("Expected IllegalArgumentException when passing empty chromosomes list to constructor");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new ElitisticListPopulation(createChromosomesList(5), -1, 0.9);
            fail("Expected OutOfRangeException when passing negative population limit to constructor");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            new ElitisticListPopulation(createChromosomesList(5), 15, -1);
            fail("Expected OutOfRangeException when passing negative elitism rate to constructor");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            new ElitisticListPopulation(createChromosomesList(5), 15, 1.1);
            fail("Expected OutOfRangeException when passing elitism rate > 1 to constructor");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testElitisticListPopulationNextGeneration() {
        ElitisticListPopulation population = createElitisticListPopulation(10, 0.9);

        // Test sorting and elitism when population size is <= elitismRate*size
        List<Chromosome> eliteChromosomes = population.nextGeneration().getChromosomes();
        assertEquals(10, eliteChromosomes.size());
        Collections.sort(eliteChromosomes);
        Chromosome bestChromosome = eliteChromosomes.get(0);
        assertEquals(10, population.getSize());
        assertEquals(bestChromosome, population.getChromosome(0));

        // Test sorting and elitism when population size > elitismRate*size
        population.addChromosome(createChromosome());
        eliteChromosomes = population.nextGeneration().getChromosomes();
        assertEquals(7, eliteChromosomes.size());
        Collections.sort(eliteChromosomes);
        Chromosome bestChromosomeNew = eliteChromosomes.get(0);
        assertNotEquals(bestChromosome, bestChromosomeNew);
        assertEquals(11, population.getSize());
        assertFalse(population.getChromosome(0).equals(bestChromosomeNew));
    }

    @Test
    public void testElitisticListPopulationSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.01);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0.01);

        try {
            population.setElitismRate(-0.1);
            fail("Expected OutOfRangeException when setting elitism rate < 0");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            population.setElitismRate(1.1);
            fail("Expected OutOfRangeException when setting elitism rate > 1");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    private List<Chromosome> createChromosomesList(int size) {
        Chromosome[] chromosomes = new Chromosome[size];
        for (int i = 0; i < size; i++) {
            chromosomes[i] = new Chromosome() {
                @Override
                public double[] getGenes() {
                    return new double[] {i};
                }

                @Override
                public void setGenes(double[] genes) {
                    // empty
                }

                @Override
                public double fitness() {
                    return i;
                }
            };
        }
        return (List<Chromosome>) List.of(chromosomes);
    }

    private ElitisticListPopulation createElitisticListPopulation(int populationLimit, double elitismRate) {
        List<Chromosome> initialChromosomes = createChromosomesList(5);
        return new ElitisticListPopulation(initialChromosomes, populationLimit, elitismRate);
    }

    private Chromosome createChromosome() {
        return new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[]{1.2};
            }

            @Override
            public void setGenes(double[] genes) {
                // empty
            }

            @Override
            public double fitness() {
                return 1.2;
            }
        };
    }
}