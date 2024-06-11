import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationEmptyConstructor() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(0, population.getPopulationSize());
        Assert.assertTrue(population.isEmpty());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativeConstructor() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationWithEmptyListConstructor() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        Assert.assertEquals(0, population.getPopulationSize());
        Assert.assertTrue(population.isEmpty());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullConstructor() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativeCapacityConstructor() {
        new ListPopulation(new ArrayList<>(Arrays.asList(new Chromosome[0])), -1);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationLessThanOneCapacityConstructor() {
        new ListPopulation(new ArrayList<>(Arrays.asList(new Chromosome[0])), 0);
    }

    @Test
    public void testListPopulationWithListConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertFalse(population.isEmpty());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullListConstructor() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddMoreThanCapacity() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new Chromosome(1));
        population.addChromosome(new Chromosome(2));
        population.addChromosome(new Chromosome(3));
        population.addChromosome(new Chromosome(4));
        population.addChromosome(new Chromosome(5));
        population.addChromosome(new Chromosome(6));
    }

    @Test
    public void testListPopulationAddChromosomesCollection() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        population.addChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertFalse(population.isEmpty());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomesCollection() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddMoreThanCapacityCollection() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        population.addChromosomes(chromosomes);
        population.addChromosome(new Chromosome(3));
        population.addChromosome(new Chromosome(4));
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new Chromosome(10));
        population.addChromosome(new Chromosome(5));
        population.addChromosome(new Chromosome(2));
        Chromosome fittest = population.getFittestChromosome();
        Assert.assertEquals(10, fittest.getFitnessValue());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitSmallerThanCurrentSize() {
        ListPopulation population = new ListPopulation(new ArrayList<>(), 5);
        population.addChromosome(new Chromosome(1));
        population.addChromosome(new Chromosome(2));
        population.setPopulationLimit(1);
    }
}