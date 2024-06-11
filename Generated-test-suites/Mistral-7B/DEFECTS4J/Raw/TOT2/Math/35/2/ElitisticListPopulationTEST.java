
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorValidArguments() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(populationLimit, elitisticListPopulation.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorInvalidElitismRate() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        int populationLimit = 10;
        double elitismRate = 1.5;

        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testNextGenerationValidPopulation() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(5, 0.9);
        elitisticListPopulation.addChromosome(new Chromosome());
        elitisticListPopulation.addChromosome(new Chromosome());
        elitisticListPopulation.addChromosome(new Chromosome());

        ElitisticListPopulation nextGeneration = elitisticListPopulation.nextGeneration();

        assertEquals(elitisticListPopulation.size(), elitisticListPopulation.size() * 0.9 + 5, nextGeneration.size());
    }

    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(0, 0.9);

        ElitisticListPopulation nextGeneration = elitisticListPopulation.nextGeneration();

        assertEquals(0, nextGeneration.size());
    }

    @Test
    public void testSetElitismRateValidElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(5, 0.9);
        elitisticListPopulation.setElitismRate(0.8);

        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateInvalidElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(5, 0.9);
        elitisticListPopulation.setElitismRate(1.2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorNullChromosomesList() {
        List<Chromosome> chromosomes = null;
        int populationLimit = 10;
        double elitismRate = 0.9;

        assertThrows(NullPointerException.class, () -> new ElitisticListPopulation(chromosomes, populationLimit, elitismRate));
    }

    @Test(expected = OutOfRangeException.class)
    public void testNextGenerationWithNegativePopulationLimit() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(5, 0.9);

        assertThrows(OutOfRangeException.class, () -> elitisticListPopulation.nextGeneration(-5));
    }

    @Test
    public void testNextGenerationWithFractionalPopulationLimit() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(5, 0.9);

        ElitisticListPopulation nextGeneration = elitisticListPopulation.nextGeneration((int) 5.5);

        assertEquals(6, nextGeneration.size());
    }

    @Test
    public void testNextGenerationWithPopulationSmallerThanElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(5, 0.9);

        ElitisticListPopulation nextGeneration = elitisticListPopulation.nextGeneration();

        assertEquals(5, nextGeneration.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithNegativeElitismRate() {
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(5, 0.9);

        elitisticListPopulation.setElitismRate(-0.1);
    }
}