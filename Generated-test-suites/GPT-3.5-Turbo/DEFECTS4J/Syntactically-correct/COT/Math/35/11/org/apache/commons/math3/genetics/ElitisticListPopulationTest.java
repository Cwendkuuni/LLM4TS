
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());

        population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = population.nextGeneration();

        assertEquals(6, nextGeneration.getChromosomes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetElitismRate_invalidRate() {
        population.setElitismRate(-0.5);
    }

    @Test
    public void testSetElitismRate_validRate() {
        population.setElitismRate(0.5);

        assertEquals(0.5, population.getElitismRate(), 0.01);
    }
}
