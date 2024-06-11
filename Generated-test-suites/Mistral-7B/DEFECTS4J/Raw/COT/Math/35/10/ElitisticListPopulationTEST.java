import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithListChromosomesAndLimit() {
        List<Chromosome> chromosomes = Collections.singletonList(new DummyChromosome(1));
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        Assert.assertEquals(populationLimit, population.size());
    }

    @Test
    public void testConstructorWithLimitAndElitismRate() {
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        Assert.assertEquals(populationLimit, population.size());
    }

    @Test
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 0.9);

        Assert.assertNotNull(population.nextGeneration());
        Assert.assertEquals(0, population.nextGeneration().size());
    }

    @Test
    public void testNextGenerationWithPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));

        Population nextGeneration = population.nextGeneration();

        Assert.assertNotNull(nextGeneration);
        Assert.assertEquals(7, nextGeneration.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test
    public void testSetElitismRateValid() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        population.setElitismRate(0.8);
        Assert.assertEquals(0.8, population.getElitismRate(), 0.001);
    }
}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double getFitness() {
        return id;
    }
}