import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.genetics.*;

public class ListPopulationTest {

    @Test
    public void testConstructor1() {
        ListPopulation pop = new ListPopulation(10);
        assertEquals(0, pop.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor1WithNegativeLimit() {
        ListPopulation pop = new ListPopulation(-1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor1WithZeroLimit() {
        ListPopulation pop = new ListPopulation(0);
    }

    @Test
    public void testConstructor2() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(chromosomes, 5);

        assertEquals(3, pop.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor2WithNullChromosomes() {
        ListPopulation pop = new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor2WithNegativeLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(chromosomes, -1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor2WithZeroLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(chromosomes, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor2WithExceededLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(chromosomes, 2);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(5);
        pop.setChromosomes(chromosomes);

        assertEquals(3, pop.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation pop = new ListPopulation(5);
        pop.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithExceededLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(2);
        pop.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(5);
        pop.addChromosomes(chromosomes);

        assertEquals(3, pop.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithExceededLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(2);
        pop.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation pop = new ListPopulation(5);
        List<Chromosome> chromosomes = pop.getChromosomes();

        assertNotNull(chromosomes);
        assertEquals(0, pop.getPopulationSize());
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome(100));
        chromosomes.add(new Chromosome(50));

        ListPopulation pop = new ListPopulation(chromosomes, 10);

        Chromosome fittest = pop.getFittestChromosome();
        assertEquals(100, fittest.getFitness());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation pop = new ListPopulation(5);

        assertEquals(5, pop.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativeLimit() {
        ListPopulation pop = new ListPopulation(5);
        pop.setPopulationLimit(-1);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZeroLimit() {
        ListPopulation pop = new ListPopulation(5);
        pop.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        ListPopulation pop = new ListPopulation(chromosomes, 10);
        pop.setPopulationLimit(2);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation pop = new ListPopulation(5);
        pop.addChromosome(new Chromosome());
        pop.addChromosome(new Chromosome());
        pop.addChromosome(new Chromosome());

        assertEquals(3, pop.getPopulationSize());
    }
}