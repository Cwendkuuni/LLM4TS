import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(1.0));
        ListPopulation population = new ListPopulation(5);
        population.addChromosomes(chromosomes);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(5, population.getPopulationSize());
        Assert.assertEquals(chromosomes.get(0), population.getChromosomes().get(0));
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationPopulationLimitExceeded() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testListPopulationAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome(1.0);
        population.addChromosome(chromosome);
        Assert.assertEquals(6, population.getPopulationLimit());
        Assert.assertEquals(1, population.getPopulationSize());
        Assert.assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationPopulationLimitExceededAddChromosome() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome(1.0);
        population.addChromosome(chromosome);
        population.addChromosome(new Chromosome(2.0));
    }

    @Test
    public void testListPopulationGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        List<Chromosome> result = population.getChromosomes();
        Assert.assertEquals(10, result.size());
        Assert.assertArrayEquals(chromosomes.toArray(), result.toArray());
    }

    @Test
    public void testListPopulationGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome(1.0));
        population.addChromosome(new Chromosome(2.0));
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.addChromosome(new Chromosome(1.0));
        population.addChromosome(new Chromosome(2.0));
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        Assert.assertEquals("[Chromosome[fitness=0.0, genotype=[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]], Chromosome[fitness=0.0, genotype=[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]], ...]", population.toString());
    }

    @Test
    public void testListPopulationIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        Iterator<Chromosome> iterator = population.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Chromosome next = iterator.next();
            Assert.assertEquals(chromosomes.get(i), next);
            i++;
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListPopulationIteratorRemove() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        Iterator<Chromosome> iterator = population.iterator();
        iterator.remove();
    }

    @Test
    public void testListPopulationAccessChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        List<Chromosome> result = population.getChromosomeList();
        Assert.assertEquals(10, result.size());
        Assert.assertArrayEquals(chromosomes.toArray(), result.toArray());
    }

    @Test
    public void testListPopulationGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome(i));
        }
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(chromosomes);
        Chromosome result = population.getFittestChromosome();
        Chromosome expected = chromosomes.get(0);
        Assert.assertEquals(expected.getFitness(), result.getFitness());
        Assert.assertEquals(expected, result);
    }
}