import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(5);
    }

    @Test
    public void testConstructorWithPopulationLimit() {
        assertEquals(5, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testConstructorWithChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, 5);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        listPopulation = new ListPopulation(0);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithPopulationLimitExceeded() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        c2.fitness = 10;
        listPopulation.addChromosome(c1);
        listPopulation.addChromosome(c2);
        assertEquals(c2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(10);
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation = new ListPopulation(chromosomes, 5);
        listPopulation.setPopulationLimit(4);
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimitExceeded() {
        for (int i = 0; i < 6; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation = new ListPopulation(chromosomes, 5);
        int count = 0;
        for (Chromosome chromosome : listPopulation) {
            count++;
        }
        assertEquals(5, count);
    }

}