import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationConstructorChromosomesAndPopulationLimit() {
        List<Chromosome> chromosomes = Collections.singletonList(new Chromosome() {}); // replace with a valid chromosome
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Assert.assertEquals(10, population.size());
    }

    @Test
    public void testElitisticListPopulationConstructorPopulationLimitAndElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        Assert.assertEquals(5, population.size());
    }

    @Test
    public void testNextGenerationEmptyPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(0, 0.9);
        Population nextGeneration = population.nextGeneration();
        Assert.assertTrue(nextGeneration.isEmpty());
    }

    @Test
    public void testNextGenerationValidPopulation() {
        ElitisticListPopulation population = new ElitisticListPopulation(List.of(new Chromosome(){}), 10, 0.9);
        Population nextGeneration = population.nextGeneration();
        Assert.assertEquals(10, nextGeneration.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateLowerThanZero() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(-0.1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetElitismRateGreaterThanOne() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        population.setElitismRate(1.1);
    }

    @Test
    public void testGetElitismRate() {
        ElitisticListPopulation population = new ElitisticListPopulation(5, 0.9);
        Assert.assertEquals(0.9, population.getElitismRate(), 0.001);
    }

    @Test
    public void testSortingAndCopyingBestChromosomes() {
        List<Chromosome> parentPopulation = List.of(
                new Chromosome(){}, new Chromosome(){}, new Chromosome(){}, // worst chromosomes
                new Chromosome(){}, new Chromosome(){}, new Chromosome(){}, // average chromosomes
                new Chromosome(){}, new Chromosome(){}, new Chromosome(){} // best chromosomes
        );
        ElitisticListPopulation population = new ElitisticListPopulation(parentPopulation, 10, 0.9);
        Collections.sort(parentPopulation);
        Population nextGeneration = population.nextGeneration();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        int boundIndex = (int) Math.ceil((1.0 - 0.9) * parentPopulation.size());
        for (int i = boundIndex; i < parentPopulation.size(); i++) {
            Assert.assertEquals(parentPopulation.get(i), nextGenerationChromosomes.get(i));
        }
    }

}