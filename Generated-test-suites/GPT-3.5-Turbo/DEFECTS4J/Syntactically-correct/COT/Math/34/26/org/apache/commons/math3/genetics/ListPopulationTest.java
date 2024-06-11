import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation population;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        population = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testConstructor() {
        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
        assertEquals(chromosome, population.getChromosomes().get(0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedLimit() {
        for (int i = 0; i < 11; i++) {
            Chromosome chromosome = new Chromosome();
            population.addChromosome(chromosome);
        }
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        chromosome1.setFitness(0.5);
        chromosome2.setFitness(0.8);

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetFittestChromosomeEmptyPopulation() {
        assertNull(population.getFittestChromosome());
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());

        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegativeValue() {
        population.setPopulationLimit(-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(0);
    }

    @Test
    public void testGetPopulationSize() {
        for (int i = 0; i < 5; i++) {
            population.addChromosome(new Chromosome());
        }
        assertEquals(5, population.getPopulationSize());
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullChromosomes() {
        population = new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        population = new ListPopulation(0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorExceedPopulationLimit() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            chromosomes.add(new Chromosome());
        }
        population = new ListPopulation(chromosomes, 10);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesNullList() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesExceedPopulationLimit() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            chromosomes.add(new Chromosome());
        }
        population.setChromosomes(chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedPopulationLimit() {
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chromosomeColl.add(new Chromosome());
        }

        population.addChromosomes(chromosomeColl);
        population.addChromosomes(chromosomeColl); // Try to add another 5 chromosomes, exceeding the population limit
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Iterator<Chromosome> iterator = population.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome();
        Chromosome chromosome2 = new Chromosome();

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        String expectedResult = "[" + chromosome1.toString() + ", " + chromosome2.toString() + "]";
        assertEquals(expectedResult, population.toString());
    }

}