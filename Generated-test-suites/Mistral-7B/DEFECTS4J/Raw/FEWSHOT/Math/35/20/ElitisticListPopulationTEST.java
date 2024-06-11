package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.FitnessEvaluator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    /**
     * Test constructor with list and limit.
     */
    @Test
    public void testElitisticListPopulationWithListAndLimit() {
        List<Chromosome> chromosomes = createChromosomes();
        int populationLimit = 10;
        double elitismRate = 0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Test constructor with limit and elitism rate.
     */
    @Test
    public void testElitisticListPopulationWithLimitAndElitismRate() {
        int populationLimit = 10;
        double elitismRate = 0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertNull(population.getChromosomes());
    }

    /**
     * Test getting elitism rate.
     */
    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    /**
     * Test setting elitism rate.
     */
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0.001);
    }

    /**
     * Test init population with elitism.
     */
    @Test
    public void testInitPopulationWithElitism() {
        List<Chromosome> chromosomes = createChromosomes();
        int populationLimit = 10;
        double elitismRate = 0.5;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        int boundIndex = (int) FastMath.ceil((1.0 - elitismRate) * oldChromosomes.size());
        List<Chromosome> newChromosomes = population.nextGeneration().getChromosomes();
        assertEquals(populationLimit, newChromosomes.size());

        for (int i = 0; i < boundIndex; i++) {
            assertEquals(oldChromosomes.get(i), newChromosomes.get(i));
        }

        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i - boundIndex);
            FitnessEvaluator evaluator = new FitnessEvaluator() {
                @Override
                public double evaluate(Chromosome chromosome) {
                    return chromosome.getFitness();
                }
            };
            assertTrue(evaluator.evaluate(oldChromosome) >= evaluator.evaluate(newChromosome));
        }
    }

    /**
     * Test init population without elitism.
     */
    @Test
    public void testInitPopulationWithoutElitism() {
        List<Chromosome> chromosomes = createChromosomes();
        int populationLimit = 10;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, 0.0);

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        List<Chromosome> newChromosomes = population.nextGeneration().getChromosomes();
        assertEquals(populationLimit, newChromosomes.size());

        for (int i = 0; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i);
            FitnessEvaluator evaluator = new FitnessEvaluator() {
                @Override
                public double evaluate(Chromosome chromosome) {
                    return chromosome.getFitness();
                }
            };
            assertTrue(evaluator.evaluate(oldChromosome) >= evaluator.evaluate(newChromosome));
        }
    }

    /**
     * Test setting elitism rate out of range.
     */
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }

    /**
     * Creates list of chromosomes.
     *
     * @return list of chromosomes
     */
    private List<Chromosome> createChromosomes() {
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };

        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 2.0;
            }
        };

        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 3.0;
            }
        };

        Chromosome chromosome4 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 4.0;
            }
        };

        Chromosome chromosome5 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 5.0;
            }
        };

        List<Chromosome> chromosomes = List.of(chromosome1, chromosome2, chromosome3, chromosome4, chromosome5);
        return chromosomes;
    }
}