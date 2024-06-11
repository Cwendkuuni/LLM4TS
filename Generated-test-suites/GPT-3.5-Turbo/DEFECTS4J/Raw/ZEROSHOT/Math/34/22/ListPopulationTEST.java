import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ListPopulationTest {

    @Test
    public void testConstructorWithValidParameters() {
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimit() {
        int populationLimit = -1;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomesList() {
        List<Chromosome> chromosomes = null;
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidPopulationLimitInChromosomesList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = -1;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesListExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomesWithValidParameters() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomesList() {
        List<Chromosome> chromosomes = null;
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setChromosomes(chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesListExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 1;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomesWithValidParameters() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.addChromosomes(chromosomes);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithChromosomesListExceedingPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        int populationLimit = 1;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        listPopulation.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(1.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(2.0);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        assertEquals(chromosome2, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(populationLimit, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitWithValidPopulationLimit() {
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        int newPopulationLimit = 20;
        listPopulation.setPopulationLimit(newPopulationLimit);
        assertEquals(newPopulationLimit, listPopulation.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidPopulationLimit() {
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        int newPopulationLimit = -1;
        listPopulation.setPopulationLimit(newPopulationLimit);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithNewPopulationLimitSmallerThanCurrentChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        int newPopulationLimit = 5;
        listPopulation.setPopulationLimit(newPopulationLimit);
    }

    @Test
    public void testGetPopulationSize() {
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(1.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(2.0);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        String expectedString = "[Chromosome: 1.0, Chromosome: 2.0]";
        assertEquals(expectedString, listPopulation.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(1.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(2.0);
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
        assertNotNull(listPopulation.iterator());
    }
}