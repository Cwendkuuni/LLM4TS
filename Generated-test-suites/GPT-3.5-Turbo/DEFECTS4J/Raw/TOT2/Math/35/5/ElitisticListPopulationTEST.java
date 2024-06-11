import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.OutOfRangeException;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        // Test constructor with list of chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.8);

        assertEquals(10, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        // Test constructor without list of chromosomes
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);

        assertEquals(10, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test
    public void testSetElitismRate() {
        // Test setting elitism rate
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);
        elitisticListPopulation.setElitismRate(0.5);

        assertEquals(0.5, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test
    public void testSetElitismRateOutOfRange() {
        // Test setting elitism rate outside range
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);

        try {
            elitisticListPopulation.setElitismRate(1.5);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected behavior
        }
    }

    @Test
    public void testNextGeneration() {
        // Test generation of next population
        ElitisticListPopulation elitisticListPopulation = new ElitisticListPopulation(10, 0.8);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        elitisticListPopulation.setChromosomes(chromosomes);

        Population nextGeneration = elitisticListPopulation.nextGeneration();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();

        assertEquals(1, nextGenerationChromosomes.size());
        assertEquals(chromosomes.get(0), nextGenerationChromosomes.get(0));
    }

}