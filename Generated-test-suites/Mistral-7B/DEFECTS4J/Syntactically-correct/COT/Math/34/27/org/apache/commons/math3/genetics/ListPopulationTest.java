import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(getChromosomeList(3));
        Assert.assertEquals(8, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(getChromosomeList(6));
    }

    @Test
    public void testListPopulationGetChromosomes() {
        ListPopulation population = new ListPopulation(5, getChromosomeList(5));
        List<Chromosome> chromosomes = population.getChromosomes();
        Assert.assertEquals(5, chromosomes.size());
    }

    @Test
    public void testListPopulationGetChromosomeList() {
        ListPopulation population = new ListPopulation(5, getChromosomeList(5));
        List<Chromosome> chromosomes = population.getChromosomeList();
        Assert.assertEquals(5, chromosomes.size());
    }

    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(getChromosomeList(5));
        Assert.assertEquals(5, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesNullArgument() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(getChromosomeList(6));
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(getChromosome());
        Assert.assertEquals(6, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomeExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(getChromosome());
        population.addChromosome(getChromosome());
        population.addChromosome(getChromosome()); // will fail
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3, getChromosomeList(3));
        Chromosome fittest = population.getFittestChromosome();
        Assert.assertNotNull(fittest);
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationSetPopulationLimitNegativeNumber() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testListPopulationSetPopulationLimitSmallerCurrentSize() {
        ListPopulation population = new ListPopulation(5, getChromosomeList(10));
        population.setPopulationLimit(5);
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(5, getChromosomeList(5));
        Assert.assertNotNull(population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        ListPopulation population = new ListPopulation(5, getChromosomeList(5));
        Iterator<Chromosome> it = population.iterator();
        Assert.assertNotNull(it);
        Assert.assertTrue(it.hasNext());
        Chromosome chromosome = it.next();
        Assert.assertNotNull(chromosome);
        Assert.assertFalse(it.hasNext());
    }

    private List<Chromosome> getChromosomeList(int size) {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            chromosomes.add(getChromosome());
        }
        return chromosomes;
    }

    private Chromosome getChromosome() {
        // You should implement this method according to your Chromosome class
        return null;
    }
}