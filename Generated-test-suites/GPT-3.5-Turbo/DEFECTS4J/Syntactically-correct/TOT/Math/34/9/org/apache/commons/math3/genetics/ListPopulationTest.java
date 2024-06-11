import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.ListPopulation;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(10);
    }

    @Test
    public void testConstructor_MaximumPopulationLimit_PositiveLimit_Success() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test (expected = NotPositiveException.class)
    public void testConstructor_NegativePopulationLimit_ExceptionThrown() {
        population = new ListPopulation(-10);
    }

    @Test (expected = NotPositiveException.class)
    public void testConstructor_ZeroPopulationLimit_ExceptionThrown() {
        population = new ListPopulation(0);
    }

    @Test
    public void testConstructor_EmptyChromosomes_Success() {
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testConstructor_ValidChromosomes_Success() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population = new ListPopulation(chromosomes, 10);
        assertEquals(chromosomes, population.getChromosomeList());
    }

    @Test (expected = NullArgumentException.class)
    public void testConstructor_NullChromosomes_ExceptionThrown() {
        population = new ListPopulation(null, 10);
    }

    @Test (expected = NumberIsTooLargeException.class)
    public void testConstructor_InvalidChromosomes_ExceptionThrown() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population = new ListPopulation(chromosomes, 1);
    }

    @Test (expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_ExceedingPopulationLimit_ExceptionThrown() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes_ValidChromosomes_Success() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.addChromosomes(chromosomes);
        assertTrue(population.getChromosomes().contains(chromosome1));
        assertTrue(population.getChromosomes().contains(chromosome2));
    }

    @Test
    public void testGetChromosomes_UnmodifiableList_Success() {
        population.addChromosome(new Chromosome());
        List<Chromosome> chromosomes = population.getChromosomes();
        try {
            chromosomes.add(new Chromosome());
            fail("UnsupportedOperationException not thrown.");
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testSetChromosomes_ValidChromosomes_Success() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        population.setChromosomes(chromosomes);
        assertTrue(population.getChromosomes().contains(chromosome1));
        assertTrue(population.getChromosomes().contains(chromosome2));
    }

    @Test (expected = NullArgumentException.class)
    public void testSetChromosomes_NullChromosomes_ExceptionThrown() {
         population.setChromosomes(null);
    }

    @Test (expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_InvalidChromosomes_ExceptionThrown() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        population.setChromosomes(chromosomes);
    }

    @Test (expected = NumberIsTooLargeException.class)
    public void testAddChromosome_ExceedingPopulationLimit_ExceptionThrown() {
        for (int i = 0; i < 11; i++) {
            population.addChromosome(new Chromosome());
        }
    }

    @Test
    public void testAddChromosome_ValidChromosome_Success() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test
    public void testGetFittestChromosome_ValidChromosomes_Success() {
        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(15);
        Chromosome chromosome3 = new Chromosome(20);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        assertEquals(chromosome3, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit_ValidLimit_Success() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test (expected = NotPositiveException.class)
    public void testSetPopulationLimit_NegativeLimit_ExceptionThrown() {
        population.setPopulationLimit(-10);
    }

    @Test (expected = NotPositiveException.class)
    public void testSetPopulationLimit_ZeroLimit_ExceptionThrown() {
        population.setPopulationLimit(0);
    }

    @Test (expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_SmallerThanCurrentSize_ExceptionThrown() {
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(0);
    }

    @Test
    public void testSetPopulationLimit_ValidLimit_Success() {
        population.setPopulationLimit(15);
        assertEquals(15, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize_ValidSize_Success() {
        population.addChromosome(new Chromosome());
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testIterator_IteratesOverChromosomes_Success() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        population.setChromosomes(chromosomes);
        int count = 0;
        for (Chromosome chromosome : population) {
            assertTrue(chromosomes.contains(chromosome));
            count++;
        }
        assertEquals(chromosomes.size(), count);
    }

    @Test
    public void testToString_ValidToString_Success() {
        population.addChromosome(new Chromosome(10));
        population.addChromosome(new Chromosome(15));
        assertEquals("[10, 15]", population.toString());
    }
}