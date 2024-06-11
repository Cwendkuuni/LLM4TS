import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
    }

    // Test case for constructor with population limit
    @Test
    public void testConstructorWithPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 3);
        Assert.assertEquals(3, listPopulation.getChromosomes().size());
    }

    // Test case for constructor with invalid population limit (zero)
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimitZero() {
        new ListPopulation(0);
    }

    // Test case for constructor with invalid population limit (negative)
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimitNegative() {
        new ListPopulation(-1);
    }

    // Test case for constructor with chromosome list larger than population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomeListLargerThanPopulationLimit() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        new ListPopulation(chromosomes, 2);
    }

    // Test case for setChromosomes method with valid input
    @Test
    public void testSetChromosomesValidInput() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        Assert.assertEquals(2, listPopulation.getChromosomes().size());
    }

    // Test case for setChromosomes method with null argument
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullArgument() {
        listPopulation.setChromosomes(null);
    }

    // Test case for setChromosomes method with chromosome list larger than population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesChromosomeListLargerThanPopulationLimit() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        listPopulation.setChromosomes(chromosomes);
    }

    // Test case for addChromosomes method with valid input
    @Test
    public void testAddChromosomesValidInput() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        Assert.assertEquals(2, listPopulation.getChromosomes().size());
    }

    // Test case for addChromosomes method with chromosome list larger than population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesChromosomeListLargerThanPopulationLimit() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        listPopulation.addChromosomes(chromosomes);
    }

    // Test case for getChromosomes method
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = listPopulation.getChromosomes();
        Assert.assertEquals(0, chromosomes.size());
    }

    // Test case for getChromosomeList method
    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = listPopulation.getChromosomeList();
        Assert.assertEquals(0, chromosomes.size());
    }

    // Test case for addChromosome method with valid input
    @Test
    public void testAddChromosomeValidInput() {
        listPopulation.addChromosome(new Chromosome());
        Assert.assertEquals(1, listPopulation.getChromosomes().size());
    }

    // Test case for addChromosome method with chromosome list larger than population limit
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeChromosomeListLargerThanPopulationLimit() {
        listPopulation.addChromosomes(Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome()));
        listPopulation.addChromosome(new Chromosome());
    }

    // Test case for getFittestChromosome method
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);
        listPopulation.addChromosomes(Arrays.asList(chromosome1, chromosome2));
        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        Assert.assertEquals(chromosome2, fittestChromosome);
    }

    // Test case for getPopulationLimit method
    @Test
    public void testGetPopulationLimit() {
        int populationLimit = listPopulation.getPopulationLimit();
        Assert.assertEquals(5, populationLimit);
    }

    // Test case for setPopulationLimit method with valid input
    @Test
    public void testSetPopulationLimitValidInput() {
        listPopulation.setPopulationLimit(10);
        Assert.assertEquals(10, listPopulation.getPopulationLimit());
    }

    // Test case for setPopulationLimit method with invalid input (zero)
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalidInputZero() {
        listPopulation.setPopulationLimit(0);
    }

    // Test case for setPopulationLimit method with invalid input (negative)
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalidInputNegative() {
        listPopulation.setPopulationLimit(-1);
    }

    // Test case for setPopulationLimit method with new population limit smaller than current population size
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitNewPopulationLimitSmallerThanCurrentPopulationSize() {
        listPopulation.setPopulationLimit(1);
    }

    // Test case for getPopulationSize method
    @Test
    public void testGetPopulationSize() {
        int populationSize = listPopulation.getPopulationSize();
        Assert.assertEquals(0, populationSize);
    }

    // Test case for toString method
    @Test
    public void testToString() {
        Assert.assertEquals("[]", listPopulation.toString());
    }

    // Test case for iterator method
    @Test
    public void testIterator() {
        Assert.assertFalse(listPopulation.iterator().hasNext());
    }
}