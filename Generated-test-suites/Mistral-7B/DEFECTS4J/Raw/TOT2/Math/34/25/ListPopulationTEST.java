
package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorPositive() {
        ListPopulation population = new ListPopulation(5);
        assertTrue(population.getPopulationSize() == 5);
    }

    // Negative population limit
    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorNegative() {
        new ListPopulation(-5);
    }

    // Population limit equal to current size of the population
    @Test
    public void testListPopulationConstructorSameSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertTrue(population.getPopulationSize() == 5);
    }

    // Population limit bigger than the current size of the population + new chromosomes
    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorBiggerSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(chromosomes, 5);
        population.addChromosomes(new ArrayList<>(5));
        population.setPopulationLimit(population.getPopulationSize() + 1);
        population.addChromosome(new Chromosome());
    }

    // Null list of chromosomes
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setChromosomes(null);
    }

    // List of chromosomes bigger than the population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesBiggerList() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.addAll(new ArrayList<>(6));
        population.setChromosomes(chromosomes);
    }

    // Null collection of chromosomes
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNullCollection() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(null);
    }

    // Collection of chromosomes bigger than the population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesBiggerCollection() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Collection<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.addAll(new ArrayList<>(11));
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(population.getPopulationSize(), chromosomes.size());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Collection<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(population.getPopulationSize(), chromosomes.size() + population.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertTrue(population.getPopulationSize() == 6);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        assertTrue(population.getPopulationSize() == 5); // population limit
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        assertEquals(population.getPopulationLimit(), 5);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> result = population.getChromosomes();
        assertEquals(chromosomes, result);
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> result = population.getChromosomeList();
        assertEquals(chromosomes, result);
    }

    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        String expected = chromosomes.toString();
        String result = population.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        Chromosome result = iterator.next();
        Chromosome expected = chromosomes.get(0);
        assertEquals(expected, result);
        iterator.remove(); // should throw an exception
    }
}
