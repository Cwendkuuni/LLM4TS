import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.*;

public class ListPopulationTest {

    @Test
    public void testConstructor_withValidPopulationLimit() {
        // Arrange
        int populationLimit = 10;

        // Act
        ListPopulation listPopulation = new ListPopulation(populationLimit);

        // Assert
        Assert.assertNotNull(listPopulation);
        Assert.assertEquals(populationLimit, listPopulation.getPopulationLimit());
        Assert.assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_withInvalidPopulationLimit() {
        // Arrange
        int populationLimit = -10;

        // Act
        ListPopulation listPopulation = new ListPopulation(populationLimit);
    }

    @Test
    public void testConstructor_withChromosomesList() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));
        int populationLimit = 10;

        // Act
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);

        // Assert
        Assert.assertNotNull(listPopulation);
        Assert.assertEquals(populationLimit, listPopulation.getPopulationLimit());
        Assert.assertEquals(2, listPopulation.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_withNullChromosomesList() {
        // Arrange
        List<Chromosome> chromosomes = null;
        int populationLimit = 10;

        // Act
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_withZeroPopulationLimit() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));
        int populationLimit = 0;

        // Act
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_withExceedingChromosomesList() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));
        int populationLimit = 1;

        // Act
        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomes_withValidChromosomesList() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));

        ListPopulation listPopulation = new ListPopulation(10);

        // Act
        listPopulation.setChromosomes(chromosomes);

        // Assert
        Assert.assertEquals(2, listPopulation.getPopulationSize());
        Assert.assertEquals(chromosomes, listPopulation.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomes_withNullChromosomesList() {
        // Arrange
        List<Chromosome> chromosomes = null;

        ListPopulation listPopulation = new ListPopulation(10);

        // Act
        listPopulation.setChromosomes(chromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_withExceedingChromosomesList() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));

        ListPopulation listPopulation = new ListPopulation(1);

        // Act
        listPopulation.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes_withValidChromosomeCollection() {
        // Arrange
        Collection<Chromosome> chromosomeCollection = new ArrayList<>();
        chromosomeCollection.add(mock(Chromosome.class));
        chromosomeCollection.add(mock(Chromosome.class));

        ListPopulation listPopulation = new ListPopulation(10);

        // Act
        listPopulation.addChromosomes(chromosomeCollection);

        // Assert
        Assert.assertEquals(2, listPopulation.getPopulationSize());
        Assert.assertTrue(listPopulation.getChromosomes().containsAll(chromosomeCollection));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_withExceedingChromosomeCollection() {
        // Arrange
        Collection<Chromosome> chromosomeCollection = new ArrayList<>();
        chromosomeCollection.add(mock(Chromosome.class));
        chromosomeCollection.add(mock(Chromosome.class));

        ListPopulation listPopulation = new ListPopulation(1);

        // Act
        listPopulation.addChromosomes(chromosomeCollection);
    }

    @Test
    public void testGetChromosomes() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = mock(Chromosome.class);
        chromosomes.add(chromosome);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        // Act
        List<Chromosome> result = listPopulation.getChromosomes();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(chromosomes, result);
    }

    @Test
    public void testGetChromosomeList() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = mock(Chromosome.class);
        chromosomes.add(chromosome);
        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        // Act
        List<Chromosome> result = listPopulation.getChromosomeList();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(chromosomes, result);
    }

    @Test
    public void testAddChromosome_withValidChromosome() {
        // Arrange
        Chromosome chromosome = mock(Chromosome.class);
        ListPopulation listPopulation = new ListPopulation(10);

        // Act
        listPopulation.addChromosome(chromosome);

        // Assert
        Assert.assertEquals(1, listPopulation.getPopulationSize());
        Assert.assertTrue(listPopulation.getChromosomes().contains(chromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_withExceedingChromosomes() {
        // Arrange
        Chromosome chromosome = mock(Chromosome.class);
        ListPopulation listPopulation = new ListPopulation(1);

        // Act
        listPopulation.addChromosome(chromosome);
        listPopulation.addChromosome(chromosome);
    }

    @Test
    public void testGetFittestChromosome() {
        // Arrange
        Chromosome chromosome1 = mock(Chromosome.class);
        Chromosome chromosome2 = mock(Chromosome.class);

        when(chromosome1.compareTo(chromosome2)).thenReturn(1);

        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        // Act
        Chromosome result = listPopulation.getFittestChromosome();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(chromosome1, result);
    }

    @Test
    public void testGetPopulationLimit() {
        // Arrange
        int populationLimit = 10;
        ListPopulation listPopulation = new ListPopulation(populationLimit);

        // Act
        int result = listPopulation.getPopulationLimit();

        // Assert
        Assert.assertEquals(populationLimit, result);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_withInvalidPopulationLimit() {
        // Arrange
        ListPopulation listPopulation = new ListPopulation(10);

        // Act
        listPopulation.setPopulationLimit(-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_withSmallPopulationLimit() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));
        int populationLimit = 1;

        ListPopulation listPopulation = new ListPopulation(chromosomes, populationLimit);

        // Act
        listPopulation.setPopulationLimit(populationLimit);
    }

    @Test
    public void testSetPopulationLimit_withValidPopulationLimit() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        int populationLimit = 10;

        ListPopulation listPopulation = new ListPopulation(chromosomes, 1);

        // Act
        listPopulation.setPopulationLimit(populationLimit);

        // Assert
        Assert.assertEquals(populationLimit, listPopulation.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(mock(Chromosome.class));
        chromosomes.add(mock(Chromosome.class));

        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        // Act
        int result = listPopulation.getPopulationSize();

        // Assert
        Assert.assertEquals(2, result);
    }

    @Test
    public void testToString() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = mock(Chromosome.class);
        chromosomes.add(chromosome);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        // Act
        String result = listPopulation.toString();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(chromosomes.toString(), result);
    }

    @Test
    public void testIterator() {
        // Arrange
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome = mock(Chromosome.class);
        chromosomes.add(chromosome);

        ListPopulation listPopulation = new ListPopulation(chromosomes, 10);

        // Act
        Iterator<Chromosome> iterator = listPopulation.iterator();

        // Assert
        Assert.assertNotNull(iterator);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(chromosome, iterator.next());
    }
}