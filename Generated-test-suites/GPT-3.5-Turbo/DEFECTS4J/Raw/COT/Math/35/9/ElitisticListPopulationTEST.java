import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.Population;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ElitisticListPopulationTest {

    private ElitisticListPopulation population;

    @Before
    public void setUp() {
        population = new ElitisticListPopulation(10, 0.9);
    }

    @Test
    public void testNextGeneration() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome());
        }
        population = new ElitisticListPopulation(chromosomes, 10, 0.9);

        Population nextGen = population.nextGeneration();

        // Check if the size is correct
        assert(nextGen.getChromosomes().size() == 9);
        
        // Check if the best chromosomes are copied to the next generation
        List<Chromosome> sortedChromosomes = population.getChromosomes();
        Collections.sort(sortedChromosomes);
        for (int i = 0; i < (int) Math.ceil((1.0 - population.getElitismRate()) * population.getChromosomes().size()); i++) {
            assert(nextGen.getChromosomes().contains(sortedChromosomes.get(i)));
        }
    }

    @Test
    public void testSetElitismRate() {
        double elitismRate = 0.5;
        population.setElitismRate(elitismRate);
        assert(population.getElitismRate() == elitismRate);

        elitismRate = 2;
        try {
            population.setElitismRate(elitismRate);
            assert(false);  // Exception should have been thrown
        } catch (OutOfRangeException e) {
            assert(e.getMessage().equals(LocalizedFormats.ELITISM_RATE.toString()));
        }
    }

    @Test
    public void testGetElitismRate() {
        double elitismRate = population.getElitismRate();
        assert(elitismRate == 0.9);
    }
}