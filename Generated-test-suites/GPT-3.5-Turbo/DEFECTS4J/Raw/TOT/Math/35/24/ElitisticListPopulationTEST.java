import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.junit.Before;
import org.junit.Test;

public class ElitisticListPopulationTest {
    
    private ElitisticListPopulation population;
    
    @Before
    public void setUp() {
        population = new ElitisticListPopulation(10, 0.5);
    }
    
    @Test
    public void testElitisticListPopulation() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        assertNotNull(population);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.001);
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        ElitisticListPopulation nextGeneration = (ElitisticListPopulation) population.nextGeneration();
        assertNotNull(nextGeneration);
        assertEquals(10, nextGeneration.getPopulationLimit());
        assertEquals(0.5, nextGeneration.getElitismRate(), 0.001);
        assertNotEquals(chromosomes, nextGeneration.getChromosomes());
        assertEquals(5, nextGeneration.getChromosomes().size());
        assertEquals(chromosomes.get(5), nextGeneration.getChromosomes().get(0));
        assertEquals(chromosomes.get(6), nextGeneration.getChromosomes().get(1));
        assertEquals(chromosomes.get(7), nextGeneration.getChromosomes().get(2));
        assertEquals(chromosomes.get(8), nextGeneration.getChromosomes().get(3));
        assertEquals(chromosomes.get(9), nextGeneration.getChromosomes().get(4));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetElitismRate() {
        population.setElitismRate(-0.1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(1.1);
    }
}
