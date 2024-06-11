import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ListPopulationTest {
    @Test
    public void testConstructor_WithValidParameters() {
        List<Chromosome> chromosomes = mock(List.class);
        int populationLimit = 10;

        ListPopulation population = new ListPopulation(chromosomes, populationLimit);

        assertEquals(population.getPopulationLimit(), populationLimit);
        assertEquals(population.getChromosomes(), chromosomes);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_WithNullChromosomes() {
        List<Chromosome> chromosomes = null;
        int populationLimit = 10;
        
        new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_WithZeroPopulationLimit() {
        List<Chromosome> chromosomes = mock(List.class);
        int populationLimit = 0;

        new ListPopulation(chromosomes, populationLimit);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_WithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = mock(List.class);
        int populationLimit = 10;

        when(chromosomes.size()).thenReturn(populationLimit + 1);

        new ListPopulation(chromosomes, populationLimit);
    }

    @Test
    public void testSetChromosomes_WithValidChromosomes() {
        List<Chromosome> chromosomes = mock(List.class);
        List<Chromosome> newChromosomes = mock(List.class);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.setChromosomes(newChromosomes);

        assertEquals(population.getChromosomes(), newChromosomes);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomes_WithNullChromosomes() {
        List<Chromosome> chromosomes = mock(List.class);
        
        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_WithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = mock(List.class);
        List<Chromosome> newChromosomes = mock(List.class);

        when(newChromosomes.size()).thenReturn(11);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.setChromosomes(newChromosomes);
    }

    @Test
    public void testAddChromosomes_WithValidChromosomes() {
        List<Chromosome> chromosomes = mock(List.class);
        List<Chromosome> newChromosomes = mock(List.class);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.addChromosomes(newChromosomes);

        assertEquals(population.getChromosomes(), newChromosomes);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_WithChromosomesExceedingPopulationLimit() {
        List<Chromosome> chromosomes = mock(List.class);
        List<Chromosome> newChromosomes = mock(List.class);

        when(newChromosomes.size()).thenReturn(11);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.addChromosomes(newChromosomes);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> chromosomes = mock(List.class);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        List<Chromosome> retrievedChromosomes = population.getChromosomes();

        assertEquals(retrievedChromosomes, chromosomes);
    }

    @Test
    public void testAddChromosome_WithValidChromosome() {
        List<Chromosome> chromosomes = mock(List.class);
        Chromosome chromosome = mock(Chromosome.class);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.addChromosome(chromosome);

        assertEquals(population.getChromosomes().size(), 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_WithExceedingPopulationLimit() {
        List<Chromosome> chromosomes = mock(List.class);
        Chromosome chromosome = mock(Chromosome.class);

        when(chromosomes.size()).thenReturn(10);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.addChromosome(chromosome);
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> chromosomes = mock(List.class);
        Chromosome chromosome1 = mock(Chromosome.class);
        Chromosome chromosome2 = mock(Chromosome.class);

        when(chromosomes.get(0)).thenReturn(chromosome1);
        when(chromosome1.compareTo(chromosome2)).thenReturn(1);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        Chromosome fittestChromosome = population.getFittestChromosome();

        assertEquals(fittestChromosome, chromosome1);
    }

    @Test
    public void testGetPopulationLimit() {
        int populationLimit = 10;

        ListPopulation population = new ListPopulation(populationLimit);
        int retrievedPopulationLimit = population.getPopulationLimit();

        assertEquals(retrievedPopulationLimit, populationLimit);
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_WithZeroPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_WithSmallerPopulationSize() {
        List<Chromosome> chromosomes = mock(List.class);

        when(chromosomes.size()).thenReturn(10);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        population.setPopulationLimit(5);
    }

    @Test
    public void testGetPopulationSize() {
        List<Chromosome> chromosomes = mock(List.class);

        when(chromosomes.size()).thenReturn(10);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        int populationSize = population.getPopulationSize();

        assertEquals(populationSize, 10);
    }

    @Test
    public void testIterator() {
        List<Chromosome> chromosomes = mock(List.class);

        ListPopulation population = new ListPopulation(chromosomes, 10);
        Iterator<Chromosome> iterator = population.iterator();

        assertEquals(iterator.hasNext(), false);
    }
}