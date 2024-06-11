import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);

        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.001);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(20, 0.5);

        assertEquals(20, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.001);
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.6);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(mock(Chromosome.class));
        }
        population.addChromosomes(chromosomes);

        Population nextGeneration = population.nextGeneration();

        assertEquals(5, nextGeneration.getChromosomes().size());
        assertTrue(nextGeneration.getChromosomes().containsAll(chromosomes));
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.7);

        assertEquals(0.7, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(1.5);
    }

}