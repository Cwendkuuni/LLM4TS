import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
    }

    @Test
    public void testEmptyListPopulationConstructor() {
        // Verify that an empty list population is created
        assertEquals(0, listPopulation.getPopulationSize());
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testListPopulationConstructor() {
        // Verify that a list population is created with the given chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation = new ListPopulation(chromosomes, 5);

        assertEquals(2, listPopulation.getPopulationSize());
        assertEquals(5, listPopulation.getPopulationLimit());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullChromosomesListPopulationConstructor() {
        // Verify that a null list of chromosomes throws NullArgumentException
        listPopulation = new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativePopulationLimitListPopulationConstructor() {
        // Verify that a negative population limit throws NotPositiveException
        listPopulation = new ListPopulation(5);
        listPopulation.setPopulationLimit(-2);
    }

    @Test(expected = NotPositiveException.class)
    public void testZeroPopulationLimitListPopulationConstructor() {
        // Verify that a zero population limit throws NotPositiveException
        listPopulation = new ListPopulation(5);
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testExceededPopulationLimitListPopulationConstructor() {
        // Verify that a list of chromosomes exceeding the population limit throws NumberIsTooLargeException
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        // Verify that the list of chromosomes is set correctly
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation.setChromosomes(chromosomes);

        assertEquals(2, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullChromosomesSetChromosomes() {
        // Verify that setting a null list of chromosomes throws NullArgumentException
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testExceededPopulationLimitSetChromosomes() {
        // Verify that a list of chromosomes exceeding the population limit throws NumberIsTooLargeException
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        // Verify that chromosomes can be added to the population
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation.addChromosomes(chromosomes);

        assertEquals(2, listPopulation.getPopulationSize());
        assertTrue(listPopulation.getChromosomes().contains(chromosome1));
        assertTrue(listPopulation.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testExceededPopulationLimitAddChromosomes() {
        // Verify that a list of chromosomes exceeding the population limit throws NumberIsTooLargeException
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation.addChromosomes(chromosomes);
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        // Verify that the list of chromosomes is returned correctly
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation.setChromosomes(chromosomes);

        List<Chromosome> retrievedChromosomes = listPopulation.getChromosomes();

        assertEquals(2, retrievedChromosomes.size());
        assertTrue(retrievedChromosomes.contains(chromosome1));
        assertTrue(retrievedChromosomes.contains(chromosome2));
    }

    @Test
    public void testGetFittestChromosome() {
        // Verify that the fittest chromosome is returned correctly
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        chromosome1.setFitness(4.5);
        chromosome2.setFitness(6.7);
        chromosome3.setFitness(3.2);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);

        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testExceededPopulationLimitAddChromosome() {
        // Verify that adding a chromosome exceeding the population limit throws NumberIsTooLargeException
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        listPopulation.addChromosome(chromosome);
        listPopulation.addChromosome(chromosome);
        listPopulation.addChromosome(chromosome);
        listPopulation.addChromosome(chromosome);
        listPopulation.addChromosome(chromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        // Verify that the population limit is returned correctly
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeSetPopulationLimit() {
        // Verify that setting a negative population limit throws NotPositiveException
        listPopulation.setPopulationLimit(-2);
    }

    @Test(expected = NotPositiveException.class)
    public void testZeroSetPopulationLimit() {
        // Verify that setting a zero population limit throws NotPositiveException
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallerSetPopulationLimit() {
        // Verify that setting a smaller population limit than the number of chromosomes throws NumberIsTooSmallException
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        // Verify that the population size is returned correctly
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);

        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test
    public void testIterator() {
        // Verify that the iterator returns the chromosomes correctly
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);

        List<Chromosome> retrievedChromosomes = new ArrayList<>();
        for (Chromosome chromosome : listPopulation) {
            retrievedChromosomes.add(chromosome);
        }

        assertEquals(2, retrievedChromosomes.size());
        assertTrue(retrievedChromosomes.contains(chromosome1));
        assertTrue(retrievedChromosomes.contains(chromosome2));
    }
}