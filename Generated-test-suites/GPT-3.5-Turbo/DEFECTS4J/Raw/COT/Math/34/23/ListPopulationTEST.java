import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        try {
            ListPopulation population = new ListPopulation(-1);
            fail("NotPositiveException not thrown");
        } catch (NotPositiveException e) {
            assertEquals(LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, e.getSpecificPattern());
        }
    }

    @Test
    public void testListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        try {
            ListPopulation population = new ListPopulation(chromosomes, 1);
            fail("NumberIsTooLargeException not thrown");
        } catch (NumberIsTooLargeException e) {
            assertEquals(LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, e.getSpecificPattern());
        }
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(3);
        population.setChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testAddChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(3);
        population.addChromosomes(chromosomes);
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(3);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertTrue(chromosomes.isEmpty());
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(3);
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertTrue(chromosomes.isEmpty());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(3);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertTrue(population.getChromosomes().contains(chromosome));
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(3);
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(2);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(3);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(1);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(3);
        assertEquals(3, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(3);
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitTooSmall() {
        ListPopulation population = new ListPopulation(3);
        try {
            population.setPopulationLimit(2);
            fail("NumberIsTooSmallException not thrown");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(3);
        assertEquals(0, population.getPopulationSize());
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals(1, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(3);
        assertEquals("[]", population.toString());
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        assertEquals("[" + chromosome.toString() + "]", population.toString());
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(3);
        Iterator<Chromosome> iterator = population.iterator();
        assertFalse(iterator.hasNext());
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        iterator = population.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(chromosome, iterator.next());
    }
}