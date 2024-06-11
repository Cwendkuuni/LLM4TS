import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import java.util.ArrayList;
import java.util.List;

public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, 3);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        listPopulation.setChromosomes(newChromosomes);
        assertEquals(2, listPopulation.getChromosomes().size());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNull() {
        listPopulation.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithExceedSize() {
        List<Chromosome> newChromosomes = new ArrayList<>();
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        newChromosomes.add(new Chromosome());
        listPopulation.setChromosomes(newChromosomes);
    }

    @Test
    public void testAddChromosome() {
        listPopulation.addChromosome(new Chromosome());
        assertEquals(3, listPopulation.getChromosomes().size());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWhenExceedSize() {
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        assertNotNull(fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(3, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegative() {
        listPopulation.setPopulationLimit(-1);
    }

    @Test
    public void testSetPopulationLimit() {
        listPopulation.setPopulationLimit(5);
        assertEquals(5, listPopulation.getPopulationLimit());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithSmallerSize() {
        listPopulation.setPopulationLimit(1);
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        assertNotNull(listPopulation.toString());
    }

    @Test
    public void testIterator() {
        assertNotNull(listPopulation.iterator());
    }
}