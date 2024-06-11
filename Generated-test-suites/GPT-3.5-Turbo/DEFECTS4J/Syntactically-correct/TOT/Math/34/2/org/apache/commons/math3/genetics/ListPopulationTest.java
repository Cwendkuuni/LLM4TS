import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private ListPopulation listPopulation;
    private List<Chromosome> chromosomes;
    private final int populationLimit = 10;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testListPopulationCreation() {
        ListPopulation emptyListPopulation = new ListPopulation(populationLimit);
        assertNotNull(emptyListPopulation.getChromosomes());
        assertEquals(emptyListPopulation.getPopulationLimit(), populationLimit);
        assertEquals(emptyListPopulation.getPopulationSize(), 0);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationCreationWithInvalidPopulationLimit() {
        ListPopulation invalidListPopulation = new ListPopulation(-1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationCreationWithExceededChromosomeList() {
        List<Chromosome> exceededChromosomes = new ArrayList<>();
        for (int i = 0; i < populationLimit + 1; i++) {
            exceededChromosomes.add(new Chromosome());
        }
        ListPopulation exceededListPopulation = new ListPopulation(exceededChromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        listPopulation.setChromosomes(newChromosomes);
        assertEquals(listPopulation.getChromosomes(), newChromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithExceededChromosomeList() {
        List<Chromosome> exceededChromosomes = new ArrayList<>();
        for (int i = 0; i < populationLimit + 1; i++) {
            exceededChromosomes.add(new Chromosome());
        }
        listPopulation.setChromosomes(exceededChromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> additionalChromosomes = new ArrayList<>();
        additionalChromosomes.add(new Chromosome());
        listPopulation.addChromosomes(additionalChromosomes);
        assertNotEquals(listPopulation.getChromosomes(), chromosomes);
        assertEquals(listPopulation.getPopulationSize(), chromosomes.size() + additionalChromosomes.size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithExceededChromosomeList() {
        List<Chromosome> exceededChromosomes = new ArrayList<>();
        for (int i = 0; i < populationLimit + 1; i++) {
            exceededChromosomes.add(new Chromosome());
        }
        listPopulation.addChromosomes(exceededChromosomes);
    }

    @Test
    public void testGetChromosomes() {
        assertEquals(listPopulation.getChromosomes(), chromosomes);
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(listPopulation.getPopulationSize(), chromosomes.size() + 1);
        assertEquals(listPopulation.getChromosomes().get(chromosomes.size()), chromosome);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithExceededChromosomeList() {
        for (int i = 0; i < populationLimit; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
        listPopulation.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = new Chromosome();
        chromosomes.add(fittestChromosome);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(listPopulation.getFittestChromosome(), fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(listPopulation.getPopulationLimit(), populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidPopulationLimit() {
        listPopulation.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerChromosomeList() {
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(listPopulation.getPopulationSize(), chromosomes.size());
    }

    @Test
    public void testIterator() {
        assertNotNull(listPopulation.iterator());
    }
}