package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for ListPopulation class
 */
public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(5);
    }

    /**
     * Test constructor with valid population limit
     */
    @Test
    public void testListPopulationConstructorValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertFalse(population.getChromosomes().isEmpty());
    }

    /**
     * Test constructor with invalid population limit (negative number)
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    /**
     * Test addChromosomes method with valid collection of chromosomes and valid population limit
     */
    @Test
    public void testAddChromosomesValidCollectionAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        population.addChromosomes(chromosomes);
        assertEquals(5 + chromosomes.size(), population.getPopulationSize());
    }

    /**
     * Test addChromosomes method with invalid collection of chromosomes and valid population limit
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNullCollection() {
        population.addChromosomes(null);
    }

    /**
     * Test addChromosomes method with valid chromosome and valid population limit
     */
    @Test
    public void testAddChromosomeValidChromosomeAndPopulationLimit() {
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        assertEquals(6, population.getPopulationSize());
    }

    /**
     * Test addChromosomes method with valid chromosome and invalid population limit
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        population.setPopulationLimit(5);
    }

    /**
     * Test getChromosomes method
     */
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        };
        population.addChromosome(chromosome);
        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals(1, populationChromosomes.size());
        assertEquals(chromosome, populationChromosomes.get(0));
    }

    /**
     * Test getFittestChromosome method
     */
    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome bestChromosome = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return 1;
            }
        };
        Chromosome worstChromosome = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return -1;
            }
        };
        chromosomes.add(bestChromosome);
        chromosomes.add(worstChromosome);
        population.setChromosomes(chromosomes);
        Chromosome expectedFittestChromosome = bestChromosome;
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(expectedFittestChromosome, fittestChromosome);
    }

    /**
     * Test setPopulationLimit method with valid population limit
     */
    @Test
    public void testSetPopulationLimitValidPopulationLimit() {
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test setPopulationLimit method with invalid population limit (negative number)
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalidPopulationLimit() {
        population.setPopulationLimit(-1);
    }

    /**
     * Test setPopulationLimit method with population size smaller than new population limit
     */
    @Test
    public void testSetPopulationLimitPopulationSizeSmallerThanNewPopulationLimit() {
        population.setPopulationLimit(10);
        population.addChromosome(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public int compareTo(Chromosome other) {
                return 0;
            }
        });
        assertEquals(6, population.getPopulationSize());
        population.setPopulationLimit(15);
        assertEquals(6, population.getPopulationSize());
    }

    /**
     * Test setPopulationLimit method with population size bigger than new population limit
     */
    @Test
    public void testSetPopulationLimitPopulationSizeBiggerThanNewPopulationLimit() {
        population.setPopulationLimit(5);
        for (int i = 0; i < 10; i++) {
            population.addChromosome(new Chromosome() {
                @Override
                public double[] getGenes() {
                    return new double[0];
                }

                @Override
                public int compareTo(Chromosome other) {
                    return 0;
                }
            });
        }
        assertEquals(10, population.getPopulationSize());
        population.setPopulationLimit(8);
        assertEquals(8, population.getPopulationSize());
    }
}