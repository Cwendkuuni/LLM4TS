import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;

public class ElitisticListPopulationTest {

    @Test
    public void testElitismRateConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.8;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitismRateSetter() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);

        double newElitismRate = 0.9;
        population.setElitismRate(newElitismRate);

        assertEquals(newElitismRate, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.8);

        try {
            population.setElitismRate(1.5);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(5));
        chromosomes.add(new Chromosome(10));
        ElitisticListPopulation population = new ElitisticListPopulation(2, 0.8);
        population.addChromosomes(chromosomes);

        ElitisticListPopulation nextGeneration = (ElitisticListPopulation) population.nextGeneration();

        assertEquals(2, nextGeneration.getChromosomes().size());
        assertEquals(10, nextGeneration.getChromosomes().get(0).getFitness());
    }

}