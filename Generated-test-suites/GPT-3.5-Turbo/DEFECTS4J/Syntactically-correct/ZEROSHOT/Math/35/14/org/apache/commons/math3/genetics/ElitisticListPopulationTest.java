import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        this.population = new ElitisticListPopulation(chromosomes, 10, 0.9);
    }

    @Test
    public void testNextGeneration() {
        Population next = this.population.nextGeneration();
        assertEquals(9, next.getPopulationLimit());
    }

    @Test
    public void testSetElitismRate() {
        this.population.setElitismRate(0.8);
        assertEquals(0.8, this.population.getElitismRate(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateOutOfRange() {
        this.population.setElitismRate(1.5);
    }

    @Test
    public void testGetElitismRate() {
        assertEquals(0.9, this.population.getElitismRate(), 0.001);
    }
}