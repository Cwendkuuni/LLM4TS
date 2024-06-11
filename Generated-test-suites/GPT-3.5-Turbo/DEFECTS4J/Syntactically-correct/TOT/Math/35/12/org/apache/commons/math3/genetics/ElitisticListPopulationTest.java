
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ElitisticListPopulationTest {
    
    private List<Chromosome> chromosomes;
    private ElitisticListPopulation population;
    
    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ElitisticListPopulation(chromosomes, 5, 0.5);
    }
    
    @Test
    public void testElitisticListPopulationConstructor() {
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0.5, population.getElitismRate(), 0.0001);
    }
    
    @Test
    public void testElitisticListPopulationConstructorWithOutOfRangeElitismRate() {
        try {
            ElitisticListPopulation population = new ElitisticListPopulation(5, -0.5);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            assertEquals(LocalizedFormats.ELITISM_RATE, e.getSpecifier());
        }
        
        try {
            ElitisticListPopulation population = new ElitisticListPopulation(5, 1.5);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            assertEquals(LocalizedFormats.ELITISM_RATE, e.getSpecifier());
        }
    }
    
    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();
        assertEquals(2, nextGeneration.getChromosomes().size());
        
        List<Chromosome> oldChromosomes = population.getChromosomes();
        assertEquals(3, oldChromosomes.size());
        
        int boundIndex = (int) (Math.ceil((1.0 - population.getElitismRate()) * oldChromosomes.size()));
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            assertTrue(nextGeneration.getChromosomes().contains(oldChromosomes.get(i)));
        }
    }
    
    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.0001);
        
        try {
            population.setElitismRate(-0.5);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            assertEquals(LocalizedFormats.ELITISM_RATE, e.getSpecifier());
        }
        
        try {
            population.setElitismRate(1.5);
            fail("Exception not thrown");
        } catch (OutOfRangeException e) {
            assertEquals(LocalizedFormats.ELITISM_RATE, e.getSpecifier());
        }
    }
}

