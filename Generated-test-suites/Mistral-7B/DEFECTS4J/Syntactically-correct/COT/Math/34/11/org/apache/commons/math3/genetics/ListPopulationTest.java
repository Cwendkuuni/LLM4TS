package org.apache.commons.math3.genetics.test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ListPopulation
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid population limit
     */
    @Test
    public void testListPopulationConstructorValidLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    /**
     * Test constructor with invalid population limit
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorInvalidLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test setChromosomes method with valid list of chromosomes and valid population limit
     */
    @Test
    public void testSetChromosomesValidListAndLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Test setChromosomes method with null list of chromosomes
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    /**
     * Test setChromosomes method with invalid population limit
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesInvalidLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
    }

    /**
     * Test addChromosomes method with valid collection of chromosomes
     */
    @Test
    public void testAddChromosomesValidCollection() {
        ListPopulation population = new ListPopulation(5);
        Collection<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        population.addChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Test addChromosomes method with invalid collection of chromosomes
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesInvalidCollection() {
        ListPopulation population = new ListPopulation(5);
        Collection<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        chromosomes.add(new Chromosome() {});
        population.addChromosomes(chromosomes);
    }

    /**
     * Test getChromosomes method
     */
    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
        List<Chromosome> result = population.getChromosomes();
        assertEquals(chromosomes, result);
    }

    /**
     * Test getChromosomeList method
     */
    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome() {});
        population.setChromosomes(chromosomes);
        List<Chromosome> result = population.getChromosomeList();
        assertEquals(chromosomes, result);
    }

    /**
     * Test addChromosome method with valid chromosome
     */
    @Test
    public void testAddChromosomeValidChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome() {};
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosome(0));
    }

    /**
     * Test addChromosome method with invalid chromosome
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddChromosomeInvalidChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(null);
    }

    /**
     * Test addChromosome method with population limit reached
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomePopulationLimitReached() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome() {};
        for (int i = 0; i < population.getPopulationLimit(); i++) {
            population.addChromosome(chromosome);
        }
    }
}