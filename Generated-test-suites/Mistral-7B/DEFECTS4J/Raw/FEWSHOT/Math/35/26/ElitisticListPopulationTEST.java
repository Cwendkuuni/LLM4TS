package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases for ElitisticListPopulation class.
 */
public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithInitialListAndLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getFitness() {
                return 10;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getFitness() {
                return 5;
            }
        };
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 0.5);

        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationWithInitialListOnly() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getFitness() {
                return 10;
            }
        };
        initialChromosomes.add(chromosome1);

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5);

        assertEquals(5, population.getPopulationLimit());
        assertNull(population.getElitismRate());
    }

    @Test
    public void testElitisticListPopulationWithLimitOnly() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.5);

        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, -0.5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 1.5);
    }

    @Test
    public void testNextGenerationWithInitialList() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getFitness() {
                return 10;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenes() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getFitness() {
                return 5;
            }
        };
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ElitisticListPopulation population = new ElitisticListPopulation(initialChromosomes, 5, 0.5);

        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);

        int boundIndex = (int) Math.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size());

        ElitisticListPopulation nextGeneration = population.nextGeneration();

        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        assertEquals(5, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.001);

        assertEquals(2, newChromosomes.size());
        assertTrue(newChromosomes.contains(chromosome1));
        assertTrue(newChromosomes.contains(chromosome2));
        assertFalse(newChromosomes.containsAll(oldChromosomes.subList(boundIndex, oldChromosomes.size())));
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);

        assertEquals(0.5, population.getElitismRate(), 0.001);

        population.setElitismRate(0.6);

        assertEquals(0.6, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);

        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 0.5);

        population.setElitismRate(1.5);
    }

}