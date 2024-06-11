import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.01);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        Population nextGeneration = population.nextGeneration();

        assertNotNull(nextGeneration);
        assertEquals(10, nextGeneration.getPopulationLimit());
        assertEquals(0.9, ((ElitisticListPopulation)nextGeneration).getElitismRate(), 0.01);
    }

    @Test
    public void testSetElitismRateValid() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.01);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalid() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        population.setElitismRate(1.5);  // This should throw OutOfRangeException
    }

}