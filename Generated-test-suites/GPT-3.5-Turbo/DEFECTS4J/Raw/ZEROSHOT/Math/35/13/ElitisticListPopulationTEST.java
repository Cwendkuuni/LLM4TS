import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.genetics.*;
import java.util.ArrayList;
import java.util.List;

public class ElitisticListPopulationTest {

    @Test
    public void testNextGeneration() {
        // create a population
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.9);

        // test next generation
        Population nextGeneration = population.nextGeneration();
        List<Chromosome> nextGenerationChromosomes = nextGeneration.getChromosomes();
        assertEquals(3, nextGenerationChromosomes.size());

        // check if the best chromosomes are copied
        assertTrue(nextGenerationChromosomes.contains(c1));
        assertTrue(nextGenerationChromosomes.contains(c2));
        assertTrue(nextGenerationChromosomes.contains(c3));

        // check if the non-best chromosomes are not copied
        assertFalse(nextGenerationChromosomes.contains(c4));
        assertFalse(nextGenerationChromosomes.contains(c5));
    }

    @Test
    public void testElitismRate() {
        // create a population
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        Chromosome c3 = new Chromosome();
        chromosomes.add(c1);
        chromosomes.add(c2);
        chromosomes.add(c3);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 3, 0.9);

        // check if the elitism rate is set correctly
        assertEquals(0.9, population.getElitismRate(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInvalidElitismRate() {
        // create a population with invalid elitism rate
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome c1 = new Chromosome();
        chromosomes.add(c1);
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 1, 1.5);
    }
}