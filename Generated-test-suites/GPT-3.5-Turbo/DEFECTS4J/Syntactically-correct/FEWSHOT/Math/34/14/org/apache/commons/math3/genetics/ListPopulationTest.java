import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {
    
    private ListPopulation listPopulation;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<Chromosome>();
        listPopulation = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(0, listPopulation.getChromosomes().size());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithPopulationLimitZero() {
        new ListPopulation(chromosomes, 0);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation(chromosomes, -10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithTooManyChromosomes() {
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, 1);
    }

    @Test
    public void testSetChromosomes() {
        chromosomes.add(new Chromosome());
        assertEquals(1, listPopulation.getChromosomes().size());

        chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(2, listPopulation.getChromosomes().size());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithTooManyChromosomes() {
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        listPopulation.addChromosomes(chromosomeColl);
        assertEquals(1, listPopulation.getChromosomes().size());

        chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        listPopulation.addChromosomes(chromosomeColl);
        assertEquals(3, listPopulation.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithTooManyChromosomes() {
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        List<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        listPopulation.addChromosomes(chromosomeColl);
    }

    @Test
    public void testGetChromosomes() {
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(0, listPopulation.getChromosomes().size());

        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(2, listPopulation.getChromosomes().size());
    }

    @Test
    public void testGetChromosomeList() {
        assertNotNull(listPopulation.getChromosomeList());
        assertEquals(0, listPopulation.getChromosomeList().size());

        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        assertNotNull(listPopulation.getChromosomeList());
        assertEquals(2, listPopulation.getChromosomeList().size());
    }

    @Test
    public void testAddChromosome() {
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());

        chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithTooManyChromosomes() {
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        listPopulation.addChromosome(chromosome1);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(5);
        listPopulation.addChromosome(chromosome2);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(7);
        listPopulation.addChromosome(chromosome3);

        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        assertEquals(10, fittestChromosome.getFitness());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithZero() {
        listPopulation.setPopulationLimit(0);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegative() {
        listPopulation.setPopulationLimit(-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());

        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testIterator() {
        Iterator<Chromosome> iterator = listPopulation.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());

        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        iterator = listPopulation.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
    }
}