import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        // typical use case: initialize ElitisticListPopulation with valid values
        int populationLimit = 50;
        double elitismRate = 0.9;
        
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        
        assertNotNull(population);
        assertEquals(population.getPopulationLimit(), populationLimit);
        assertEquals(population.getElitismRate(), elitismRate, 0.001);
    }
    
    @Test
    public void testSetElitismRate() {
        // typical use case: set elitism rate within valid range
        double elitismRate = 0.75;
        
        ElitisticListPopulation population = new ElitisticListPopulation(50, 0.9);
        population.setElitismRate(elitismRate);
        
        assertEquals(population.getElitismRate(), elitismRate, 0.001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        // error scenario: set elitism rate outside of valid range
        double elitismRate = 1.5;
        
        ElitisticListPopulation population = new ElitisticListPopulation(50, 0.9);
        population.setElitismRate(elitismRate);
    }
    
    @Test
    public void testNextGeneration() {
        // typical use case: generate next generation with elitism
        int populationLimit = 50;
        double elitismRate = 0.9;
        
        // create a population with chromosomes
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < populationLimit; i++) {
            Chromosome chromosome = new Chromosome();
            // add some fitness value to the chromosome for sorting
            chromosome.setFitness(i);
            chromosomes.add(chromosome);
        }
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        Population nextGeneration = population.nextGeneration();
        
        // check that the next generation has correct number of chromosomes
        assertEquals(nextGeneration.getPopulationLimit(), populationLimit);
        
        // check that the first (populationLimit * (1 - elitismRate)) chromosomes are not in the next generation
        for (int i = 0; i < populationLimit * (1 - elitismRate); i++) {
            assertFalse(nextGeneration.getChromosomes().contains(chromosomes.get(i)));
        }
        
        // check that the remaining chromosomes are in the next generation
        for (int i = (int) Math.ceil(populationLimit * (1 - elitismRate)); i < populationLimit; i++) {
            assertTrue(nextGeneration.getChromosomes().contains(chromosomes.get(i)));
        }
    }
}