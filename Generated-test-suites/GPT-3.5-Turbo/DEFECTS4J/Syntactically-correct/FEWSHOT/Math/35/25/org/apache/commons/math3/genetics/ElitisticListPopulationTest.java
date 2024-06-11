import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        population = new ElitisticListPopulation(100, 0.9);
    }

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
      
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
      
        assertNotNull(population);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);

        assertNotNull(population);
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.5);

        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRangeLow() {
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRangeHigh() {
        population.setElitismRate(1.1);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> oldChromosomes = new ArrayList<>();
        oldChromosomes.add(new Chromosome("1"));
        oldChromosomes.add(new Chromosome("3"));
        oldChromosomes.add(new Chromosome("2"));
        oldChromosomes.add(new Chromosome("4"));
      
        population.addChromosomes(oldChromosomes);

        Population nextGeneration = population.nextGeneration();

        assertNotNull(nextGeneration);
        assertEquals(population.getPopulationLimit(), nextGeneration.getPopulationLimit());
        assertEquals(population.getElitismRate(), ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.001);
        assertEquals(3, nextGeneration.getChromosomes().size());
        assertTrue(nextGeneration.getChromosomes().containsAll(oldChromosomes.subList(1, 4)));
    }
}