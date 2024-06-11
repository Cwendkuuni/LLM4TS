import static org.junit.Assert.*;
import org.junit.Test;

public class DiscreteDistributionTest {

    @Test
    public void testProbability() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(new Pair<>(1, 0.5), new Pair<>(2, 0.5)));

        assertEquals(0.5, distribution.probability(1), 0.0001);
        assertEquals(0.5, distribution.probability(2), 0.0001);
        assertEquals(0.0, distribution.probability(3), 0.0001);
    }

    @Test
    public void testGetSamples() {
        List<Pair<Integer, Double>> samples = Arrays.asList(new Pair<>(1, 0.3), new Pair<>(2, 0.5), new Pair<>(3, 0.2));
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(samples);

        assertEquals(samples, distribution.getSamples());
    }

    @Test
    public void testSample() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(new Pair<>(1, 0.5), new Pair<>(2, 0.5)));

        for (int i = 0; i < 100; i++) {
            assertTrue(distribution.sample() == 1 || distribution.sample() == 2);
        }
    }

    @Test
    public void testSampleSize() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(new Pair<>(1, 0.5), new Pair<>(2, 0.5)));

        Integer[] samples = distribution.sample(5);

        assertEquals(5, samples.length);
        for (Integer sample : samples) {
            assertTrue(sample == 1 || sample == 2);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSampleSizeException() {
        DiscreteDistribution<Integer> distribution = new DiscreteDistribution<>(Arrays.asList(new Pair<>(1, 0.5), new Pair<>(2, 0.5)));
        distribution.sample(0);
    }
}