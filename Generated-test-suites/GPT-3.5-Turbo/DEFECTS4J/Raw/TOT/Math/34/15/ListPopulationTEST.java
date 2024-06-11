import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testConstructor_withPopulationLimitAboveZero_shouldSetPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_withPopulationLimitZero_shouldThrowNotPositiveException() {
        new ListPopulation(0);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_withPopulationLimitNegative_shouldThrowNotPositiveException() {
        new ListPopulation(-1);
    }

    @Test
    public void testAddChromosomes_withValidChromosomes_shouldAddChromosomesToPopulation() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());

        population.addChromosomes(newChromosomes);

        assertEquals(4, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_withExceededPopulationLimit_shouldThrowNumberIsTooLargeException() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());

        population.addChromosomes(newChromosomes);
    }

    @Test
    public void testSetChromosomes_withValidChromosomes_shouldClearAndAddNewChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());

        population.setChromosomes(newChromosomes);

        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(newChromosomes));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_withExceededPopulationLimit_shouldThrowNumberIsTooLargeException() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());

        population.setChromosomes(newChromosomes);
    }

    @Test
    public void testAddChromosome_withValidChromosome_shouldAddChromosomeToPopulation() {
        Chromosome newChromosome = new Chromosome();

        population.addChromosome(newChromosome);

        assertEquals(3, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(newChromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_withExceededPopulationLimit_shouldThrowNumberIsTooLargeException() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome_withMultipleChromosomes_shouldReturnFittestChromosome() {
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertTrue(population.getChromosomes().contains(fittestChromosome));
    }

    @Test
    public void testGetPopulationLimit_withValidPopulationLimit_shouldReturnPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_withZero_shouldThrowNotPositiveException() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_withNegative_shouldThrowNotPositiveException() {
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_withSmallerThanCurrentPopulationSize_shouldThrowNumberIsTooSmallException() {
        population.setPopulationLimit(2);
    }

    @Test
    public void testGetPopulationSize_withValidPopulation_shouldReturnPopulationSize() {
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString_shouldReturnStringRepresentationOfPopulation() {
        String expected = "[Chromosome, Chromosome]";
        assertEquals(expected, population.toString());
    }

    @Test
    public void testIterator_withValidPopulation_shouldReturnIteratorOverPopulation() {
        Collection<Chromosome> expected = Arrays.asList(new Chromosome(), new Chromosome());
        assertTrue(population.iterator().hasNext());
        assertTrue(expected.contains(population.iterator().next()));
    }

}