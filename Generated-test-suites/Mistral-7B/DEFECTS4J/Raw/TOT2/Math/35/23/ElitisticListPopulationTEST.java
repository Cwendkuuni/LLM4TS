import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Test;

public class ElitisticListPopulationTest {

    @Test
    public void testConstructorWithListAndInt() {
        List<Chromosome> chromosomes = new ArrayList<>();
        int populationLimit = 10;
        double elitismRate = 0.9;

        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, populationLimit, elitismRate);

        assertEquals(populationLimit, population.size());
        assertEquals(elitismRate, population.getElitismRate(), 0.001);
        assertNotNull(population.getChromosomes());
        assertSame(chromosomes, population.getChromosomes());
    }
}