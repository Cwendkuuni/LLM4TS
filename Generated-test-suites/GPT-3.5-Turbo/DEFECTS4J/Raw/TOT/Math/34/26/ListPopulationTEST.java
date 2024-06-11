import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
    
    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(5);
    }
    
    @Test(expected = NotPositiveException.class)
    public void testPopulationLimitNotPositive() {
        new ListPopulation(0);
    }
    
    @Test
    public void testDefaultConstructor() {
        int limit = population.getPopulationLimit();
        Assert.assertEquals(5, limit);
        Assert.assertEquals(0, population.getPopulationSize());
    }
    
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        population.setPopulationLimit(0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 5);
        population.setPopulationLimit(1);
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesBiggerThanPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }
    
    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
    }
    
    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = population.getChromosomes();
        Assert.assertEquals(0, chromosomes.size());
    }
    
    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        Assert.assertEquals(1, population.getPopulationSize());
    }
    
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedPopulationLimit() {
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }
    
    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        Chromosome fittest = population.getFittestChromosome();
        Assert.assertEquals(chromosome, fittest);
    }
    
    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        int count = 0;
        for (Chromosome chromosome : population) {
            if (count == 0) {
                Assert.assertEquals(chromosome1, chromosome);
            } else if (count == 1) {
                Assert.assertEquals(chromosome2, chromosome);
            }
            count++;
        }
        Assert.assertEquals(2, count);
    }
}