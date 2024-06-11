import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {
    
    private ListPopulation population;
    
    @Before
    public void setup() {
        population = new ListPopulation(10);
    }
    
    @Test
    public void testConstructor() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testNullChromosomesConstructor() {
        population = new ListPopulation(null, 10);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testNonPositivePopulationLimitConstructor() {
        population = new ListPopulation(0);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testTooManyChromosomesConstructor() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 1);
    }
    
    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testNullChromosomesSet() {
        population.setChromosomes(null);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testTooManyChromosomesSet() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setPopulationLimit(1);
        population.setChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testTooManyChromosomesAdd() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setPopulationLimit(1);
        population.addChromosomes(chromosomes);
    }
    
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomeList());
    }
    
    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(chromosome);
        assertEquals(chromosomes, population.getChromosomes());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testTooManyChromosomesAddChromosome() {
        population.setPopulationLimit(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(30);
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosome3, population.getFittestChromosome());
    }
    
    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testNonPositivePopulationLimit() {
        population.setPopulationLimit(0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallPopulationLimit() {
        population.setChromosomes(Arrays.asList(new Chromosome(), new Chromosome()));
        population.setPopulationLimit(1);
    }
    
    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());
        population.setChromosomes(Arrays.asList(new Chromosome(), new Chromosome()));
        assertEquals(2, population.getPopulationSize());
    }
    
    @Test
    public void testIterator() {
        Iterator<Chromosome> iterator = population.iterator();
        assertFalse(iterator.hasNext());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        iterator = population.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }
}