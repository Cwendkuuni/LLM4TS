import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithListAndLimit() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        Population population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationConstructorWithLimit() {
        Population population = new ElitisticListPopulation(5, 0.9);
        assertEquals(5, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithNegativeElitismRate() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, -0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorWithElitismRateGreaterThanOne() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        new ElitisticListPopulation(chromosomes, 10, 1.1);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new DummyChromosome(i));
        }
        ElitisticListPopulation pop = new ElitisticListPopulation(chromosomes, 10, 0.9);
        ElitisticListPopulation nextGeneration = pop.nextGeneration();
        assertEquals(5, nextGeneration.size());
        for (int i = 0; i < 5; i++) {
            Chromosome chromosome = pop.getChromosome(i);
            Chromosome nextGenerationChromosome = nextGeneration.getChromosome(i);
            assertEquals(chromosome, nextGenerationChromosome);
        }
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation pop = new ElitisticListPopulation(new java.util.ArrayList<>(), 10, 0.9);
        pop.setElitismRate(0.8);
        assertEquals(0.8, pop.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateNegative() {
        ElitisticListPopulation pop = new ElitisticListPopulation(new java.util.ArrayList<>(), 10, 0.9);
        pop.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation pop = new ElitisticListPopulation(new java.util.ArrayList<>(), 10, 0.9);
        pop.setElitismRate(1.1);
    }

}

class DummyChromosome implements Chromosome {
    private int id;

    public DummyChromosome(int id) {
        this.id = id;
    }

    @Override
    public double getFitness() {
        return id;
    }
}