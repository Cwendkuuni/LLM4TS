import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertNotNull(population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.5);
        ElitisticListPopulation nextGeneration = (ElitisticListPopulation) population.nextGeneration();
        assertEquals(2, nextGeneration.getChromosomes().size());
        assertTrue(nextGeneration.getChromosomes().contains(new Chromosome(2)));
        assertTrue(nextGeneration.getChromosomes().contains(new Chromosome(3)));
    }

    @Test
    public void testSetElitismRateWithinRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(1.5);
    }
}