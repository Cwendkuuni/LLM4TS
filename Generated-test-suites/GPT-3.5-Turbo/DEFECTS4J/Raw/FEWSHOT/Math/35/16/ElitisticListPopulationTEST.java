import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.0001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(1.5);
    }
    
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.7);
        
        assertEquals(0.7, population.getElitismRate(), 0.0001);
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome();
        c1.setFitness(10);
        Chromosome c2 = new Chromosome();
        c2.setFitness(20);
        Chromosome c3 = new Chromosome();
        c3.setFitness(30);
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 2, 0.5);
        
        Population nextGeneration = population.nextGeneration();
        
        assertEquals(2, nextGeneration.getPopulationLimit());
        assertEquals(0.5, ((ElitisticListPopulation) nextGeneration).getElitismRate(), 0.0001);
        assertEquals(2, nextGeneration.getChromosomes().size());
        assertEquals(c2, nextGeneration.getChromosomes().get(0));
        assertEquals(c3, nextGeneration.getChromosomes().get(1));
    }
}