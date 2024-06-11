
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = mock(ListPopulation.class);
    }

    @Test
    public void testConstructorWithPopulationLimit() {
        int populationLimit = 10;
        listPopulation = new ListPopulation(populationLimit);
        Assert.assertEquals(populationLimit, listPopulation.getPopulationLimit());
        Assert.assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        int populationLimit = 10;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, populationLimit);
        Assert.assertEquals(populationLimit, listPopulation.getPopulationLimit());
        Assert.assertEquals(chromosomes.size(), listPopulation.getPopulationSize());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullChromosomes() {
        int populationLimit = 10;
        listPopulation = new ListPopulation(null, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        int populationLimit = -10;
        listPopulation = new ListPopulation(populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithZeroPopulationLimit() {
        int populationLimit = 0;
        listPopulation = new ListPopulation(populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimitAndChromosomes() {
        int populationLimit = -10;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithZeroPopulationLimitAndChromosomes() {
        int populationLimit = 0;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        int populationLimit = 2;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes.size(), listPopulation.getPopulationSize());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullChromosomes() {
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedingPopulationLimit() {
        int populationLimit = 2;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setPopulationLimit(populationLimit);
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        Assert.assertEquals(chromosomes.size(), listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        int populationLimit = 2;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setPopulationLimit(populationLimit);
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosome1.setFitness(5);
        chromosome2.setFitness(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation.setChromosomes(chromosomes);
        Assert.assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 10;
        listPopulation.setPopulationLimit(populationLimit);
        Assert.assertEquals(populationLimit, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetNegativePopulationLimit() {
        int populationLimit = -10;
        listPopulation.setPopulationLimit(populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetZeroPopulationLimit() {
        int populationLimit = 0;
        listPopulation.setPopulationLimit(populationLimit);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanChromosomes() {
        int populationLimit = 1;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        listPopulation.setPopulationLimit(populationLimit);
    }

    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes.size(), listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes.toString(), listPopulation.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes.iterator(), listPopulation.iterator());
    }
}
