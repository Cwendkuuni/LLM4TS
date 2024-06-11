import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(1, population.getChromosomes().size());
    }

    @Test
    public void testConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertNotNull(population.getChromosomes());
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.9);
        Population nextGeneration = population.nextGeneration();
        assertEquals(1, nextGeneration.getChromosomes().size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 1.1);
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        assertEquals(0.8, population.getElitismRate(), 0);
    }
    
}