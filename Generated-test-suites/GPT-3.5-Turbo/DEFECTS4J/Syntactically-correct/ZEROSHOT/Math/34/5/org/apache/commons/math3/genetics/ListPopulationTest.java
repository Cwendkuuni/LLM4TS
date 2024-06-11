import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    private List<Chromosome> chromosomes;
    private ListPopulation population;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        population = new ListPopulation(chromosomes, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(chromosomes, -1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithZeroPopulationLimit() {
        new ListPopulation(chromosomes, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedPopulationLimit() {
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        population.setChromosomes(newChromosomes);

        assertEquals(newChromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesExceedPopulationLimit() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        population.setChromosomes(newChromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        population.addChromosomes(newChromosomes);

        assertEquals(2, population.getChromosomes().size());
        assertTrue(population.getChromosomes().containsAll(newChromosomes));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        population.addChromosome(chromosome);
    }

    @Test
    public void testGetChromosomes() {
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        assertEquals(chromosomes, population.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);

        assertEquals(1, population.getChromosomes().size());
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedPopulationLimit() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        population.addChromosome(chromosome);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(15);
        assertEquals(15, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeLimit() {
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithChromosomesExceedNewLimit() {
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(0);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());

        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());

        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testToString() {
        assertEquals("[]", population.toString());

        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);

        assertEquals("[" + chromosome.toString() + "]", population.toString());
    }
}