import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationCreationWithListAndLimit() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.8;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationCreationWithLimit() {
        int populationLimit = 10;
        double elitismRate = 0.8;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationWithNegativeElitismRate() {
        int populationLimit = 10;
        double elitismRate = -0.1;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationCreationWithElitismRateGreaterThanOne() {
        int populationLimit = 10;
        double elitismRate = 1.1;
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> oldChromosomes = new java.util.ArrayList<>();
        for (int i = 0; i < 20; i++) {
            oldChromosomes.add(new DummyChromosome(i));
        }
        Collections.sort(oldChromosomes);

        ElitisticListPopulation population = new ElitisticListPopulation(oldChromosomes, 20, 0.6);

        Population nextGeneration = population.nextGeneration();

        assertEquals(20, nextGeneration.getPopulationLimit());
        assertEquals(0.6, nextGeneration.getElitismRate(), 0.001);

        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        assertEquals(oldChromosomes.size(), newChromosomes.size());

        // check that the elitism rate number of best chromosomes are copied
        int elitismIndex = (int) Math.ceil(oldChromosomes.size() * 0.6);
        for (int i = 0; i < elitismIndex; i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i);
            assertEquals(oldChromosome, newChromosome);
        }

        // check that the rest of the chromosomes are created anew
        for (int i = elitismIndex; i < oldChromosomes.size(); i++) {
            Chromosome oldChromosome = oldChromosomes.get(i);
            Chromosome newChromosome = newChromosomes.get(i);
            assertNotEquals(oldChromosome, newChromosome);
        }
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        population.setElitismRate(0.9);
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);
        population.setElitismRate(1.1);
    }

}

class DummyChromosome implements Chromosome {
    private final int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double[] getGenes() {
        return new double[0];
    }

    @Override
    public void setGenes(double[] genes) {

    }

    @Override
    public double getFitness() {
        return id;
    }

    @Override
    public String toString() {
        return "DummyChromosome " + id;
    }
}