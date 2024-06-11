import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ListPopulationTest {

    @Test
    public void testConstructorWithPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        new ListPopulation(0);
    }

    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimitInChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(10);
        Collection<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        Collection<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        for (int i = 0; i < 11; i++) {
            population.addChromosome(new Chromosome());
        }
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosome1.setFitness(10.0);
        chromosome2.setFitness(20.0);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(10);
        assertEquals("[]", population.toString());

        population.addChromosome(new Chromosome());
        assertEquals("[chromosome]", population.toString());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 10);
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosome1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(chromosome2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}