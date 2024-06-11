import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        population = new ListPopulation(10);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNull() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            chromosomes.add(new Chromosome());
        }
        population.addChromosomes(chromosomes);
        population.addChromosomes(Collections.singletonList(new Chromosome()));
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomeList());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedLimit() {
        for (int i = 0; i < 10; i++) {
            population.addChromosome(new Chromosome());
        }
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(Collections.singletonList(chromosome), population.getChromosomes());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome(1);
        Chromosome chromosome2 = new Chromosome(2);
        Chromosome chromosome3 = new Chromosome(3);
        Chromosome chromosome4 = new Chromosome(4);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        population.addChromosome(chromosome4);
        assertEquals(chromosome4, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanChromosomes() {
        for (int i = 0; i < 5; i++) {
            population.addChromosome(new Chromosome());
        }
        population.setPopulationLimit(4);
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(15);
        assertEquals(15, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(Collections.singletonList(chromosome).toString(), population.toString());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertNotNull(population.iterator());
        assertEquals(chromosome, population.iterator().next());
    }
}