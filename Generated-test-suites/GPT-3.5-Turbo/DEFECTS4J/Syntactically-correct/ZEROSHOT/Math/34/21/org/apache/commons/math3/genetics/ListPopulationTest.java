package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void initialize() {
        population = new ListPopulation(10);
    }

    @Test
    public void testConstructor_ValidPopulationLimit() {
        Assert.assertEquals(0, population.getPopulationSize());
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0, population.getChromosomes().size());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_InvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(0);
    }

    @Test
    public void testConstructor_WithChromosomes_ValidPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(2, population.getChromosomes().size());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_NullChromosomes() {
        ListPopulation population = new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_WithChromosomes_InvalidPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 0);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(2, population.getChromosomes().size());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomes_NullChromosomes() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_InvalidPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        population.addChromosomes(chromosomeColl);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(2, population.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_InvalidPopulationLimit() {
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        population.addChromosomes(chromosomeColl);
    }

    @Test
    public void testGetChromosomes_EmptyPopulation() {
        List<Chromosome> chromosomes = population.getChromosomes();
        Assert.assertEquals(0, chromosomes.size());
    }

    @Test
    public void testGetChromosomes_PopulatedPopulation() {
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        population.addChromosomes(chromosomeColl);
        List<Chromosome> chromosomes = population.getChromosomes();
        Assert.assertEquals(2, chromosomes.size());
    }

    @Test
    public void testGetChromosomeList() {
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        population.addChromosomes(chromosomeColl);
        List<Chromosome> chromosomes = population.getChromosomeList();
        Assert.assertEquals(2, chromosomes.size());
    }

    @Test
    public void testAddChromosome_ValidPopulationLimit() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(2, population.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_InvalidPopulationLimit() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome(5));
        chromosomeColl.add(new Chromosome(3));
        chromosomeColl.add(new Chromosome(7));
        population.addChromosomes(chromosomeColl);
        Chromosome fittestChromosome = population.getFittestChromosome();
        Assert.assertEquals(7, fittestChromosome.getFitness());
    }

    @Test
    public void testGetPopulationLimit() {
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit_ValidLimit() {
        population.setPopulationLimit(20);
        Assert.assertEquals(20, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_InvalidLimit() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_SmallerThanCurrentSize() {
        population.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        population.addChromosomes(chromosomeColl);
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        String populationString = population.toString();
        Assert.assertEquals("[]", populationString);
    }

    @Test
    public void testIterator() {
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        population.addChromosomes(chromosomeColl);
        Iterator<Chromosome> iterator = population.iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    private class Chromosome implements Comparable<Chromosome> {

        private int fitness;

        public Chromosome() {
            this.fitness = 0;
        }

        public Chromosome(int fitness) {
            this.fitness = fitness;
        }

        public int getFitness() {
            return fitness;
        }

        @Override
        public int compareTo(Chromosome other) {
            return this.fitness - other.fitness;
        }
    }
}