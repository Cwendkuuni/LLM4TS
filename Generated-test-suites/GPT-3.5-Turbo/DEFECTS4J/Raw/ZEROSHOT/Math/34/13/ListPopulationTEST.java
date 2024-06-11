import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {
    private ListPopulation listPopulation;

    @Before
    public void setup() {
        listPopulation = new ListPopulation(10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_ThrowsNotPositiveException() {
        ListPopulation negativePopulation = new ListPopulation(-1);
    }

    @Test
    public void testConstructor_CreatesEmptyPopulation() {
        ListPopulation emptyPopulation = new ListPopulation(0);
        assertEquals(0, emptyPopulation.getPopulationSize());
    }

    @Test
    public void testConstructor_CreatesPopulationWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation population = new ListPopulation(chromosomes, 10);

        assertEquals(3, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_ThrowsNullArgumentException() {
        ListPopulation population = new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_ThrowsNumberIsTooLargeException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation population = new ListPopulation(chromosomes, 2);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);

        assertEquals(3, listPopulation.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomes_ThrowsNullArgumentException() {
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_ThrowsNumberIsTooLargeException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        listPopulation.setPopulationLimit(2);
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);

        assertEquals(3, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_ThrowsNumberIsTooLargeException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        listPopulation.setPopulationLimit(2);
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);

        List<Chromosome> returnedChromosomes = listPopulation.getChromosomes();

        assertEquals(chromosomes, returnedChromosomes);
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);

        List<Chromosome> returnedChromosomes = listPopulation.getChromosomeList();

        assertEquals(chromosomes, returnedChromosomes);
    }

    @Test
    public void testAddChromosome() {
        listPopulation.addChromosome(new Chromosome());

        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_ThrowsNumberIsTooLargeException() {
        listPopulation.setPopulationLimit(1);
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittest1 = new Chromosome();
        Chromosome fittest2 = new Chromosome();
        fittest2.setFitness(10);
        Chromosome fittest3 = new Chromosome();
        fittest3.setFitness(20);

        listPopulation.addChromosome(fittest1);
        listPopulation.addChromosome(fittest2);
        listPopulation.addChromosome(fittest3);

        Chromosome fittest = listPopulation.getFittestChromosome();

        assertEquals(fittest3, fittest);
    }

    @Test
    public void testGetPopulationLimit() {
        int limit = listPopulation.getPopulationLimit();

        assertEquals(10, limit);
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(20);

        int limit = listPopulation.getPopulationLimit();

        assertEquals(20, limit);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_ThrowsNotPositiveException() {
        listPopulation.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_ThrowsNumberIsTooSmallException() {
        listPopulation.setChromosome(new Chromosome());
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        int size = listPopulation.getPopulationSize();

        assertEquals(0, size);
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);

        String populationString = listPopulation.toString();
        String expectedString = "[chromosome1, chromosome2, chromosome3]";

        assertEquals(expectedString, populationString);
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);

        Iterator<Chromosome> iterator = listPopulation.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(chromosome1, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(chromosome2, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(chromosome3, iterator.next());

        assertFalse(iterator.hasNext());
    }
}