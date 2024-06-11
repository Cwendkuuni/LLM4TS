import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;

public class ElitisticListPopulationTest {

    @Test
    public void testElitisticListPopulationWithListAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10);
        int expectedSize = 10;
        Assert.assertEquals(expectedSize, population.size());
        
    }

    // Edge case: empty list
    @Test(expected = IllegalArgumentException.class)
    public void testElitisticListPopulationWithEmptyList() {
        List<Chromosome> emptyList = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(emptyList, 10);
    }

    // Exception handling: OutOfRangeException
    @Test(expected = OutOfRangeException.class)
    public void testElitisticListPopulationWithNegativePopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, -5);
    }
}