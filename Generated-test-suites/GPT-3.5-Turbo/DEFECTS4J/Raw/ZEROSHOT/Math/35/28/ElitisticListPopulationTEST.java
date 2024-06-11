import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population = new ElitisticListPopulation(chromosomes, 5, 0.9);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();

        assertNotNull(nextGeneration);
        assertEquals(5, nextGeneration.getPopulationLimit());

        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        assertEquals(3, nextGenerationChromosomes.size());
        assertTrue(nextGenerationChromosomes.contains(population.getChromosomes().get(0)));
        assertTrue(nextGenerationChromosomes.contains(population.getChromosomes().get(1)));
        assertTrue(nextGenerationChromosomes.contains(population.getChromosomes().get(2)));
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.75);
        assertEquals(0.75, population.getElitismRate(), 0.0);
    }
}