import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;

public class ElitisticListPopulationTest {
    @Test
    public void testConstructorWithListAndLimitAndRate() {
        List<Chromosome> chromosomes = Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome());
        ElitisticListPopulation population = new ElitisticListPopulation(chromosomes, 10, 0.9);
        Assert.assertEquals(10, population.size());
    }
}