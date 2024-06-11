

// JUnit 4 test cases for ListPopulation class
// Author: [Your Name]

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(3, population.getPopulationSize());
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorInvalidPopulationLimit() {
        new ListPopulation(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorExceedPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 2);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertEquals(chromosomes.get(0), population.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullChromosomes() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        Collection<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        population.addChromosomes(chromosomes);
        Assert.assertEquals(5, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        Collection<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = population.getChromosomes();
        Assert.assertEquals(3, chromosomes.size());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = population.getChromosomeList();
        Assert.assertEquals(3, chromosomes.size());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        Assert.assertEquals(4, population.getPopulationSize());
        Assert.assertEquals(chromosome, population.getChromosomes().get(3));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedPopulationLimit() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = population.getFittestChromosome();
        Assert.assertNotNull(fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitInvalidPopulationLimit() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        population.setPopulationLimit(1);
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        Assert.assertEquals(3, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[Chromosome, Chromosome, Chromosome]", population.toString());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Chromosome chromosome : population) {
            Assert.assertNotNull(chromosome);
            count++;
        }
        Assert.assertEquals(3, count);
    }
}
