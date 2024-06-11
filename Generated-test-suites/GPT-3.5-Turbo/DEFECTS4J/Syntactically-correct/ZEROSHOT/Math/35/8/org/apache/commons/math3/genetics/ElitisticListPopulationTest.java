import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // create a population with some chromosomes
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        
        // compute the next generation
        Population nextGeneration = population.nextGeneration();
        
        // check the size of the next generation
        assertEquals(10, nextGeneration.getPopulationLimit());
        
        // check that the best chromosomes are directly copied
        List<Chromosome> oldChromosomes = population.getChromosomes();
        List<Chromosome> newChromosomes = nextGeneration.getChromosomes();
        for (int i = 0; i < (int)(0.5 * oldChromosomes.size()); i++) {
            assertSame(oldChromosomes.get(i), newChromosomes.get(i));
        }
    }

    @Test
    public void testSetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.0);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithValueBelowZero() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(-0.1);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateWithValueAboveOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(1.1);
    }

    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        assertEquals(0.5, population.getElitismRate(), 0.0);
    }
    
}