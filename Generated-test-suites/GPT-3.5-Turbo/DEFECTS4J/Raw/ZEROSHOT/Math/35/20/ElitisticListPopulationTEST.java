import org.junit.Test;
import org.apache.commons.math3.genetics.*;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(chromosomes, population.getChromosomes());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.0001);
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        chromosomes.add(c1);
        chromosomes.add(c2);
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Population nextGen = population.nextGeneration();
        assertEquals(10, nextGen.getPopulationLimit());
        assertEquals(0.9, ((ElitisticListPopulation)nextGen).getElitismRate(), 0.0001);
        assertEquals(1, nextGen.getChromosomes().size());
        assertEquals(c2, nextGen.getChromosomes().get(0));
    }
    
    @Test
    public void testSetElitismRate() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        population.setElitismRate(0.5);
        assertEquals(0.5, population.getElitismRate(), 0.0001);
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        population.setElitismRate(1.5);
    }

}