import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        assertNotNull(population.getChromosomes());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 100, 0.9);
        
        Population nextGeneration = population.nextGeneration();
        
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        
        assertEquals(1, nextGenerationChromosomes.size());
        assertEquals(chromosome3, nextGenerationChromosomes.get(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateBelowRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateAboveRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(1.1);
    }

    @Test
    public void testSetElitismRateWithinRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(100, 0.9);
        
        population.setElitismRate(0.5);
        
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }

}