import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test class for ElitisticListPopulation.
 */
public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithListAndLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Assert.assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationConstructorWithLimitAndRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        Assert.assertEquals(10, population.size());
    }

    @Test
    public void testNextGenerationWithEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        Population nextGeneration = population.nextGeneration();
        Assert.assertNotNull(nextGeneration);
        Assert.assertEquals(1, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithPopulation() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        chromosomes.add(new DummyChromosome(1));
        chromosomes.add(new DummyChromosome(2));
        chromosomes.add(new DummyChromosome(3));
        chromosomes.add(new DummyChromosome(4));
        chromosomes.add(new DummyChromosome(5));
        Collections.sort(chromosomes);

        Population nextGeneration = population.nextGeneration();
        Assert.assertNotNull(nextGeneration);
        Assert.assertEquals(3, nextGeneration.size());

        int boundIndex = (int) Math.ceil((1.0 - 0.9) * chromosomes.size());
        for (int i = boundIndex; i < chromosomes.size(); i++) {
            Assert.assertEquals(chromosomes.get(i), nextGeneration.get(i - 3));
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        population.setElitismRate(1.1);
    }

    @Test
    public void testSetElitismRateValid() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 10, 0.9);
        population.setElitismRate(0.5);
        Assert.assertEquals(0.5, population.getElitismRate(), 0);
    }

}

class DummyChromosome implements Chromosome {
    private int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double[] getGenes() {
        return new double[]{id};
    }

    @Override
    public void setGenes(double[] genes) {
        // not used
    }

    @Override
    public double fitness() {
        return id;
    }
}