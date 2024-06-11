import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private List<Chromosome> chromosomes;
    private ListPopulation population;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i + 1));
        }
        population = new ListPopulation(chromosomes, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testPopulationLimitNotPositive() {
        new ListPopulation(-1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListOfChromosomesBiggerThanPopulationSize() {
        List<Chromosome> biggerList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            biggerList.add(new Chromosome(i + 1));
        }
        new ListPopulation(biggerList, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedsPopulationLimit() {
        List<Chromosome> additionalChromosomes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            additionalChromosomes.add(new Chromosome(i + 11));
        }
        population.addChromosomes(additionalChromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> returnedChromosomes = population.getChromosomes();
        assertArrayEquals(chromosomes.toArray(), returnedChromosomes.toArray());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> returnedChromosomes = population.getChromosomeList();
        assertArrayEquals(chromosomes.toArray(), returnedChromosomes.toArray());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedsPopulationLimit() {
        population.addChromosome(new Chromosome(11));
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(chromosomes.get(9), fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = population.getPopulationLimit();
        assertEquals(10, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrent() {
        population.setPopulationLimit(9);
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(20);
        int populationLimit = population.getPopulationLimit();
        assertEquals(20, populationLimit);
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = population.getPopulationSize();
        assertEquals(chromosomes.size(), populationSize);
    }

    @Test
    public void testIterator() {
        List<Chromosome> returnedChromosomes = new ArrayList<>();
        for (Chromosome chromosome : population) {
            returnedChromosomes.add(chromosome);
        }
        assertArrayEquals(chromosomes.toArray(), returnedChromosomes.toArray());
    }

}