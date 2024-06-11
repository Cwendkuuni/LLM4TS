package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation<>(5);
    }

    @Test
    public void testListPopulationCreationWithEmptyList() {
        ListPopulation population = new ListPopulation<>(Collections.emptyList(), 5);
        Assert.assertNotNull(population.getChromosomes());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testListPopulationCreationWithPositiveListAndSize() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation population = new ListPopulation<>(chromosomes, 5);
        Assert.assertNotNull(population.getChromosomes());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertArrayEquals(chromosomes.toArray(), population.getChromosomes().toArray());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationCreationWithNegativePopulationLimit() {
        new ListPopulation<>(Collections.emptyList(), -1);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationCreationWithZeroPopulationLimit() {
        new ListPopulation<>(Collections.emptyList(), 0);
    }

    @Test
    public void testListPopulationSetChromosomes() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        ListPopulation population = new ListPopulation<>(initialChromosomes, 5);
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {});
        population.setChromosomes(newChromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertArrayEquals(newChromosomes.toArray(), population.getChromosomes().toArray());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation<>(Collections.emptyList(), 5);
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {}, new Chromosome() {});
        population.setChromosomes(newChromosomes);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation<>(Collections.emptyList(), 5);
        Chromosome chromosome = new Chromosome() {};
        population.addChromosomes(Collections.singletonList(chromosome));
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertArrayEquals(new Object[]{chromosome}, population.getChromosomes().toArray());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation<>(Collections.emptyList(), 5);
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome() {}, new Chromosome() {}, new Chromosome() {});
        population.addChromosomes(newChromosomes);
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation<>(Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 5.0;
            }
        }, new Chromosome() {
            @Override
            public double getFitness() {
                return 10.0;
            }
        }), 5);
        Chromosome fittestChromosome = population.getFittestChromosome();
        Assert.assertEquals(fittestChromosome, population.getChromosomes().get(1));
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation<>(Collections.emptyList(), 5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitWithNegativeValue() {
        ListPopulation population = new ListPopulation<>(Collections.emptyList(), 5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitWithTooSmallValue() {
        ListPopulation population = new ListPopulation<>(Collections.emptyList(), 5);
        population.setPopulationLimit(3);
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation<>(Collections.emptyList(), 5);
        Assert.assertEquals(0, population.getPopulationSize());
        population.addChromosome(new Chromosome() {});
        Assert.assertEquals(1, population.getPopulationSize());
    }
}