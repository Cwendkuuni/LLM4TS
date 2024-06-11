package org.example.junit;

import org.apache.commons.math3.genetics.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorNoInitialChromosomes() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationSize());
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorNegativePopulationLimit() {
        try {
            ListPopulation population = new ListPopulation(-5);
        } catch (NotPositiveException e) {
            // Pass, test successful
        }
    }

    @Test
    public void testListPopulationConstructorLargeInitialChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            initialChromosomes.add(new Chromosome());
        }

        ListPopulation population = new ListPopulation(5, initialChromosomes);
        assertEquals(5, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
        assertFalse(population.getChromosomes().containsAll(initialChromosomes));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorNullChromosomes() {
        ListPopulation population = new ListPopulation(5, null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorLargeChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            initialChromosomes.add(new Chromosome());
        }

        try {
            ListPopulation population = new ListPopulation(5, initialChromosomes);
        } catch (NumberIsTooLargeException e) {
            // Pass, test successful
        }
    }

    @Test
    public void testListPopulationSetChromosomesEmptyList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> emptyList = Collections.emptyList();
        population.setChromosomes(emptyList);
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(6, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesLargeCollection() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> largeCollection = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            largeCollection.add(new Chromosome());
        }

        try {
            population.addChromosomes(largeCollection);
        } catch (NumberIsTooLargeException e) {
            // Pass, test successful
        }
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3);
        Chromosome fittestChromosome = new Chromosome();
        population.addChromosome(fittestChromosome);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());

        Chromosome result = population.getFittestChromosome();
        assertEquals(fittestChromosome, result);
    }

    // Test other methods

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        String result = population.toString();
        assertTrue(result.contains("Chromosome"));
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);

        Iterator<Chromosome> iterator = population.iterator();
        Chromosome result = iterator.next();
        assertEquals(chromosome, result);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListPopulationIteratorRemove() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);

        Iterator<Chromosome> iterator = population.iterator();
        iterator.remove();
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(5);
        try {
            population.setPopulationLimit(-5);
        } catch (NotPositiveException e) {
            // Pass, test successful
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitSmallerCurrentSize() {
        ListPopulation population = new ListPopulation(10);
        try {
            population.setPopulationLimit(5);
        } catch (NumberIsTooSmallException e) {
            // Pass, test successful
        }
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationSize());
    }

}