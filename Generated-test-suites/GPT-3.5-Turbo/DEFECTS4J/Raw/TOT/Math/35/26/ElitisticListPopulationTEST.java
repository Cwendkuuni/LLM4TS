import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.FastMath;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructor() {
        // Typical case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("1010"));
        chromosomes.add(new Chromosome("1100"));
        chromosomes.add(new Chromosome("1111"));
        chromosomes.add(new Chromosome("0000"));
        int populationLimit = 10;
        double elitismRate = 0.8;

        ElitisticListPopulation population1 = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        assertEquals(chromosomes, population1.getChromosomes());
        assertEquals(populationLimit, population1.getPopulationLimit());
        assertEquals(elitismRate, population1.getElitismRate(), 0.00001);

        // Edge case: Empty chromosomes list
        List<Chromosome> emptyChromosomes = new ArrayList<>();
        int emptyPopulationLimit = 0;
        double emptyElitismRate = 0.0;

        ElitisticListPopulation population2 = new ElitisticListPopulation(emptyChromosomes, emptyPopulationLimit, emptyElitismRate);

        assertEquals(emptyChromosomes, population2.getChromosomes());
        assertEquals(emptyPopulationLimit, population2.getPopulationLimit());
        assertEquals(emptyElitismRate, population2.getElitismRate(), 0.00001);
    }

    @Test(expected=OutOfRangeException.class)
    public void testConstructor_OutOfRangeException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("1010"));
        int populationLimit = 10;
        // Error scenario: elitism rate is outside the [0, 1] range
        double elitismRate = -0.5;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
    }

    @Test
    public void testNextGeneration() {
        // Typical case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("1010"));
        chromosomes.add(new Chromosome("1100"));
        chromosomes.add(new Chromosome("1111"));
        chromosomes.add(new Chromosome("0000"));
        int populationLimit = 10;
        double elitismRate = 0.8;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        Population nextGeneration = population.nextGeneration();

        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(new Chromosome("1111"));
        expectedChromosomes.add(new Chromosome("0000"));

        assertEquals(expectedChromosomes, nextGeneration.getChromosomes());

        // Edge case: No chromosomes selected for next generation
        List<Chromosome> emptyChromosomes = new ArrayList<>();
        int emptyPopulationLimit = 10;
        double emptyElitismRate = 0.0;

        ElitisticListPopulation emptyPopulation = new ElitisticListPopulation(emptyChromosomes, emptyPopulationLimit, emptyElitismRate);
        Population emptyNextGeneration = emptyPopulation.nextGeneration();

        assertEquals(emptyChromosomes, emptyNextGeneration.getChromosomes());
    }

    @Test
    public void testSetElitismRate() {
        // Typical case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("1010"));
        int populationLimit = 10;
        double elitismRate = 0.8;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        double newElitismRate = 0.5;
        population.setElitismRate(newElitismRate);

        assertEquals(newElitismRate, population.getElitismRate(), 0.00001);

        // Error scenario: setting elitism rate outside the [0, 1] range
        double invalidElitismRate = -0.5;
        try {
            population.setElitismRate(invalidElitismRate);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertEquals("Elitism rate (-0.5) is out of range (0.0, 1.0)", e.getMessage());
        }
    }

    @Test
    public void testGetElitismRate() {
        // Typical case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome("1010"));
        int populationLimit = 10;
        double elitismRate = 0.8;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        assertEquals(elitismRate, population.getElitismRate(), 0.00001);

        // Edge case: Elitism rate is 0.0
        List<Chromosome> zeroChromosomes = new ArrayList<>();
        int zeroPopulationLimit = 10;
        double zeroElitismRate = 0.0;

        ElitisticListPopulation zeroPopulation = new ElitisticListPopulation(zeroChromosomes, zeroPopulationLimit, zeroElitismRate);

        assertEquals(zeroElitismRate, zeroPopulation.getElitismRate(), 0.00001);
    }
}