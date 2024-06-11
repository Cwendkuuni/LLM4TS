import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("1"));
        chromosomes.add(new Chromosome("2"));
        chromosomes.add(new Chromosome("3"));

        listPopulation = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, population.getPopulationLimit());
        assertEquals(3, population.getPopulationSize());
        assertEquals("1", population.getChromosomes().get(0).getId());
        assertEquals("2", population.getChromosomes().get(1).getId());
        assertEquals("3", population.getChromosomes().get(2).getId());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        new ListPopulation(3);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorExceededChromosomesLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("1"));
        chromosomes.add(new Chromosome("2"));
        chromosomes.add(new Chromosome("3"));
        chromosomes.add(new Chromosome("4"));

        new ListPopulation(chromosomes, 3);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome("4"));
        newChromosomes.add(new Chromosome("5"));

        population.setChromosomes(newChromosomes);

        assertEquals(10, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals("4", population.getChromosomes().get(0).getId());
        assertEquals("5", population.getChromosomes().get(1).getId());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNull() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceededChromosomesLimit() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome("4"));
        newChromosomes.add(new Chromosome("5"));
        newChromosomes.add(new Chromosome("6"));

        population.setChromosomes(newChromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome("4"));
        newChromosomes.add(new Chromosome("5"));

        population.addChromosomes(newChromosomes);

        assertEquals(10, population.getPopulationLimit());
        assertEquals(5, population.getPopulationSize());
        assertEquals("1", population.getChromosomes().get(0).getId());
        assertEquals("2", population.getChromosomes().get(1).getId());
        assertEquals("3", population.getChromosomes().get(2).getId());
        assertEquals("4", population.getChromosomes().get(3).getId());
        assertEquals("5", population.getChromosomes().get(4).getId());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceededChromosomesLimit() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome("4"));
        newChromosomes.add(new Chromosome("5"));
        newChromosomes.add(new Chromosome("6"));

        population.addChromosomes(newChromosomes);
    }

    @Test
    public void testGetChromosomes() {
        assertEquals(3, population.getChromosomes().size());
        assertEquals("1", population.getChromosomes().get(0).getId());
        assertEquals("2", population.getChromosomes().get(1).getId());
        assertEquals("3", population.getChromosomes().get(2).getId());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals("3", fittestChromosome.getId());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanPopulationSize() {
        population.setPopulationLimit(2);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(3, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        String populationString = population.toString();
        assertEquals("[Chromosome{Id='1'}, Chromosome{Id='2'}, Chromosome{Id='3'}]", populationString);
    }

    @Test
    public void testIterator() {
        Iterator<Chromosome> iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("1", iterator.next().getId());
        assertTrue(iterator.hasNext());
        assertEquals("2", iterator.next().getId());
        assertTrue(iterator.hasNext());
        assertEquals("3", iterator.next().getId());
        assertFalse(iterator.hasNext());
    }
}