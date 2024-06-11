package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(0, lp.getPopulationSize());

        ListPopulation lp2 = new ListPopulation(100);
        assertEquals(0, lp2.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNonPositive() {
        new ListPopulation(0);
    }

    @Test
    public void testAddChromosome() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome();
        lp.addChromosome(c1);
        assertEquals(1, lp.getPopulationSize());

        Chromosome c2 = new Chromosome();
        lp.addChromosome(c2);
        assertEquals(2, lp.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedLimit() {
        ListPopulation lp = new ListPopulation(1);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome();
        lp.addChromosome(c1);
        assertEquals(1, lp.getPopulationSize());

        Chromosome c2 = new Chromosome();
        lp.addChromosome(c2); // Exceeds population limit
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();

        lp.addChromosomes(Arrays.asList(c1, c2, c3));
        assertEquals(3, lp.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedLimit() {
        ListPopulation lp = new ListPopulation(2);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();

        lp.addChromosomes(Arrays.asList(c1, c2, c3)); // Exceeds population limit
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();

        lp.setChromosomes(Arrays.asList(c1, c2));
        assertEquals(2, lp.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedLimit() {
        ListPopulation lp = new ListPopulation(1);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();

        lp.setChromosomes(Arrays.asList(c1, c2)); // Exceeds population limit
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        lp.addChromosome(c1);
        lp.addChromosome(c2);

        List<Chromosome> chromosomes = lp.getChromosomes();
        assertEquals(2, chromosomes.size());
        assertTrue(chromosomes.contains(c1));
        assertTrue(chromosomes.contains(c2));
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        lp.addChromosome(c1);
        lp.addChromosome(c2);

        List<Chromosome> chromosomes = lp.getChromosomeList();
        assertEquals(2, chromosomes.size());
        assertTrue(chromosomes.contains(c1));
        assertTrue(chromosomes.contains(c2));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(0, lp.getPopulationSize());

        Chromosome c1 = new Chromosome(1);
        Chromosome c2 = new Chromosome(2);
        Chromosome c3 = new Chromosome(3);
        lp.addChromosome(c1);
        lp.addChromosome(c2);
        lp.addChromosome(c3);

        Chromosome fittest = lp.getFittestChromosome();
        assertEquals(3, fittest.getFitness());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(10, lp.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNonPositive() {
        ListPopulation lp = new ListPopulation(10);
        lp.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmaller() {
        ListPopulation lp = new ListPopulation(10);
        lp.addChromosome(new Chromosome());
        lp.setPopulationLimit(5); // Less than current population size
    }

    @Test
    public void testSetPopulationLimitLarger() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(10, lp.getPopulationLimit());

        lp.setPopulationLimit(20);
        assertEquals(20, lp.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation lp = new ListPopulation(10);
        assertEquals(0, lp.getPopulationSize());

        lp.addChromosome(new Chromosome());
        lp.addChromosome(new Chromosome());
        assertEquals(2, lp.getPopulationSize());
    }
}