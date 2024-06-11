import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ElitisticListPopulationTest {
    
    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        // Test case for the constructor with chromosomes
        // Typical use case: creating a population with a list of chromosomes
        Chromosome chrom1 = new Chromosome();
        Chromosome chrom2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chrom1);
        chromosomes.add(chrom2);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 50, 0.8);
        
        assertEquals(2, population.getChromosomes().size());
        assertEquals(50, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        // Test case for the constructor without chromosomes
        // Typical use case: creating an empty population
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        assertEquals(0, population.getChromosomes().size());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }
    
    @Test
    public void testNextGeneration() {
        // Test case for the nextGeneration() method
        // Typical use case: generating the next generation of the population
        Chromosome chrom1 = new Chromosome();
        Chromosome chrom2 = new Chromosome();
        Chromosome chrom3 = new Chromosome();
        Chromosome chrom4 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chrom1);
        chromosomes.add(chrom2);
        chromosomes.add(chrom3);
        chromosomes.add(chrom4);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 4, 0.5);
        
        Population nextGeneration = population.nextGeneration();
        
        assertEquals(2, nextGeneration.getChromosomes().size());
        assertEquals(4, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        // Test case for setElitismRate() method with an invalid elitism rate
        // Error scenario: setting a rate less than 0
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(-0.1);
    }
    
    @Test
    public void testSetElitismRateValid() {
        // Test case for setElitismRate() method with a valid elitism rate
        // Typical use case: setting a rate between 0 and 1
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(0.7);
        
        assertEquals(0.7, population.getElitismRate(), 0.001);
    }
}