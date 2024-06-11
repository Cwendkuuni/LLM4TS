import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.math3.genetics.ElitisticListPopulation.ELITISM_RATE_DEFAULT;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructionWithListAndLimit() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, ELITISM_RATE_DEFAULT);
        Assert.assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationConstructionWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, ELITISM_RATE_DEFAULT);
        Assert.assertEquals(5, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionWithNegativeElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructionWithElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(new ArrayList<>(), 5, 1.2);
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithListAndLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Chromosome chromosome = new DummyChromosome(i);
            chromosomes.add(chromosome);
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, ELITISM_RATE_DEFAULT);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(10, nextGeneration.size());
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithLimit() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, ELITISM_RATE_DEFAULT);
        population.addChromosome(new DummyChromosome(0));
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(4));

        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(5, nextGeneration.size());
        Assert.assertFalse(nextGeneration.contains(population.getChromosome(4)));
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, ELITISM_RATE_DEFAULT);
        population.addChromosome(new DummyChromosome(0));
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(4));
        population.addChromosome(new DummyChromosome(5));
        population.addChromosome(new DummyChromosome(6));

        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(5, nextGeneration.size());
        int boundIndex = (int) Math.ceil((1.0 - ELITISM_RATE_DEFAULT) * population.size());
        for (int i = boundIndex; i < population.size(); i++) {
            Chromosome chromosome = nextGeneration.getChromosome(i - boundIndex);
            Chromosome oldChromosome = population.getChromosome(i);
            Assert.assertEquals(oldChromosome, chromosome);
        }
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 1.2);
        population.addChromosome(new DummyChromosome(0));
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(4));
        population.addChromosome(new DummyChromosome(5));

        Population nextGeneration = population.nextGeneration();
        Assert.fail("Next generation size should be less than population size");
    }

    @Test
    public void testElitisticListPopulationNextGenerationWithElitismRateZero() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.0);
        population.addChromosome(new DummyChromosome(0));
        population.addChromosome(new DummyChromosome(1));
        population.addChromosome(new DummyChromosome(2));
        population.addChromosome(new DummyChromosome(3));
        population.addChromosome(new DummyChromosome(4));

        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(5, nextGeneration.size());
        for (int i = 0; i < population.size(); i++) {
            Chromosome chromosome = nextGeneration.getChromosome(i);
            Chromosome oldChromosome = population.getChromosome(i);
            Assert.assertEquals(oldChromosome, chromosome);
        }
    }

    @Test
    public void testElitisticListPopulationSetElitismRateWithNegativeValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, ELITISM_RATE_DEFAULT);
        Assert.assertThrows(OutOfRangeException.class, () -> population.setElitismRate(-0.1));
    }

    @Test
    public void testElitisticListPopulationSetElitismRateWithValueGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, ELITISM_RATE_DEFAULT);
        Assert.assertThrows(OutOfRangeException.class, () -> population.setElitismRate(1.2));
    }

    @Test
    public void testElitisticListPopulationSetElitismRateWithValueEqualToOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, ELITISM_RATE_DEFAULT);
        population.setElitismRate(1.0);
        Assert.assertEquals(1.0, population.getElitismRate(), Precision.EPSILON);
    }

    @Test
    public void testElitisticListPopulationSetElitismRateWithValueLessThanZero() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, ELITISM_RATE_DEFAULT);
        population.setElitismRate(0.0);
        Assert.assertEquals(0.0, population.getElitismRate(), Precision.EPSILON);
    }

    @Test
    public void testElitisticListPopulationSetElitismRateWithValueGreaterThanZeroButLessThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, ELITISM_RATE_DEFAULT);
        population.setElitismRate(0.5);
        Assert.assertEquals(0.5, population.getElitismRate(), Precision.EPSILON);
    }

    private static class DummyChromosome implements Chromosome {
        private int index;

        public DummyChromosome(int index) {
            this.index = index;
        }

        @Override
        public int getFitness() {
            return index;
        }

        @Override
        public int compareTo(Chromosome other) {
            DummyChromosome otherChromosome = (DummyChromosome) other;
            return FastMath.compare(this.getFitness(), otherChromosome.getFitness());
        }

        @Override
        public String toString() {
            return String.valueOf(getFitness());
        }
    }

}