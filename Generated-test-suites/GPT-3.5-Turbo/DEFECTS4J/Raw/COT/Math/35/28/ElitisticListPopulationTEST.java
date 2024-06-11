import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import java.util.*;

public class ElitisticListPopulationTest {
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.9);
        Population nextGeneration = population.nextGeneration();
        
        List<Chromosome> nextChromosomes = nextGeneration.getChromosomes();
        assertEquals(chromosomes.get(1), nextChromosomes.get(0));
        assertEquals(chromosomes.get(2), nextChromosomes.get(1));
    }
    
    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.9);
        
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.0001);
    }
    
    @Test(expected=OutOfRangeException.class)
    public void testSetElitismRateWithOutOfRangeValue() {
        ElitisticListPopulation population = new ElitisticListPopulation(3, 0.9);
        
        population.setElitismRate(1.5);
    }
}