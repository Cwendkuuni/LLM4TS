import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(3, chromosomes.size());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals(3, chromosomes.size());
    }

    @Test
    public void testAddChromosome() {
        Chromosome newChromosome = new Chromosome();
        population.addChromosome(newChromosome);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals(4, chromosomes.size());
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
    public void testAddChromosomesWithinPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.addChromosomes(chromosomes);
        List<Chromosome> updatedChromosomes = population.getChromosomes();
        assertEquals(5, updatedChromosomes.size());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNotPositive() {
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        population.setPopulationLimit(2);
    }

    @Test
    public void testSetPopulationLimitLargerThanCurrentSize() {
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(3, population.getPopulationSize());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertNotNull(fittestChromosome);
    }

}