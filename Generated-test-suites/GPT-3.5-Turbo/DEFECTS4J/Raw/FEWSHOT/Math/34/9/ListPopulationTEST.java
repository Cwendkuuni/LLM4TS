import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.exception.NotPositiveException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    // Test the constructor with the population limit being a positive number
    @Test
    public void testConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    // Test the constructor with the population limit as 0
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithZeroPopulationLimit() {
        new ListPopulation(0);
    }

    // Test the constructor with the population limit as a negative number
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(-10);
    }

    // Test the second constructor with the given chromosomes and population limit being a positive number
    @Test
    public void testConstructorWithChromosomesAndPositivePopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(chromosomes, population.getChromosomes());
    }

    // Test the second constructor with the given chromosomes as null
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }

    // Test the second constructor with the given chromosomes and population limit as 0
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithChromosomesAndZeroPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 0);
    }

    // Test the second constructor with the given chromosomes and population limit as a negative number
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithChromosomesAndNegativePopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, -10);
    }

    // Test the second constructor with the given chromosomes exceeding the population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 1);
    }

    // Test the set chromosomes method with null chromosomes
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    // Test the set chromosomes method with chromosomes exceeding population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    // Test the add chromosomes method with chromosomes exceeding population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(2);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
    }

    // Test the add chromosome method when chromosome is added successfully
    @Test
    public void testAddChromosomeSuccess() {
        ListPopulation population = new ListPopulation(2);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    // Test the add chromosome method when population size exceeds population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    // Test the get fittest chromosome method when no chromosomes exist
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFittestChromosomeNoChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.getFittestChromosome();
    }

    // Test the get fittest chromosome method when one chromosome exists
    @Test
    public void testGetFittestChromosomeOneChromosome() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(chromosome, population.getFittestChromosome());
    }

    // Test the get fittest chromosome method when multiple chromosomes exist
    @Test
    public void testGetFittestChromosomeMultipleChromosomes() {
        ListPopulation population = new ListPopulation(10);
        Chromosome fittestChromosome = new Chromosome();
        Chromosome unfitChromosome = new Chromosome();
        population.addChromosome(fittestChromosome);
        population.addChromosome(unfitChromosome);
        assertEquals(fittestChromosome, population.getFittestChromosome());
    }

    // Test the get population limit method
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    // Test the set population limit method with population limit as a positive number
    @Test
    public void testSetPopulationLimitWithPositiveNumber() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    // Test the set population limit method with population limit as 0
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZero() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(0);
    }

    // Test the set population limit method with population limit as a negative number
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeNumber() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-10);
    }

    // Test the set population limit method with population limit smaller than the number of chromosomes
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(1);
    }

    // Test the get population size method when no chromosomes exist
    @Test
    public void testGetPopulationSizeNoChromosomes() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
    }

    // Test the get population size method when one chromosome exists
    @Test
    public void testGetPopulationSizeOneChromosome() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
    }

    // Test the get population size method when multiple chromosomes exist
    @Test
    public void testGetPopulationSizeMultipleChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        assertEquals(2, population.getPopulationSize());
    }

    // Test the toString method
    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        assertEquals("[Chromosome[]]", population.toString());
    }

    // Test the iterator method
    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(chromosome, population.iterator().next());
    }
}