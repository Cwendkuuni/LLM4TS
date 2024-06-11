import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class ListPopulationTest {

    @Test
    public void testConstructor() {
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation = new ListPopulation(chromosomes, populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        int populationLimit = -1;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomes() {
        List<Chromosome> chromosomes = null;
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 1;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        List<Chromosome> chromosomes = null;
        listPopulation.setChromosomes(chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(1);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation listPopulation = new ListPopulation(10);
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        listPopulation.addChromosomes(chromosomeColl);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(1);
        Collection<Chromosome> chromosomeColl = new ArrayList<>();
        chromosomeColl.add(new Chromosome());
        chromosomeColl.add(new Chromosome());
        listPopulation.addChromosomes(chromosomeColl);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        List<Chromosome> result = listPopulation.getChromosomes();
        assertEquals(chromosomes, result);
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        List<Chromosome> result = listPopulation.getChromosomeList();
        assertEquals(chromosomes, result);
    }

    @Test
    public void testAddChromosome() {
        ListPopulation listPopulation = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        listPopulation.addChromosome(chromosome);
        assertEquals(1, listPopulation.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeExceedingPopulationLimit() {
        ListPopulation listPopulation = new ListPopulation(1);
        listPopulation.addChromosome(new Chromosome());
        listPopulation.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(0.5);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(0.8);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(0.3);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        Chromosome fittestChromosome = listPopulation.getFittestChromosome();
        assertEquals(chromosome2, fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidPopulationLimit() {
        int populationLimit = -1;
        ListPopulation listPopulation = new ListPopulation(10);
        listPopulation.setPopulationLimit(populationLimit);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithInvalidChromosomesSize() {
        int populationLimit = 1;
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        listPopulation.setPopulationLimit(populationLimit);
    }

    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(0.5);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(0.8);
        Chromosome chromosome3 = new Chromosome();
        chromosome3.setFitness(0.3);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);
        String expected = "[Chromosome(fitness=0.5), Chromosome(fitness=0.8), Chromosome(fitness=0.3)]";
        assertEquals(expected, listPopulation.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        Iterator<Chromosome> iterator = listPopulation.iterator();
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
        assertFalse(iterator.hasNext());
    }
}