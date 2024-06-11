
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(10);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullChromosomes() {
        population = new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativePopulationLimit() {
        population = new ListPopulation(10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_PopulationSizeExceeded() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullPointerException.class)
    public void testSetChromosomes_NullChromosomes() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_PopulationSizeExceeded() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_PopulationSizeExceeded() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_PopulationSizeExceeded() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(15);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2, chromosome3);
        population.setChromosomes(chromosomes);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit_SmallerPopulationSize() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        population.setChromosomes(chromosomes);
        population.setPopulationLimit(2);
        assertEquals(2, population.getPopulationLimit());
        assertEquals(2, population.getChromosomes().size());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_SmallerThanCurrentSize() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        population.setChromosomes(chromosomes);
        population.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        assertEquals("[]", population.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        population.setChromosomes(chromosomes);
        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
            assertTrue(chromosomes.contains(chromosome));
        }
        assertEquals(3, count);
    }

}
