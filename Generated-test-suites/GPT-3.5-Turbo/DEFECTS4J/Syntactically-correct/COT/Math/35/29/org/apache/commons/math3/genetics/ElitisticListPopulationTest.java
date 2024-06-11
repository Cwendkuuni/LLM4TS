import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        Population nextGeneration = population.nextGeneration();

        Assert.assertEquals(3, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRateWithinRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        population.setElitismRate(0.5);

        Assert.assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfBounds() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);

        population.setElitismRate(1.5);
    }

}