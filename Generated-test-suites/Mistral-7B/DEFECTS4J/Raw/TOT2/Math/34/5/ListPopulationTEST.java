import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.genetics.ListPopulation;

public class ListPopulationTest {

    @Test
    public void testListPopulationEmpty() {
        ListPopulation population = new ListPopulation(10);
        Assert.assertEquals(10, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
        Assert.assertNotNull(population.getChromosomes());
    }
}