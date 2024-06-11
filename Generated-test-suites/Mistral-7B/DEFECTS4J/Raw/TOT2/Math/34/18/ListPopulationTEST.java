import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testEmptyListPopulation() {
        ListPopulation population = new ListPopulation();
        int expectedSize = 0;
        Assert.assertEquals(expectedSize, population.getPopulationSize());
    }
}