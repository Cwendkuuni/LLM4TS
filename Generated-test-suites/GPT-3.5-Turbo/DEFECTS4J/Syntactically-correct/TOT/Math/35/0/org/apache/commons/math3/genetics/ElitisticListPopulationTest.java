import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 5;
        double elitismRate = 0.9;
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testElitisticListPopulationConstructor() {
        assertEquals(5, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.9, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 0.8;
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        assertEquals(10, elitisticListPopulation.getPopulationLimit());
        assertEquals(0.8, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationConstructorException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        double elitismRate = 1.5;
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        List<Chromosome> oldChromosomes = elitisticListPopulation.getChromosomes();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();

        int boundIndex = (int) Math.ceil((1.0 - elitisticListPopulation.getElitismRate()) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            assertTrue(newChromosomes.contains(oldChromosomes.get(i)));
        }
    }

    @Test
    public void testSetElitismRate() {
        double newElitismRate = 0.75;
        elitisticListPopulation.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, elitisticListPopulation.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateException() {
        double newElitismRate = -0.5;
        elitisticListPopulation.setElitismRate(newElitismRate);
    }

}