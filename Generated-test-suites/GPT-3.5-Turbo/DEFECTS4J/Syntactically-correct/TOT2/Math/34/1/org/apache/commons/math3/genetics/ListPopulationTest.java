import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ListPopulationTest {

    @Test
    public void testListPopulationPopulationLimitConstructor() {
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testListPopulationPopulationLimitChromosomesConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        listPopulation.setChromosomes(chromosomes);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        listPopulation.addChromosomes(chromosomes);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = mock(Chromosome.class);
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(1);
        listPopulation.addChromosome(mock(Chromosome.class));
        listPopulation.addChromosome(mock(Chromosome.class));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome1 = mock(Chromosome.class);
        Chromosome chromosome2 = mock(Chromosome.class);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosome1, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(20);
        assertEquals(20, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        ListPopulation listPopulation = new ListPopulation(2);
        listPopulation.addChromosome(mock(Chromosome.class));
        listPopulation.setPopulationLimit(1);
    }
}