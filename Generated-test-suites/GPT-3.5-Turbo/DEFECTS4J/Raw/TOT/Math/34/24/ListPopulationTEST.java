import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testConstructor_ValidChromosomesPopulationLimit_ValidPopulation() {
        assertEquals(3, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(3, population.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_ExceedingPopulationLimit_ExceptionThrown() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NonPositivePopulationLimit_ExceptionThrown() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 0);
    }

    @Test
    public void testSetChromosomes_ValidChromosomes_UpdatedChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        population.setChromosomes(newChromosomes);
        assertEquals(3, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(3, population.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_ExceedingPopulationLimit_ExceptionThrown() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        population.setChromosomes(newChromosomes);
    }

    @Test
    public void testAddChromosomes_AddingChromosomes_ChromosomesAdded() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        population.addChromosomes(newChromosomes);
        assertEquals(5, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(5, population.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_ExceedingPopulationLimit_ExceptionThrown() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        population.addChromosomes(newChromosomes);
    }

    @Test
    public void testGetChromosomes_UnmodifiableList_ReturnsUnmodifiableList() {
        List<Chromosome> chromosomes = population.getChromosomes();
        assertNotNull(chromosomes);
        assertNotSame(chromosomes, population.getChromosomes());
        assertEquals(3, chromosomes.size());
    }

    @Test
    public void testGetChromosomeList_ReturnsChromosomeList() {
        List<Chromosome> chromosomeList = population.getChromosomeList();
        assertNotNull(chromosomeList);
        assertSame(chromosomeList, population.getChromosomeList());
        assertEquals(3, chromosomeList.size());
    }

    @Test
    public void testAddChromosome_ValidChromosome_ChromosomeAdded() {
        Chromosome newChromosome = new Chromosome();
        population.addChromosome(newChromosome);
        assertEquals(4, population.getPopulationSize());
        assertEquals(5, population.getPopulationLimit());
        assertNotNull(population.getChromosomes());
        assertEquals(4, population.getChromosomes().size());
        assertTrue(population.getChromosomes().contains(newChromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_ExceedingPopulationLimit_ExceptionThrown() {
        Chromosome newChromosome = new Chromosome();
        population.addChromosome(newChromosome);
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome_ValidChromosomes_ReturnsFittestChromosome() {
        Chromosome fittest = population.getFittestChromosome();
        assertNotNull(fittest);
        assertTrue(population.getChromosomes().contains(fittest));
    }

    @Test
    public void testGetPopulationLimit_ValidPopulationLimit_ReturnsPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_NonPositivePopulationLimit_ExceptionThrown() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_NewLimitSmallerThanCurrentSize_ExceptionThrown() {
        population.setPopulationLimit(2);
    }

    @Test
    public void testGetPopulationSize_ValidPopulationSize_ReturnsPopulationSize() {
        assertEquals(3, population.getPopulationSize());
    }

}