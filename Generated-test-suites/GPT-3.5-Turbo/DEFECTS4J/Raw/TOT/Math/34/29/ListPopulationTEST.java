import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {

    // Test constructor with positive population limit
    @Test
    public void testConstructorWithPositiveLimit() {
        int populationLimit = 10;
        ListPopulation population = new ListPopulation(populationLimit);
        assertNotNull(population);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    // Test constructor with zero population limit
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithZeroLimit() {
        int populationLimit = 0;
        ListPopulation population = new ListPopulation(populationLimit);
    }

    // Test constructor with negative population limit
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeLimit() {
        int populationLimit = -10;
        ListPopulation population = new ListPopulation(populationLimit);
    }

    // Test constructor with initial chromosomes
    @Test
    public void testConstructorWithChromosomes() {
        int populationLimit = 10;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
        assertNotNull(population);
        assertEquals(populationLimit, population.getPopulationLimit());
        assertEquals(3, population.getPopulationSize());
    }

    // Test constructor with null chromosomes
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        int populationLimit = 10;
        List<Chromosome> chromosomes = null;
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
    }

    // Test constructor with chromosomes exceeding population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithExcessiveChromosomes() {
        int populationLimit = 3;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, populationLimit);
    }

    // Test adding chromosomes to population
    @Test
    public void testAddChromosomes() {
        int populationLimit = 5;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(populationLimit);
        population.addChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());

        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(5, population.getPopulationSize());
    }

    // Test adding excessive chromosomes to population
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddExcessiveChromosomes() {
        int populationLimit = 3;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(populationLimit);
        population.addChromosomes(chromosomes);
    }

    // Test setting chromosomes
    @Test
    public void testSetChromosomes() {
        int populationLimit = 5;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(populationLimit);
        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());

        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(4, population.getPopulationSize());
    }
    
    // Test setting null chromosomes
    @Test(expected = NullArgumentException.class)
    public void testSetNullChromosomes() {
        int populationLimit = 5;
        List<Chromosome> chromosomes = null;
        ListPopulation population = new ListPopulation(populationLimit);
        population.setChromosomes(chromosomes);
    }

    // Test setting excessive chromosomes
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetExcessiveChromosomes() {
        int populationLimit = 3;
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(populationLimit);
        population.setChromosomes(chromosomes);
    }
    
    // Test adding a chromosome to population
    @Test
    public void testAddChromosome() {
        int populationLimit = 5;
        ListPopulation population = new ListPopulation(populationLimit);
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
        
        population.addChromosome(new Chromosome());
        assertEquals(2, population.getPopulationSize());
    }
    
    // Test adding excessive chromosome to population
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddExcessiveChromosome() {
        int populationLimit = 2;
        ListPopulation population = new ListPopulation(populationLimit);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }
    
    // Test getting the fittest chromosome
    @Test
    public void testGetFittestChromosome() {
        int populationLimit = 5;
        ListPopulation population = new ListPopulation(populationLimit);
        
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        chromosome1.setFitness(0.5);
        chromosome2.setFitness(0.8);
        chromosome3.setFitness(0.2);
        
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        
        assertEquals(chromosome2, population.getFittestChromosome());
    }
    
    // Test setting population limit
    @Test
    public void testSetPopulationLimit() {
        int populationLimit = 5;
        int newPopulationLimit = 10;
        ListPopulation population = new ListPopulation(populationLimit);
        
        population.setPopulationLimit(newPopulationLimit);
        assertEquals(newPopulationLimit, population.getPopulationLimit());
    }
    
    // Test setting population limit smaller than current size
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerSize() {
        int populationLimit = 5;
        int newPopulationLimit = 3;
        ListPopulation population = new ListPopulation(populationLimit);
        
        population.setPopulationLimit(newPopulationLimit);
    }
    
    // Test getting population size
    @Test
    public void testGetPopulationSize() {
        int populationLimit = 5;
        ListPopulation population = new ListPopulation(populationLimit);
        
        assertEquals(0, population.getPopulationSize());
        
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
        
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        assertEquals(3, population.getPopulationSize());
    }
    
    // Test toString method
    @Test
    public void testToString() {
        int populationLimit = 5;
        ListPopulation population = new ListPopulation(populationLimit);
        
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        
        String expectedString = "[" + chromosome1.toString() + ", "
                                + chromosome2.toString() + ", "
                                + chromosome3.toString() + "]";
        
        assertEquals(expectedString, population.toString());
    }
    
    // Test iterator
    @Test
    public void testIterator() {
        int populationLimit = 5;
        ListPopulation population = new ListPopulation(populationLimit);
        
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        Chromosome chromosome3 = new Chromosome();
        
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        
        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
            assertNotNull(chromosome);
        }
        
        assertEquals(3, count);
    }

}