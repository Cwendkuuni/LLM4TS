import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        
        assertNotNull(population);
        assertEquals(0.5, population.getElitismRate(), 0.0001);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.5);
        Population nextGeneration = population.nextGeneration();
        
        assertEquals(1, nextGeneration.getChromosomes().size());
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(0.7);
        
        assertEquals(0.7, population.getElitismRate(), 0.0001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateException() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        
        population.setElitismRate(1.5);
    }

}