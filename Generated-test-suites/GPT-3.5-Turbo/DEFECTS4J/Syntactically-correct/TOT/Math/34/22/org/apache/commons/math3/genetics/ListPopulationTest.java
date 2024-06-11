
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ListPopulationTest {

    private ListPopulation population;
    private List<Chromosome> chromosomes;

    @Before
    public void setUp() {
        chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population = new ListPopulation(chromosomes, 10);
    }

    @Test
    public void testListPopulation() {
        assertNotNull(population);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListPopulation_NullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulation_NegativePopulationLimit() {
        new ListPopulation(chromosomes, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulation_TooManyChromosomes() {
        chromosomes.addAll(Arrays.asList(new Chromosome(), new Chromosome()));
        new ListPopulation(chromosomes, 2);
    }

    @Test
    public void testSetChromosomes() {
        assertEquals(2, population.getPopulationSize());
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        population.setChromosomes(newChromosomes);
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(newChromosomes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetChromosomes_NullChromosomes() {
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomes_TooManyChromosomes() {
        chromosomes.addAll(Arrays.asList(new Chromosome(), new Chromosome()));
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        assertEquals(2, population.getPopulationSize());
        List<Chromosome> newChromosomes = Arrays.asList(new Chromosome(), new Chromosome());
        population.addChromosomes(newChromosomes);
        assertEquals(4, population.getPopulationSize());
        assertTrue(population.getChromosomes().containsAll(newChromosomes));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomes_TooManyChromosomes() {
        chromosomes.addAll(Arrays.asList(new Chromosome(), new Chromosome()));
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetChromosomes() {
        assertEquals(2, population.getChromosomes().size());
        assertTrue(population.getChromosomes().containsAll(chromosomes));
    }

    @Test
    public void testAddChromosome() {
        assertEquals(2, population.getPopulationSize());
        Chromosome newChromosome = new Chromosome();
        population.addChromosome(newChromosome);
        assertEquals(3, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(newChromosome));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosome_TooManyChromosomes() {
        chromosomes.addAll(Arrays.asList(new Chromosome(), new Chromosome()));
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome fittest = new Chromosome();
        for (Chromosome chromosome : chromosomes) {
            if (chromosome.compareTo(fittest) > 0) {
                fittest = chromosome;
            }
        }
        assertEquals(fittest, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimit_NegativePopulationLimit() {
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimit_TooSmallPopulationLimit() {
        population.setPopulationLimit(1);
    }

    @Test
    public void testSetPopulationLimit() {
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        assertEquals(chromosomes.toString(), population.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        population.iterator().remove();
    }
}
