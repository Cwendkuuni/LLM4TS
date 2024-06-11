import org.junit.Test;
import static org.junit.Assert.*;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructor1() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        chromosomes.add(new Chromosome(4));
        chromosomes.add(new Chromosome(5));
        
        int populationLimit = 10;
        double elitismRate = 0.9;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        
        assertEquals(10, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertEquals(5, population.getChromosomes().size());
    }

    @Test
    public void testElitisticListPopulationConstructor2() {
        int populationLimit = 10;
        double elitismRate = 0.9;
        
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        
        assertEquals(10, population.getPopulationLimit());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertEquals(0, population.getChromosomes().size());
    }
    
    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        chromosomes.add(new Chromosome(4));
        chromosomes.add(new Chromosome(5));
        
        int populationLimit = 10;
        double elitismRate = 0.9;
        
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        
        assertEquals(populationLimit, nextGeneration.getPopulationLimit());
        assertEquals(elitismRate, nextGeneration.getElitismRate(), 0.001);
        assertEquals(1, nextGeneration.getChromosomes().get(0).getFitness());
        assertEquals(2, nextGeneration.getChromosomes().get(1).getFitness());
        assertEquals(3, nextGeneration.getChromosomes().get(2).getFitness());
        assertEquals(4, nextGeneration.getChromosomes().get(3).getFitness());
        assertEquals(5, nextGeneration.getChromosomes().get(4).getFitness());
        assertEquals(5, nextGeneration.getChromosomes().size());
    }
    
    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfBounds() {
        int populationLimit = 10;
        double elitismRate = -0.1;
        
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
    }
    
    @Test
    public void testGetElitismRate() {
        int populationLimit = 10;
        double elitismRate = 0.9;
        
        ElitisticListPopulation population = new ElitisticListPopulation(populationLimit, elitismRate);
        
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
    }
}