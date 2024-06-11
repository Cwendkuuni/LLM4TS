package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for ListPopulation class.
 */
public class ListPopulationTest {

    private ListPopulation listPopulation;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        listPopulation = new ListPopulation(chromosomes, 15);
    }

    

    @Test
    public void testListPopulationConstructorWithEmptyList() {
        assertNotNull(new ListPopulation(1));
    }

    @Test
    public void testListPopulationConstructorWithPopulationLimitLessThanOne() {
        assertThrows(NotPositiveException.class, () -> new ListPopulation(-1));
    }

    @Test
    public void testListPopulationConstructorWithPopulationLimitGreaterThanSizeOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome(i));
        }
        assertThrows(NumberIsTooSmallException.class, () -> new ListPopulation(chromosomes, 4));
    }

    @Test
    public void testListPopulationConstructorWithPopulationLimitLessThanSizeOfChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new Chromosome(i));
        }
        ListPopulation listPopulation = new ListPopulation(chromosomes, 12);
        assertEquals(12, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationConstructorWithNullListOfChromosomes() {
        assertThrows(NullArgumentException.class, () -> new ListPopulation((List<Chromosome>) null, 10));
    }

    @Test
    public void testListPopulationSetChromosomesWithNullListOfChromosomes() {
        listPopulation.setChromosomes((List<Chromosome>) null);
        assertNull(listPopulation.getChromosomes());
    }

    @Test
    public void testListPopulationSetChromosomesWithSizeGreaterThanPopulationLimit() {
        listPopulation.setChromosomes(chromosomes);
        listPopulation.setChromosomes(Collections.singletonList(new Chromosome(11)));
        assertEquals(11, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationSetChromosomesWithSizeLessThanPopulationLimitAndAddChromosome() {
        listPopulation.setChromosomes(chromosomes);
        listPopulation.addChromosome(new Chromosome(11));
        assertEquals(11, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationSetChromosomesWithSameSizeAndSameChromosomes() {
        List<Chromosome> chromosomescopy = new ArrayList<>(listPopulation.getChromosomes());
        listPopulation.setChromosomes(listPopulation.getChromosomes());
        assertEquals(listPopulation.getChromosomes(), chromosomescopy);
    }

    @Test
    public void testListPopulationSetChromosomesWithSameSizeButDifferentChromosomes() {
        List<Chromosome> chromosomescopy = new ArrayList<>(listPopulation.getChromosomes());
        Chromosome chromosome = chromosomes.get(0);
        listPopulation.setChromosomes(Collections.singletonList(new Chromosome(11)));
        assertNotEquals(listPopulation.getChromosomes(), chromosomescopy);
    }

    @Test
    public void testListPopulationAddChromosomesWithNullCollection() {
        assertThrows(NullArgumentException.class, () -> listPopulation.addChromosomes((Collection<Chromosome>) null));
    }

    @Test
    public void testListPopulationAddChromosomesWithEmptyCollection() {
        listPopulation.addChromosomes(Collections.emptyList());
        assertEquals(10, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationAddChromosomesWithCollectionSizeLessThanPopulationLimit() {
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(new Chromosome(11));
        listPopulation.addChromosomes(chromosomesToAdd);
        assertEquals(11, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationAddChromosomesWithCollectionSizeGreaterThanPopulationLimit() {
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            chromosomesToAdd.add(new Chromosome(i));
        }
        assertThrows(NumberIsTooLargeException.class, () -> listPopulation.addChromosomes(chromosomesToAdd));
    }

    @Test
    public void testListPopulationGetChromosomes() {
        List<Chromosome> chromosomes = listPopulation.getChromosomes();
        assertEquals(10, chromosomes.size());
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        List<Chromosome> chromosomesList = listPopulation.getChromosomeList();
        assertEquals(10, chromosomesList.size());
    }

    @Test
    public void testListPopulationAddChromosomeWithNullChromosome() {
        assertThrows(NullArgumentException.class, () -> listPopulation.addChromosome(null));
    }

    @Test
    public void testListPopulationAddChromosomeWithChromosomeLessThanPopulationLimit() {
        Chromosome chromosome = new Chromosome(11);
        listPopulation.addChromosome(chromosome);
        assertEquals(11, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationAddChromosomeWithChromosomeGreaterThanPopulationLimit() {
        Chromosome chromosome = new Chromosome(20);
        assertThrows(NumberIsTooLargeException.class, () -> listPopulation.addChromosome(chromosome));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        Chromosome bestChromosome = chromosomes.get(0);
        assertEquals(bestChromosome, fittestChromosome);
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        assertEquals(15, listPopulation.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimitWithPopulationLimitLessThanOne() {
        assertThrows(NotPositiveException.class, () -> listPopulation.setPopulationLimit(-1));
    }

    @Test
    public void testListPopulationSetPopulationLimitWithPopulationLimitGreaterThanCurrentSize() {
        listPopulation.setPopulationLimit(20);
        assertEquals(10, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationSetPopulationLimitWithPopulationLimitLessThanCurrentSize() {
        listPopulation.setPopulationLimit(5);
        assertEquals(5, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationToString() {
        assertEquals("[Chromosome[0], Chromosome[1], Chromosome[2], Chromosome[3], Chromosome[4], Chromosome[5], Chromosome[6], Chromosome[7], Chromosome[8], Chromosome[9]]", listPopulation.toString());
    }

    @Test
    public void testListPopulationIterator() {
        Iterator<Chromosome> iterator = listPopulation.iterator();
        List<Chromosome> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        assertEquals(listPopulation.getChromosomes(), list);
    }

    
}