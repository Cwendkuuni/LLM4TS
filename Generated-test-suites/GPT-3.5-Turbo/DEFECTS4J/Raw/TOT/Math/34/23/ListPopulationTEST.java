import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private ListPopulation listPopulation;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        listPopulation = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testConstructor_ValidInput() {
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_ExceedsPopulationLimit() {
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, chromosomes.size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_ChromosomesBiggerThanPopulationLimit() {
        for (int i = 0; i < 11; i++) {
            chromosomes.add(new Chromosome());
        }
        listPopulation = new ListPopulation(chromosomes, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_ExceedsPopulationLimit() {
        List<Chromosome> additionalChromosomes = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            additionalChromosomes.add(new Chromosome());
        }
        listPopulation.addChromosomes(additionalChromosomes);
    }

    @Test
    public void testAddChromosomes_ValidInput() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome1, listPopulation.getChromosomes().get(0));
        assertEquals(chromosome2, listPopulation.getChromosomes().get(1));
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testSetChromosomes_ValidInput() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosome1, listPopulation.getChromosomes().get(0));
        assertEquals(chromosome2, listPopulation.getChromosomes().get(1));
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_ExceedsPopulationLimit() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome1, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        expectedChromosomes.add(chromosome1);
        expectedChromosomes.add(chromosome2);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(expectedChromosomes, listPopulation.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        expectedChromosomes.add(chromosome1);
        expectedChromosomes.add(chromosome2);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(expectedChromosomes, listPopulation.getChromosomeList());
    }

    @Test
    public void testAddChromosome_ValidInput() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(chromosome, listPopulation.getChromosomes().get(0));
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_ExceedsPopulationLimit() {
        for (int i = 0; i < 11; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_NotPositiveNumber() {
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_SmallerThanCurrentSize() {
        for (int i = 0; i < 5; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
        listPopulation.setPopulationLimit(4);
    }

    @Test
    public void testSetPopulationLimit_ValidInput() {
        listPopulation.setPopulationLimit(20);
        assertEquals(20, listPopulation.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize_EmptyPopulation() {
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testGetPopulationSize_NonEmptyPopulation() {
        for (int i = 0; i < 5; i++) {
            listPopulation.addChromosome(new Chromosome());
        }
        assertEquals(5, listPopulation.getPopulationSize());
    }

    @Test
    public void testIterator() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        listPopulation.setChromosomes(chromosomes);
        int count = 0;
        for (Chromosome chromosome : listPopulation) {
            if (count == 0) {
                assertEquals(chromosome1, chromosome);
            } else {
                assertEquals(chromosome2, chromosome);
            }
            count++;
        }
    }
}