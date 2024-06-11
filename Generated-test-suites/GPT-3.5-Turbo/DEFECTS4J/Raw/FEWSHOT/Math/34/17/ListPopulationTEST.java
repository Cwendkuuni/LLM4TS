import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
    
    private ListPopulation population;
    private MockChromosome chromosome;
    
    @Before
    public void setUp() {
        population = new MockListPopulation(3);
        chromosome = new MockChromosome();
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        population = new MockListPopulation(-1);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithZeroPopulationLimit() {
        population = new MockListPopulation(0);
    }
    
    @Test
    public void testConstructorWithPopulationLimit() {
        assertEquals(3, population.getPopulationLimit());
    }
    
    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        assertEquals(3, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        population = new MockListPopulation(null, 3);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        population = new MockListPopulation(population.getChromosomes(), 2);
    }
    
    @Test
    public void testSetChromosomes() {
        population.setChromosomes(null);
        assertEquals(0, population.getPopulationSize());
        
        population.setChromosomes(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        population.setChromosomes(null);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesExceedingPopulationLimit() {
        population.setChromosomes(population.getChromosomes());
    }
    
    @Test
    public void testAddChromosomes() {
        population.addChromosomes(null);
        assertEquals(0, population.getPopulationSize());
        
        population.addChromosomes(population.getChromosomes());
        assertEquals(0, population.getPopulationSize());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        population.addChromosomes(population.getChromosomes());
    }
    
    @Test
    public void testGetChromosomes() {
        assertEquals(0, population.getChromosomes().size());
        
        population.addChromosome(chromosome);
        assertEquals(1, population.getChromosomes().size());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }
    
    @Test
    public void testAddChromosome() {
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
        
        population.addChromosome(chromosome);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(1));
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingPopulationLimit() {
        population.addChromosome(chromosome);
        population.addChromosome(chromosome);
        population.addChromosome(chromosome);
        population.addChromosome(chromosome);
    }
    
    @Test
    public void testGetFittestChromosome() {
        assertNull(population.getFittestChromosome());
        
        population.addChromosome(chromosome);
        assertEquals(chromosome, population.getFittestChromosome());
        
        population.addChromosome(chromosome);
        assertEquals(chromosome, population.getFittestChromosome());
        
        MockChromosome fittestChromosome = new MockChromosome(10);
        population.addChromosome(fittestChromosome);
        assertEquals(fittestChromosome, population.getFittestChromosome());
    }
    
    @Test
    public void testGetPopulationLimit() {
        assertEquals(3, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeLimit() {
        population.setPopulationLimit(-1);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZeroLimit() {
        population.setPopulationLimit(0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerLimit() {
        population.setPopulationLimit(2);
    }
    
    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
        
        population.setPopulationLimit(3);
        assertEquals(3, population.getPopulationLimit());
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());
        
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }
    
    private class MockListPopulation extends ListPopulation {
        public MockListPopulation(int populationLimit) {
            super(populationLimit);
        }
        
        public MockListPopulation(List<Chromosome> chromosomes, int populationLimit) {
            super(chromosomes, populationLimit);
        }
    }
    
    private class MockChromosome extends Chromosome {
        private double fitness;
        
        public MockChromosome() {
            this.fitness = 0;
        }
        
        public MockChromosome(double fitness) {
            this.fitness = fitness;
        }
        
        @Override
        public double fitness() {
            return fitness;
        }
        
        @Override
        public int compareTo(Chromosome other) {
            return Double.compare(fitness, other.fitness());
        }
    }
}