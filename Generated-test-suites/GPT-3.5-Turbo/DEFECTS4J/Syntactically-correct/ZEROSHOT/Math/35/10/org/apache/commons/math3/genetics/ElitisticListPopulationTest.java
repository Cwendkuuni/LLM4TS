import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {
    
    private ElitisticListPopulation testPopulation;
    
    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        List<Integer> genes1 = new ArrayList<>();
        genes1.add(1);
        genes1.add(2);
        Chromosome chromosome1 = new Chromosome(genes1);
        chromosomes.add(chromosome1);
        List<Integer> genes2 = new ArrayList<>();
        genes2.add(3);
        genes2.add(4);
        Chromosome chromosome2 = new Chromosome(genes2);
        chromosomes.add(chromosome2);
        
        testPopulation = new ElitisticListPopulation(chromosomes, 10, 0.5);
    }
    
    @Test
    public void testNextGeneration() {
        ListPopulation nextGeneration = testPopulation.nextGeneration();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        assertEquals(1, newChromosomes.size());
        assertEquals(testPopulation.getChromosomes().get(1), newChromosomes.get(0));
    }
    
    @Test
    public void testSetElitismRate() {
        double newElitismRate = 0.7;
        testPopulation.setElitismRate(newElitismRate);
        assertEquals(newElitismRate, testPopulation.getElitismRate(), 0.0001);
    }
    
    @Test(expected=OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        double newElitismRate = -0.1;
        testPopulation.setElitismRate(newElitismRate);
    }
}