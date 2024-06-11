import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {
    
    private ElitisticListPopulation population;
    
    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ElitisticListPopulation(chromosomes, 5, 0.9);
    }
    
    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        assertEquals(4, nextGeneration.getChromosomes().size());
    }
    
    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-0.5);
    }
    
    @Test
    public void testGetElitismRate() {
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }
}