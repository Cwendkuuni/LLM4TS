import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exepction.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorWithChromosomes() {
        // Typical use case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(chromosomes.size(), population.getChromosomes().size());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
        
        // Edge case: empty chromosome list
        chromosomes = new ArrayList<>();
        population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        assertEquals(chromosomes.size(), population.getChromosomes().size());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }
    
    @Test
    public void testElitisticListPopulationConstructorWithoutChromosomes() {
        // Typical use case
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.9);
        assertEquals(0, population.getChromosomes().size());
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0.9, population.getElitismRate(), 0.001);
    }
    
    @Test(expected=OutOfRangeException.class)
    public void testElitisticListPopulationConstructorOutOfRangeElitismRate() {
        // Error scenario: elitism rate outside the [0, 1] range
        ElitisticListPopulation population = new ElitisticListPopulation(10, 1.1);
    }
    
    @Test
    public void testNextGeneration() {
        // Typical use case
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        chromosomes.add(new Chromosome(3));
        chromosomes.add(new Chromosome(4));
        chromosomes.add(new Chromosome(5));
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 5, 0.9);
        
        Population nextGeneration = population.nextGeneration();
        assertEquals(1, nextGeneration.getChromosomes().size());
        assertEquals(5, nextGeneration.getPopulationLimit());
        assertEquals(0.9, nextGeneration.getElitismRate(), 0.001);
        
        // Edge case: no chromosomes added to next generation
        chromosomes = new ArrayList<>();
        population = new ElitisticListPopulation(chromosomes, 5, 0.3);
        
        nextGeneration = population.nextGeneration();
        assertEquals(0, nextGeneration.getChromosomes().size());
        assertEquals(5, nextGeneration.getPopulationLimit());
        assertEquals(0.3, nextGeneration.getElitismRate(), 0.001);
    }
    
    @Test
    public void testSetElitismRate() {
        // Typical use case
        ElitisticListPopulation population = new ElitisticListPopulation(10, 0.5);
        population.setElitismRate(0.7);
        assertEquals(0.7, population.getElitismRate(), 0.001);
        
        // Error scenario: elitism rate outside the [0, 1] range
        try {
            population.setElitismRate(1.2);
            fail("Expected OutOfRangeException was not thrown.");
        } catch (OutOfRangeException e) {
            // check exception message
            assertEquals("Elitism rate is out of range: 1.2. Expected value to be within range [0, 1].", e.getMessage());
        }
    }
}