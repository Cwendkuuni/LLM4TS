import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private List<Chromosome> emptyChromosomes;
    private List<Chromosome> populatedChromosomes;
    private List<Chromosome> overLimitChromosomes;
    private List<Chromosome> moreChromosomes;
    private List<Chromosome> lessChromosomes;
    private ListPopulation population;

    @Before
    public void setUp() {
        emptyChromosomes = new ArrayList<Chromosome>();
        populatedChromosomes = Arrays.asList(new Chromosome());
        overLimitChromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());  // More chromosomes than population limit
        moreChromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        lessChromosomes = Arrays.asList(new Chromosome());
        population = new ListPopulation(2);
    }

    // Test initialization with empty chromosomes
    @Test
    public void testInitWithEmptyChromosomes() {
        ListPopulation emptyPopulation = new ListPopulation(emptyChromosomes, 1);
        assertEquals(0, emptyPopulation.getPopulationSize());
    }

    // Test initialization with populated chromosomes
    @Test
    public void testInitWithPopulatedChromosomes() {
        ListPopulation populatedPopulation = new ListPopulation(populatedChromosomes, 1);
        assertEquals(1, populatedPopulation.getPopulationSize());
    }

    // Test initialization with more chromosomes than population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testInitWithOverLimitChromosomes() {
        new ListPopulation(overLimitChromosomes, 1);
    }

    // Test setting chromosomes with empty list
    @Test
    public void testSetChromosomesWithEmptyList() {
        population.setChromosomes(emptyChromosomes);
        assertEquals(0, population.getPopulationSize());
    }

    // Test setting chromosomes with more chromosomes than population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithMoreChromosomes() {
        population.setChromosomes(overLimitChromosomes);
    }

    // Test adding chromosomes with more chromosomes than population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithMoreChromosomes() {
        population.addChromosomes(overLimitChromosomes);
    }

    // Test adding chromosomes with less chromosomes than population limit
    @Test
    public void testAddChromosomesWithLessChromosomes() {
        population.addChromosomes(lessChromosomes);
        assertEquals(1, population.getPopulationSize());
    }

    // Test getting unmodifiable list of chromosomes
    @Test
    public void testGetChromosomes() {
        population.setChromosomes(moreChromosomes);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertNotSame(moreChromosomes, chromosomes);
        assertEquals(moreChromosomes, chromosomes);
    }

    // Test getting fittest chromosome
    @Test
    public void testGetFittestChromosome() {
        Chromosome fittest = new Chromosome();
        population.setChromosomes(Arrays.asList(fittest, new Chromosome()));
        assertEquals(fittest, population.getFittestChromosome());
    }

    // Test setting population limit with negative number
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeNumber() {
        population.setPopulationLimit(-1);
    }

    // Test setting population limit smaller than current population size
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        population.setChromosomes(Arrays.asList(new Chromosome(), new Chromosome()));
        population.setPopulationLimit(1);
    }

    // Test setting population limit equal to current population size
    @Test
    public void testSetPopulationLimitEqualToCurrentSize() {
        population.setChromosomes(Arrays.asList(new Chromosome()));
        population.setPopulationLimit(1);
        assertEquals(1, population.getPopulationSize());
    }

    // Test adding chromosome to full population
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeToFullPopulation() {
        population.setChromosomes(Arrays.asList(new Chromosome(), new Chromosome()));
        population.addChromosome(new Chromosome());
    }

    // Test iterator
    @Test
    public void testIterator() {
        population.setChromosomes(moreChromosomes);
        List<Chromosome> result = new ArrayList<>();
        for (Chromosome chromosome : population) {
            result.add(chromosome);
        }
        assertEquals(moreChromosomes, result);
    }

    // Test toString
    @Test
    public void testToString() {
        population.setChromosomes(moreChromosomes);
        String expected = moreChromosomes.toString();
        assertEquals(expected, population.toString());
    }

}