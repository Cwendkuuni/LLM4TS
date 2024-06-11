import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        
        listPopulation.setChromosomes(newChromosomes);

        Assert.assertEquals(2, listPopulation.getPopulationSize());
        Assert.assertEquals(newChromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNull() {
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedPopulationLimit() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        listPopulation.setChromosomes(newChromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());

        listPopulation.addChromosomes(newChromosomes);

        Assert.assertEquals(4, listPopulation.getPopulationSize());
        Assert.assertEquals(4, listPopulation.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        listPopulation.addChromosomes(newChromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = listPopulation.getChromosomes();

        Assert.assertEquals(2, chromosomes.size());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);

        Chromosome fittestChromosome = listPopulation.getFittestChromosome();

        Assert.assertEquals(chromosome, fittestChromosome);
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);

        List<Chromosome> chromosomes = listPopulation.getChromosomes();
        Assert.assertTrue(chromosomes.contains(chromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedPopulationLimit() {
        for (int i = 0; i < 5; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
        listPopulation.addChromosome(new Chromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = listPopulation.getPopulationLimit();

        Assert.assertEquals(5, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        listPopulation.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanExistingChromosomes() {
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(10);
        int newPopulationLimit = listPopulation.getPopulationLimit();

        Assert.assertEquals(10, newPopulationLimit);
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = listPopulation.getPopulationSize();

        Assert.assertEquals(2, populationSize);
    }

    @Test
    public void testToString() {
        String populationString = listPopulation.toString();

        Assert.assertTrue(populationString.contains("Chromosome"));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Chromosome chromosome : listPopulation) {
            count++;
        }

        Assert.assertEquals(2, count);
    }
}