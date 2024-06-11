import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulation_ListAndInt() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10);
        assertEquals(10, population.size());
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulation_ListAndNegativeInt() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, -5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulation_ListAndZeroInt() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testElitisticListPopulation_ListAndNullInt() {
        List<Chromosome> chromosomes = null;
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10);
    }
}