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
import java.util.Iterator;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructionWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertTrue(population.getChromosomes().isEmpty());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructionWithNegativePopulationLimit() {
        new ListPopulation(-5);
    }

    @Test
    public void testListPopulationConstructionWithEmptyChromosomesList() {
        List<Chromosome> chromosomes = Collections.emptyList();
        ListPopulation population = new ListPopulation(chromosomes, 5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructionWithNullChromosomesList() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructionWithChromosomesListBiggerThanPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(new byte[0]));
        chromosomes.add(new Chromosome(new byte[0]));
        ListPopulation population = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Collections.singletonList(new Chromosome(new byte[0])));
        Assert.assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationAddChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(Collections.singletonList(new Chromosome(new byte[0])));
        population.addChromosomes(Collections.singletonList(new Chromosome(new byte[0])));
    }

    @Test
    public void testListPopulationSetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = Collections.singletonList(new Chromosome(new byte[0]));
        population.setChromosomes(chromosomes);
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationSetChromosomesBiggerThanPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(new byte[0]));
        chromosomes.add(new Chromosome(new byte[0]));
        population.setChromosomes(chromosomes);
    }
}