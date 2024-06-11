import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testListPopulationWithValidChromosomes() {
        Assert.assertEquals(2, listPopulation.getPopulationSize());
        Assert.assertEquals(5, listPopulation.getPopulationLimit());
        Assert.assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        List<Chromosome> chromosomes = null;
        listPopulation = new ListPopulation(chromosomes, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithZeroPopulationLimit() {
        listPopulation = new ListPopulation(0);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithNegativePopulationLimit() {
        listPopulation = new ListPopulation(-5);
    }

    @Test
    public void testSetChromosomesWithValidChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        listPopulation.setChromosomes(newChromosomes);

        Assert.assertEquals(2, listPopulation.getPopulationSize());
        Assert.assertEquals(5, listPopulation.getPopulationLimit());
        Assert.assertEquals(newChromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        List<Chromosome> chromosomes = null;
        listPopulation.setChromosomes(chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithExceedingPopulationLimit() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        listPopulation.setChromosomes(newChromosomes);
    }

    @Test
    public void testAddChromosomesWithValidChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        listPopulation.addChromosomes(newChromosomes);

        Assert.assertEquals(4, listPopulation.getPopulationSize());
        Assert.assertEquals(5, listPopulation.getPopulationLimit());
        Assert.assertEquals(chromosomes + newChromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithExceedingPopulationLimit() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        listPopulation.addChromosomes(newChromosomes);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        Assert.assertNotNull(fittestChromosome);
        Assert.assertEquals(Chromosome, fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        Assert.assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitWithValidPopulationLimit() {
        listPopulation.setPopulationLimit(10);
        Assert.assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZeroPopulationLimit() {
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        listPopulation.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallPopulationLimit() {
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        Assert.assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testIterator() {
        Iterator<Chromosome> iterator = listPopulation.iterator();
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.hasNext());
    }
}