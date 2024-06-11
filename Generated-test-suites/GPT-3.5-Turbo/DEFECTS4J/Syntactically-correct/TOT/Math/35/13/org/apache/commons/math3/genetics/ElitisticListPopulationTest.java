import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.genetics.Chromosome;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructor1() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 100;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test
    public void testConstructor2() {
        int populationLimit = 100;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);

        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 100;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        ElitisticListPopulation nextGeneration = (ElitisticListPopulation) population.nextGeneration();

        assertEquals(population.getPopulationLimit(), nextGeneration.getPopulationLimit());
        assertEquals(population.getElitismRate(), nextGeneration.getElitismRate(), 0.001);
        assertEquals(chromosomes.get(1), nextGeneration.getChromosomes().get(0));
        assertEquals(chromosomes.get(2), nextGeneration.getChromosomes().get(1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        double elitismRate = -0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(100, elitismRate);
    }

    @Test
    public void testSetElitismRate() {
        double elitismRate = 0.5;

        ElitisticListPopulation population = new ElitisticListPopulation(100, elitismRate);

        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }

}