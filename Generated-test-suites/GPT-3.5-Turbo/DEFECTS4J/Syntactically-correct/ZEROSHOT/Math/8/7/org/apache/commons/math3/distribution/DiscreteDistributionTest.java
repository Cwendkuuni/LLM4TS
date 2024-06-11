import static org.junit.Assert.*;
import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>();
        double result = distribution.probability(5);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetSamples() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>();
        List<Pair<Integer, Double>> samples = distribution.getSamples();
        assertEquals(0, samples.size());
    }

    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>();
        Integer result = distribution.sample();
        assertNotNull(result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleWithNegativeSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>();
        distribution.sample(-1);
    }
}