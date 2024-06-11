import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulation() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(0, population.getPopulationSize());
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationWithChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertSame(chromosome1, population.getChromosomes().get(0));
        Assert.assertSame(chromosome2, population.getChromosomes().get(1));
        population.addChromosome(new Chromosome());
        Assert.assertEquals(3, population.getPopulationSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        ListPopulation population = new ListPopulation(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulationWithTooManyChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertSame(chromosome1, population.getChromosomes().get(0));
        Assert.assertSame(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetChromosomesWithTooManyChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(1);
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertSame(chromosome1, population.getChromosomes().get(0));
        Assert.assertSame(chromosome2, population.getChromosomes().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChromosomesExceedingLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(2);
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> actualChromosomes = population.getChromosomes();
        Assert.assertEquals(2, actualChromosomes.size());
        Assert.assertSame(chromosome1, actualChromosomes.get(0));
        Assert.assertSame(chromosome2, actualChromosomes.get(1));
    }

    @Test
    public void testGetChromosomeList() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        List<Chromosome> actualChromosomes = population.getChromosomeList();
        Assert.assertSame(chromosome1, actualChromosomes.get(0));
        Assert.assertSame(chromosome2, actualChromosomes.get(1));
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(chromosome);
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertSame(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddChromosomeExceedingLimit() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(20);
        Chromosome chromosome3 = new Chromosome(30);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2, chromosome3);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Chromosome fittestChromosome = population.getFittestChromosome();
        Assert.assertSame(chromosome3, fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPopulationLimitBelowPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(2);
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome(10);
        Chromosome chromosome2 = new Chromosome(20);
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        String expectedString = "[10, 20]";
        Assert.assertEquals(expectedString, population.toString());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        List<Chromosome> chromosomes = Arrays.asList(chromosome1, chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);

        int count = 0;
        for (Chromosome chromosome : population) {
            count++;
        }

        Assert.assertEquals(2, count);
    }
}