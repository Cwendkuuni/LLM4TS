import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation elitisticListPopulation;

    @Before
    public void setup() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        elitisticListPopulation = new ElitisticListPopulation(chromosomes, 10, 0.9);
    }

    @Test
    public void testElitisticListPopulation() {
        Assert.assertNotNull(elitisticListPopulation);
    }

    @Test
    public void testNextGeneration() {
        Population nextGeneration = elitisticListPopulation.nextGeneration();
        Assert.assertNotNull(nextGeneration);
    }

    @Test
    public void testSetElitismRate() {
        elitisticListPopulation.setElitismRate(0.5);
        double elitismRate = elitisticListPopulation.getElitismRate();
        Assert.assertEquals(0.5, elitismRate, 0.001);
    }
}