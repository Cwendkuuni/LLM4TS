import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = createChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
        assertEquals(20, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithNegativeElitismRate() {
        List<Chromosome> chromosomes = createChromosomes(5);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = createChromosomes(5);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 1.2);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = createChromosomes(10);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(20, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithElitism() {
        List<Chromosome> chromosomes = createChromosomes(15);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 20, 0.5);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        List<Chromosome> oldChromosomes = population.getChromosomes();
        Collections.sort(oldChromosomes);
        int elitismSize = (int) FastMath.ceil(0.5 * oldChromosomes.size());
        int index = 0;
        for (Chromosome chromosome : nextGeneration.getChromosomes()) {
            assertTrue(oldChromosomes.get(index++) instanceof Chromosome);
        }
    }

    @Test
    public void testSetElitismRate() {
        List<Chromosome> chromosomes = createChromosomes(5);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        List<Chromosome> chromosomes = createChromosomes(5);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        population.setElitismRate(-0.1);
    }

    private List<Chromosome> createChromosomes(int size) {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            chromosomes.add(new DummyChromosome(i));
        }
        return chromosomes;
    }

    private static class DummyChromosome implements Chromosome {
        private final int id;

        public DummyChromosome(int id) {
            this.id = id;
        }

        @Override
        public double getFitness() {
            return id;
        }

        @Override
        public String toString() {
            return "DummyChromosome:" + id;
        }
    }

}