import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ListPopulation.
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid population limit.
     */
    @Test
    public void testListPopulationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test constructor with negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    /**
     * Test constructor with list of chromosomes and valid population limit.
     */
    @Test
    public void testListPopulationWithListOfChromosomesAndValidPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Test constructor with list of chromosomes and insufficient population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithListOfChromosomesAndInsufficientPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        chromosomes.add(new Chromosome(2.0));
        ListPopulation population = new ListPopulation(chromosomes, 1);
    }

    /**
     * Test constructor with null list of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullListOfChromosomes() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    /**
     * Test method setChromosomes with valid list of chromosomes.
     */
    @Test
    public void testSetChromosomesWithValidListOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Test method setChromosomes with insufficient population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithInsufficientPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        chromosomes.add(new Chromosome(2.0));
        ListPopulation population = new ListPopulation(Collections.emptyList(), 1);
        population.setChromosomes(chromosomes);
    }

    /**
     * Test method addChromosomes with valid collection of chromosomes.
     */
    @Test
    public void testAddChromosomesWithValidCollectionOfChromosomes() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        population.addChromosomes(chromosomes);
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Test method addChromosomes with insufficient population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithInsufficientPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        chromosomes.add(new Chromosome(2.0));
        population.addChromosomes(chromosomes);
    }

    /**
     * Test method getChromosomes.
     */
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(chromosomes, 2);
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    /**
     * Test method getChromosomeList.
     */
    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(chromosomes, 2);
        Assert.assertEquals(chromosomes, population.getChromosomeList());
    }

    /**
     * Test method addChromosome with valid chromosome.
     */
    @Test
    public void testAddChromosomeWithValidChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Chromosome chromosome = new Chromosome(1.0);
        population.addChromosome(chromosome);
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertEquals(chromosome, population.getChromosomes().get(0));
    }

    /**
     * Test method addChromosome with insufficient population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithInsufficientPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 1);
        Chromosome chromosome = new Chromosome(1.0);
        population.addChromosome(chromosome);
    }

    /**
     * Test method getFittestChromosome.
     */
    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome bestChromosome = new Chromosome(10.0);
        chromosomes.add(new Chromosome(5.0));
        chromosomes.add(bestChromosome);
        ListPopulation population = new ListPopulation(chromosomes, 3);
        Assert.assertEquals(bestChromosome, population.getFittestChromosome());
    }

    /**
     * Test method getPopulationLimit.
     */
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test method setPopulationLimit with valid population limit.
     */
    @Test
    public void testSetPopulationLimitWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test method setPopulationLimit with insufficient population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInsufficientPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 5);
        population.setPopulationLimit(0);
    }

    /**
     * Test method setPopulationLimit with current population size.
     */
    @Test
    public void testSetPopulationLimitWithCurrentPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(chromosomes, 5);
        population.setPopulationLimit(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test method setPopulationLimit with larger population size.
     */
    @Test
    public void testSetPopulationLimitWithLargerPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(chromosomes, 5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test method getPopulationSize.
     */
    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(1, population.getPopulationSize());
    }

    /**
     * Test method toString.
     */
    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals("[Chromosome[fitness=0.0, genes=[]], Chromosome[fitness=0.0, genes=[]]]", population.toString());
    }

    /**
     * Test method iterator.
     */
    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(new Chromosome(1.0));
        Assert.assertArrayEquals(expectedChromosomes.toArray(), population.iterator().toArray());
    }
}