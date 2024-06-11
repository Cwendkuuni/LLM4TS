package org.apache.commons.math3.genetics;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        int populationLimit = 10;
        ListPopulation population = new TestListPopulation(populationLimit);

        Assert.assertEquals(populationLimit, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testSetChromosomes() {
        int populationLimit = 10;
        ListPopulation population = new TestListPopulation(populationLimit);

        List<Chromosome> chromosomes = new ArrayList<Chromosome>();

        Chromosome chromosome1 = new TestChromosome(1);
        Chromosome chromosome2 = new TestChromosome(2);

        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population.setChromosomes(chromosomes);

        Assert.assertEquals(chromosomes, population.getChromosomes());
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedsPopulationLimit() {
        int populationLimit = 1;
        ListPopulation population = new TestListPopulation(populationLimit);

        List<Chromosome> chromosomes = new ArrayList<Chromosome>();

        Chromosome chromosome1 = new TestChromosome(1);
        Chromosome chromosome2 = new TestChromosome(2);

        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        int populationLimit = 3;
        ListPopulation population = new TestListPopulation(populationLimit);

        List<Chromosome> chromosomeColl = new ArrayList<Chromosome>();

        Chromosome chromosome1 = new TestChromosome(1);
        Chromosome chromosome2 = new TestChromosome(2);
        Chromosome chromosome3 = new TestChromosome(3);

        chromosomeColl.add(chromosome1);
        chromosomeColl.add(chromosome2);
        chromosomeColl.add(chromosome3);

        population.addChromosomes(chromosomeColl);

        Assert.assertEquals(chromosomeColl, population.getChromosomes());
        Assert.assertEquals(populationLimit, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedsPopulationLimit() {
        int populationLimit = 2;
        ListPopulation population = new TestListPopulation(populationLimit);

        List<Chromosome> chromosomeColl = new ArrayList<Chromosome>();

        Chromosome chromosome1 = new TestChromosome(1);
        Chromosome chromosome2 = new TestChromosome(2);
        Chromosome chromosome3 = new TestChromosome(3);

        chromosomeColl.add(chromosome1);
        chromosomeColl.add(chromosome2);
        chromosomeColl.add(chromosome3);

        population.addChromosomes(chromosomeColl);
    }

    @Test
    public void testGetChromosomes() {
        int populationLimit = 3;
        ListPopulation population = new TestListPopulation(populationLimit);

        List<Chromosome> chromosomes = new ArrayList<Chromosome>();

        Chromosome chromosome1 = new TestChromosome(1);
        Chromosome chromosome2 = new TestChromosome(2);
        Chromosome chromosome3 = new TestChromosome(3);

        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);

        population.setChromosomes(chromosomes);

        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosome() {
        int populationLimit = 2;
        ListPopulation population = new TestListPopulation(populationLimit);

        Chromosome chromosome1 = new TestChromosome(1);
        Chromosome chromosome2 = new TestChromosome(2);

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Assert.assertEquals(Arrays.asList(chromosome1, chromosome2), population.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedsPopulationLimit() {
        int populationLimit = 1;
        ListPopulation population = new TestListPopulation(populationLimit);

        Chromosome chromosome1 = new TestChromosome(1);
        Chromosome chromosome2 = new TestChromosome(2);

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
    }

    @Test
    public void testGetFittestChromosome() {
        int populationLimit = 3;
        ListPopulation population = new TestListPopulation(populationLimit);

        Chromosome chromosome1 = new TestChromosome(1);
        Chromosome chromosome2 = new TestChromosome(2);
        Chromosome chromosome3 = new TestChromosome(3);

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        Assert.assertEquals(chromosome3, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 10;
        ListPopulation population = new TestListPopulation(populationLimit);

        Assert.assertEquals(populationLimit, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        int populationLimit = 10;
        ListPopulation population = new TestListPopulation(populationLimit);

        population.setPopulationLimit(20);

        Assert.assertEquals(20, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        int populationLimit = 10;
        ListPopulation population = new TestListPopulation(populationLimit);

        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        int populationLimit = 10;
        ListPopulation population = new TestListPopulation(populationLimit);

        population.setChromosomes(Arrays.asList(new TestChromosome(1), new TestChromosome(2), new TestChromosome(3)));

        population.setPopulationLimit(2);
    }

    @Test
    public void testGetPopulationSize() {
        int populationLimit = 10;
        ListPopulation population = new TestListPopulation(populationLimit);

        population.setChromosomes(Arrays.asList(new TestChromosome(1), new TestChromosome(2), new TestChromosome(3)));

        Assert.assertEquals(3, population.getPopulationSize());
    }

    private class TestChromosome implements Chromosome {
        private int value;

        public TestChromosome(int value) {
            this.value = value;
        }

        public int getFitness() {
            return 0;
        }

        public void setFitness(int fitness) {

        }

        public void updateFitness() {

        }

        public int compareTo(Chromosome o) {
            return 0;
        }

        public List<Chromosome> crossover(Chromosome chromosome) {
            return null;
        }

        public void mutate() {

        }

        public Chromosome clone() {
            return null;
        }
    }

    private class TestListPopulation extends ListPopulation {
        public TestListPopulation(int populationLimit) {
            super(populationLimit);
        }
    }

}