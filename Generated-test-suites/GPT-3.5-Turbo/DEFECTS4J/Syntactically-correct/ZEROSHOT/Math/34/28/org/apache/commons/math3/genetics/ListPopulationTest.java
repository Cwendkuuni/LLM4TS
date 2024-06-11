import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.ListPopulation;

public class ListPopulationTest {
    
    @Test
    public void testListPopulationConstructorEmptyList() {
        ListPopulation listPopulation = new ListPopulation(10);
        Assert.assertEquals(Collections.<Chromosome>emptyList(), listPopulation.getChromosomes());
        Assert.assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testListPopulationConstructor() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        Assert.assertEquals(chromosomes, listPopulation.getChromosomes());
        Assert.assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorNegativePopulationLimit() {
        new ListPopulation(10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorExceedsPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedsPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(3);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        Assert.assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedsPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        Assert.assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        Assert.assertEquals(chromosomes, listPopulation.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(3);
        listPopulation.addChromosome(new Chromosome());
        Assert.assertEquals(1, listPopulation.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedsPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(1);
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation listPopulation = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(50);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(100);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(75);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        listPopulation.addChromosome(chromosome3);
        Assert.assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(10);
        Assert.assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(3);
        listPopulation.setPopulationLimit(5);
        Assert.assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegativePopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(3);
        listPopulation.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitLowerThanCurrentPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(3);
        listPopulation.addChromosome(new Chromosome());
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation listPopulation = new ListPopulation(3);
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(new Chromosome());
        Assert.assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        Assert.assertEquals(chromosomes.toString(), listPopulation.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        Assert.assertEquals(chromosomes.iterator(), listPopulation.iterator());
    }
}