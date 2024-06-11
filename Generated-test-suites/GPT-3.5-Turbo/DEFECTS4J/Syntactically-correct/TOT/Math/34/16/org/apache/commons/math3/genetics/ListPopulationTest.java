import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPopulationTest {

    // Test case for constructor with population limit
    @Test
    public void testConstructorWithPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    // Test case for constructor with chromosomes and population limit
    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome1, chromosome2));
        ListPopulation population = new ListPopulation(chromosomes, 10);
        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    // Test case for constructor with null chromosomes
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }

    // Test case for constructor with negative population limit
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(0);
    }

    // Test case for constructor with chromosomes exceeding population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome1, chromosome2));
        new ListPopulation(chromosomes, 1);
    }

    // Test case for setChromosomes method
    @Test
    public void testSetChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome1, chromosome2));
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    // Test case for setChromosomes method with null chromosomes
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    // Test case for setChromosomes method with chromosomes exceeding population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesExceedingPopulationLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome1, chromosome2));
        ListPopulation population = new ListPopulation(1);
        population.setChromosomes(chromosomes);
    }

    // Test case for addChromosomes method
    @Test
    public void testAddChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome1, chromosome2));
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    // Test case for addChromosomes method when populationLimit is exceeded
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWhenPopulationLimitExceeded() {
        Chromosome chromosome = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome));
        ListPopulation population = new ListPopulation(1);
        population.addChromosomes(chromosomes);
    }

    // Test case for getFittestChromosome method
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(10);
        chromosome1.setFitness(100);
        Chromosome chromosome2 = new Chromosome(10);
        chromosome2.setFitness(200);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome1, chromosome2));
        ListPopulation population = new ListPopulation(chromosomes, 10);
        Chromosome fittest = population.getFittestChromosome();
        assertEquals(chromosome2, fittest);
    }

    // Test case for getPopulationLimit method
    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(10, population.getPopulationLimit());
    }

    // Test case for setPopulationLimit method
    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(20);
        assertEquals(20, population.getPopulationLimit());
    }

    // Test case for setPopulationLimit method with negative value
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeValue() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-1);
    }

    // Test case for setPopulationLimit method with new limit smaller than current population size
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithNewLimitSmallerThanCurrentPopulationSize() {
        Chromosome chromosome = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome));
        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.setPopulationLimit(1);
    }

    // Test case for getPopulationSize method
    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
    }

    // Test case for iterator method
    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = new ArrayList<Chromosome>(Arrays.asList(chromosome1, chromosome2));
        ListPopulation population = new ListPopulation(chromosomes, 10);
        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
        }
        assertEquals(2, count);
    }

}