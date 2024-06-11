import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private List<Chromosome> chromosomes;
    private ListPopulation listPopulation;
    private int populationLimit;

    @Before
    public void setUp() {
        populationLimit = 10;
        chromosomes = new ArrayList<Chromosome>();
        chromosomes.add(new Chromosome(1));
        chromosomes.add(new Chromosome(2));
        listPopulation = new ListPopulation(chromosomes, populationLimit);
    }
  
    @Test
    public void testListPopulation_Constructor() {
        try {
            ListPopulation emptyListPopulation = new ListPopulation(0);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }

        try {
            ListPopulation largerListPopulation = new ListPopulation(1);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals("Chromosomes list is bigger than population limit", e.getMessage());
            assertEquals(2, chromosomes.size());
        }

        try {
            ListPopulation listPopulation = new ListPopulation(chromosomes, 4);
            assertEquals(2, listPopulation.getPopulationSize());
            List<Chromosome> list = listPopulation.getChromosomes();
            assertEquals(2, list.size());
            assertEquals(1, list.get(0).getGene());
            assertEquals(2, list.get(1).getGene());
        } catch (NumberIsTooLargeException e) {
            fail("Unexpected NumberIsTooLargeException");
        }
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> newList = new ArrayList<Chromosome>();
        newList.add(new Chromosome(3));
        newList.add(new Chromosome(4));

        try {
            listPopulation.setChromosomes(newList);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }

        try {
            listPopulation.setChromosomes(chromosomes);
            assertEquals(2, listPopulation.getChromosomeList().size());
            assertEquals(1, listPopulation.getChromosomeList().get(0).getGene());
            assertEquals(2, listPopulation.getChromosomeList().get(1).getGene());
        } catch (NumberIsTooLargeException e) {
            fail("Unexpected NumberIsTooLargeException");
        }
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome(3));
        newChromosomes.add(new Chromosome(4));
        newChromosomes.add(new Chromosome(5));

        try {
            listPopulation.addChromosomes(newChromosomes);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals("Chromosomes to be added is bigger than population limit", e.getMessage());
            assertEquals(2, listPopulation.getChromosomeList().size());
            assertEquals(1, listPopulation.getChromosomeList().get(0).getGene());
            assertEquals(2, listPopulation.getChromosomeList().get(1).getGene());
        }

        try {
            List<Chromosome> newList = new ArrayList<Chromosome>();
            newList.add(new Chromosome(3));
            newList.add(new Chromosome(4));
            newList.add(new Chromosome(5));
            newList.add(new Chromosome(6));
            listPopulation.addChromosomes(newList);
            assertEquals(6, listPopulation.getChromosomeList().size());
            assertEquals(1, listPopulation.getChromosomeList().get(0).getGene());
            assertEquals(6, listPopulation.getChromosomeList().get(5).getGene());
        } catch (NumberIsTooLargeException e) {
            fail("Unexpected NumberIsTooLargeException");
        }
    }

    @Test
    public void testGetChromosomes() {
        assertNotNull(listPopulation.getChromosomes());
        assertEquals(2, listPopulation.getChromosomes().size());
        assertEquals(1, listPopulation.getChromosomes().get(0).getGene());
        assertEquals(2, listPopulation.getChromosomes().get(1).getGene());
    }

    @Test
    public void testAddChromosome() {
        try {
            Chromosome chromosome = new Chromosome(3);
            listPopulation.addChromosome(chromosome);
            assertEquals(3, listPopulation.getChromosomeList().size());
            assertEquals(3, listPopulation.getChromosomeList().get(2).getGene());
        } catch (NumberIsTooLargeException e) {
            fail("Unexpected NumberIsTooLargeException");
        }

        try {
            Chromosome chromosome = new Chromosome(4);
            listPopulation.addChromosome(chromosome);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertEquals("Chromosomes list is bigger than population limit", e.getMessage());
        }
    }

    @Test
    public void testGetFittestChromosome() {
        int[] genes = new int[]{1, 2, 3, 4, 5, 6};
        List<Chromosome> chromosomeList = new ArrayList<Chromosome>();
        for (int gene : genes) {
            chromosomeList.add(new Chromosome(gene));
        }
        ListPopulation newListPopulation = new ListPopulation(chromosomeList, 10);
        Chromosome fittest = newListPopulation.getFittestChromosome();
        assertEquals(6, fittest.getGene());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        try {
            listPopulation.setPopulationLimit(0);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }

        try {
            listPopulation.setPopulationLimit(4);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            assertEquals("New population limit (4) is smaller than current number of chromosomes (2)", e.getMessage());
        }

        try {
            listPopulation.setPopulationLimit(3);
            assertEquals(3, listPopulation.getPopulationLimit());
        } catch (NumberIsTooSmallException e) {
            fail("Unexpected NumberIsTooSmallException");
        }
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(2, listPopulation.getPopulationSize());
    }
}