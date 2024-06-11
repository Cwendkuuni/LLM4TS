
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
    private static final int POPULATION_LIMIT = 10;

    private ListPopulation population;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, POPULATION_LIMIT);
    }

    @Test
    public void testConstructor() {
        assertNotNull(population);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> actualChromosomes = population.getChromosomes();
        assertEquals(chromosomes, actualChromosomes);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome expectedFittest = chromosomes.get(0);
        Chromosome actualFittest = population.getFittestChromosome();
        assertEquals(expectedFittest, actualFittest);
    }

    @Test
    public void testGetPopulationLimit() {
        int actualPopulationLimit = population.getPopulationLimit();
        assertEquals(POPULATION_LIMIT, actualPopulationLimit);
    }

    @Test
    public void testSetPopulationLimit() {
        int newPopulationLimit = 20;
        population.setPopulationLimit(newPopulationLimit);
        int actualPopulationLimit = population.getPopulationLimit();
        assertEquals(newPopulationLimit, actualPopulationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetPopulationLimitWithInvalidLimit() {
        population.setPopulationLimit(-1);
    }

    @Test
    public void testGetPopulationSize() {
        int expectedPopulationSize = chromosomes.size();
        int actualPopulationSize = population.getPopulationSize();
        assertEquals(expectedPopulationSize, actualPopulationSize);
    }

    @Test
    public void testAddChromosome() {
        Chromosome newChromosome = new Chromosome();
        population.addChromosome(newChromosome);
        int expectedPopulationSize = chromosomes.size() + 1;
        int actualPopulationSize = population.getPopulationSize();
        assertEquals(expectedPopulationSize, actualPopulationSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithExceededLimit() {
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        population.addChromosomes(newChromosomes);
    }
}
