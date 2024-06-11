import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);
        
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.8, population.getElitismRate(), 0.01);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(5));
        chromosomes.add(new Chromosome(10));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        
        Population nextGen = population.nextGeneration();
        
        List<Chromosome> nextGenChromosomes = nextGen.getChromosomes();
        
        assertEquals(1, nextGenChromosomes.size());
        assertEquals(10, nextGenChromosomes.get(0).getFitness());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateException() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.8);
        
        population.setElitismRate(1.5);
    }

}