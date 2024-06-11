import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
        population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    }

    @Test
    public void testNextGeneration() {
        ElitisticListPopulation nextGeneration = population.nextGeneration();
        assertEquals(nextGeneration.getPopulationLimit(), population.getPopulationLimit());
        assertEquals(nextGeneration.getElitismRate(), population.getElitismRate(), 0.0);

        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        List<Chromosome> originalChromosomes = population.getChromosomes();

        // Check that the last (populationSize - elitismRate) chromosomes of the original population are in the next generation
        for (int i=population.getElitismRate(); i<originalChromosomes.size(); i++) {
            assertEquals(nextGenerationChromosomes.get(i-population.getElitismRate()), originalChromosomes.get(i));
        }

    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        population.setElitismRate(-0.1);
    }

    @Test
    public void testSetElitismRate() {
        population.setElitismRate(0.5);
        assertEquals(population.getElitismRate(), 0.5, 0.0);
    }

}