import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListPopulationTest {
    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(5);
    }

    @Test
    public void testConstructor() {
        assertEquals(5, population.getPopulationLimit());

        List<Chromosome> chromosomes = population.getChromosomes();
        assertNotNull(chromosomes);
        assertTrue(chromosomes.isEmpty());
    }

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population = new ListPopulation(chromosomes, 5);

        assertEquals(5, population.getPopulationLimit());

        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals(3, populationChromosomes.size());
        assertTrue(populationChromosomes.containsAll(chromosomes));
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        population = new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNonPositivePopulationLimit() {
        population = new ListPopulation(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population = new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.setChromosomes(chromosomes);

        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals(3, populationChromosomes.size());
        assertTrue(populationChromosomes.containsAll(chromosomes));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetNullChromosomes() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        population.addChromosome(chromosome1);
        assertEquals(1, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome1));

        population.addChromosome(chromosome2);
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        Chromosome chromosome4 = new Chromosome();
        Chromosome chromosome5 = new Chromosome();
        Chromosome chromosome6 = new Chromosome();

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        population.addChromosome(chromosome4);
        population.addChromosome(chromosome5);
        population.addChromosome(chromosome6);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        Chromosome chromosome3 = new Chromosome(3);

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        Chromosome fittestChromosome = population.getFittestChromosome();
        assertEquals(3, fittestChromosome.getFitness(), 0);
        assertFalse(population.getChromosomes().contains(fittestChromosome));
    }

    @Test
    public void testGetFittestChromosomeWhenEmpty() {
        assertNull(population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());

        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);

        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = population.getChromosomes();

        assertNotNull(chromosomes);
        assertTrue(chromosomes.isEmpty());

        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        chromosomes = population.getChromosomes();

        assertEquals(2, chromosomes.size());
        assertTrue(chromosomes.contains(chromosome1));
        assertTrue(chromosomes.contains(chromosome2));
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(5, population.getPopulationLimit());

        population.setPopulationLimit(10);

        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetNonPositivePopulationLimit() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        population.setPopulationLimit(1);
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        Chromosome chromosome3 = new Chromosome(3);

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        String populationString = population.toString();
        assertEquals("[1, 2, 3]", populationString);
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        List<Chromosome> chromosomes = new ArrayList<>();
        for (Chromosome chromosome : population) {
            chromosomes.add(chromosome);
        }

        assertEquals(3, chromosomes.size());
        assertTrue(chromosomes.contains(chromosome1));
        assertTrue(chromosomes.contains(chromosome2));
        assertTrue(chromosomes.contains(chromosome3));
    }
}