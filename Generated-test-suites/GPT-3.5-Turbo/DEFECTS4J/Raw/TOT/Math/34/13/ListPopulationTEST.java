import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {

    /**
     * Test the constructor with a positive population limit.
     * This is a typical use case.
     */
    @Test
    public void testConstructorPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test the constructor with a negative population limit.
     * This is an error scenario.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    /**
     * Test the constructor with zero population limit.
     * This is an error scenario.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorZeroPopulationLimit() {
        ListPopulation population = new ListPopulation(0);
    }

    /**
     * Test the constructor with a list of chromosomes that exceeds the population limit.
     * This is an error scenario.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorChromosomesExceedPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 1);
    }

    /**
     * Test the constructor with a null list of chromosomes.
     * This is an error scenario.
     */
    @Test(expected = NullArgumentException.class)
    public void testConstructorNullChromosomes() {
        ListPopulation population = new ListPopulation(null, 10);
    }

    /**
     * Test the setChromosomes method with a list of chromosomes that exceeds the population limit.
     * This is an error scenario.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    /**
     * Test the setChromosomes method with a null list of chromosomes.
     * This is an error scenario.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    /**
     * Test the addChromosomes method with a list of chromosomes that exceeds the population limit.
     * This is an error scenario.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
    }

    /**
     * Test the addChromosomes method with a null list of chromosomes.
     * This is an error scenario.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    /**
     * Test the addChromosome method with a population that is already at its limit.
     * This is an error scenario.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomePopulationLimit() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    /**
     * Test the getFittestChromosome method in a population with a single chromosome.
     * This is a typical use case.
     */
    @Test
    public void testGetFittestChromosomeSingleChromosome() {
        ListPopulation population = new ListPopulation(1);
        Chromosome c = new Chromosome();
        population.addChromosome(c);
        assertEquals(c, population.getFittestChromosome());
    }

    /**
     * Test the getFittestChromosome method in a population with multiple chromosomes.
     * This is a typical use case.
     */
    @Test
    public void testGetFittestChromosomeMultipleChromosomes() {
        ListPopulation population = new ListPopulation(3);
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();
        c2.setFitness(10);
        c3.setFitness(5);
        population.addChromosome(c1);
        population.addChromosome(c2);
        population.addChromosome(c3);
        assertEquals(c2, population.getFittestChromosome());
    }

    /**
     * Test the setPopulationLimit method with a positive population limit.
     * This is a typical use case.
     */
    @Test
    public void testSetPopulationLimitPositive() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    /**
     * Test the setPopulationLimit method with a negative population limit.
     * This is an error scenario.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    /**
     * Test the setPopulationLimit method with the current population size larger than the new limit.
     * This is an error scenario.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitTooSmall() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(1);
    }

    /**
     * Test the iterator method with an empty population.
     * This is a typical use case.
     */
    @Test
    public void testIteratorEmptyPopulation() {
        ListPopulation population = new ListPopulation(5);
        assertFalse(population.iterator().hasNext());
    }

    /**
     * Test the iterator method with a non-empty population.
     * This is a typical use case.
     */
    @Test
    public void testIteratorNonEmptyPopulation() {
        ListPopulation population = new ListPopulation(5);
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        population.addChromosome(c1);
        population.addChromosome(c2);
        assertTrue(population.iterator().hasNext());
        assertEquals(c1, population.iterator().next());
        assertTrue(population.iterator().hasNext());
        assertEquals(c2, population.iterator().next());
        assertFalse(population.iterator().hasNext());
    }

    /**
     * Test the toString method with an empty population.
     * This is a typical use case.
     */
    @Test
    public void testToStringEmptyPopulation() {
        ListPopulation population = new ListPopulation(1);
        assertEquals("[]", population.toString());
    }

    /**
     * Test the toString method with a non-empty population.
     * This is a typical use case.
     */
    @Test
    public void testToStringNonEmptyPopulation() {
        ListPopulation population = new ListPopulation(3);
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        population.addChromosome(c1);
        population.addChromosome(c2);
        assertEquals("[" + c1.toString() + ", " + c2.toString() + "]", population.toString());
    }

}