import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import static org.junit.Assert.*;

public class ListPopulationTest {
    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
    }

    @Test
    public void testConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation listPopulation2 = new ListPopulation(chromosomes, 5);

        assertEquals(chromosomes, listPopulation2.getChromosomes());
        assertEquals(5, listPopulation2.getPopulationLimit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidPopulationLimit() {
        new ListPopulation(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorChromosomesExceedsPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());

        new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation.setChromosomes(chromosomes);

        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetChromosomesNull() {
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedsPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());

        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation.addChromosomes(chromosomes);

        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedsPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());

        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new Chromosome();
        chromosomes.add(chromosome);

        listPopulation.setChromosomes(chromosomes);

        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = new Chromosome();
        chromosomes.add(chromosome);

        listPopulation.setChromosomes(chromosomes);

        assertEquals(chromosomes, listPopulation.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);

        assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedsPopulationLimit() {
        for (int i = 0; i < 6; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(5);
        Chromosome chromosome2 = new Chromosome(7);
        Chromosome chromosome3 = new Chromosome(3);
        Chromosome chromosome4 = new Chromosome(10);

        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);
        listPopulation.addChromosome(chromosome4);

        assertEquals(chromosome4, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(10);

        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalid() {
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        for (int i = 0; i < 5; i++) {
            listPopulation.addChromosome(new Chromosome());
        }

        listPopulation.setPopulationLimit(3);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());

        for (int i = 0; i < 5; i++) {
            listPopulation.addChromosome(new Chromosome());
        }

        assertEquals(5, listPopulation.getPopulationSize());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        listPopulation.setChromosomes(chromosomes);

        Iterator<Chromosome> iterator = listPopulation.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(chromosome1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(chromosome2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}