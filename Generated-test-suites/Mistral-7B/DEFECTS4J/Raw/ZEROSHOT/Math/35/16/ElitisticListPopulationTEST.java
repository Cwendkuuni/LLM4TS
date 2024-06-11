import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = createDummyChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.9);
        assertEquals(15, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationNegativeElitismRate() {
        List<Chromosome> chromosomes = createDummyChromosomes(10);
        new ElitisticListPopulation(chromosomes, 15, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationGreaterThanOneElitismRate() {
        List<Chromosome> chromosomes = createDummyChromosomes(10);
        new ElitisticListPopulation(chromosomes, 15, 1.1);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = createDummyChromosomes(11);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.9);
        Population nextGeneration = population.nextGeneration();

        assertEquals(15, nextGeneration.size());
        ElitisticListPopulation nextPopulation = (ElitisticListPopulation) nextGeneration;

        // check that the elitism part is the same
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);
        int boundaryIndex = (int) FastMath.ceil((1.0 - 0.9) * oldChromosomes.size());
        for (int i = boundaryIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome nextChromosome = nextPopulation.getChromosome(i);
            assertEquals(oldChromosome, nextChromosome);
        }

        // check that the rest is different
        for (int i = 0; i < boundaryIndex; i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome nextChromosome = nextPopulation.getChromosome(i);
            assertNotEquals(oldChromosome, nextChromosome);
        }
    }

    @Test
    public void testSetElitismRate() {
        List<Chromosome> chromosomes = createDummyChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.9);

        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegativeElitismRate() {
        List<Chromosome> chromosomes = createDummyChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.9);

        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOneElitismRate() {
        List<Chromosome> chromosomes = createDummyChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 15, 0.9);

        population.setElitismRate(1.1);
    }

    private List<Chromosome> createDummyChromosomes(int size) {
        return java.util.Collections.nCopies(size, new DummyChromosome());
    }

    private static class DummyChromosome implements Chromosome {
        @Override
        public String toString() {
            return "DummyChromosome";
        }

        @Override
        public double[] getGenes() {
            return new double[0];
        }

        @Override
        public void setGenes(double[] genes) {
        }

        @Override
        public int compareTo(Chromosome o) {
            return 0;
        }
    }
}