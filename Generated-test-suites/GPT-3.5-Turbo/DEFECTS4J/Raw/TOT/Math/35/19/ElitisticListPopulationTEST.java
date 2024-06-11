import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(2, chromosomes.size());
        assertEquals(1, chromosomes.get(0).getId());
        assertEquals(2, chromosomes.get(1).getId());
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        population = new ElitisticListPopulation(10, 0.5);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertTrue(chromosomes.isEmpty());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-1);
    }

    @Test
    public void testSetElitismRateWithinRange() {
        population.setElitismRate(0.6);
        assertEquals(0.6, population.getElitismRate(), 0);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        List<Chromosome> oldChromosomes = population.getChromosomes();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        assertEquals(2, oldChromosomes.size());
        assertEquals(1, newChromosomes.size());
        assertEquals(2, newChromosomes.get(0).getId());
    }

}