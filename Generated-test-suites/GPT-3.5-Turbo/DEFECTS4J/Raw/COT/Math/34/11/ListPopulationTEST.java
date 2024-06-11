import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;

public class ListPopulationTest {
    
    @Test
    public void testConstructor() {
        ListPopulation population = new ListPopulation(100);
        assertEquals(0, population.getPopulationSize());
        assertEquals(100, population.getPopulationLimit());
        assertTrue(population.getChromosomes().isEmpty());
    }
    
    @Test
    public void testConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        assertEquals(2, population.getPopulationSize());
        assertEquals(100, population.getPopulationLimit());
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithZeroPopulationLimit() {
        new ListPopulation(0);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(-100);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 100);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            chromosomes.add(new Chromosome());
        }
        new ListPopulation(chromosomes, 100);
    }
    
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(100);
        population.setChromosomes(chromosomes);
        
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(100);
        population.setChromosomes(null);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            chromosomes.add(new Chromosome());
        }
        
        ListPopulation population = new ListPopulation(100);
        population.setChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(100);
        population.addChromosomes(chromosomes);
        
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            chromosomes.add(new Chromosome());
        }
        
        ListPopulation population = new ListPopulation(100);
        population.addChromosomes(chromosomes);
    }
    
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        
        assertEquals(chromosome2, population.getFittestChromosome());
    }
    
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(100);
        assertEquals(100, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZero() {
        ListPopulation population = new ListPopulation(100);
        population.setPopulationLimit(0);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegative() {
        ListPopulation population = new ListPopulation(100);
        population.setPopulationLimit(-100);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitBelowCurrentPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        population.setPopulationLimit(1);
    }
    
    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        
        ListPopulation population = new ListPopulation(chromosomes, 100);
        assertEquals(chromosomes.toString(), population.toString());
    }
    
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        ListPopulation population = new ListPopulation(100);
        population.iterator().remove();
    }

}