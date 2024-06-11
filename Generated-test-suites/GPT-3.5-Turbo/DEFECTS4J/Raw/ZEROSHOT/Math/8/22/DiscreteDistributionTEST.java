import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiscreteDistributionTest {

    private DiscreteDistribution<Integer> distribution;

    @Before
    public void setUp() {
        distribution = new DiscreteDistribution<>(
                List.of(
                        new Pair<>(1, 0.4),
                        new Pair<>(2, 0.3),
                        new Pair<>(3, 0.2),
                        new Pair<>(4, 0.1)
                )
        );
    }

    @Test
    public void testProbability() {
        assertEquals(0.4, distribution.probability(1), 1e-10);
        assertEquals(0.3, distribution.probability(2), 1e-10);
        assertEquals(0.2, distribution.probability(3), 1e-10);
        assertEquals(0.1, distribution.probability(4), 1e-10);
        assertEquals(0.0, distribution.probability(5), 1e-10);
    }

    @Test
    public void testGetSamples() {
        assertEquals(List.of(
                new Pair<>(1, 0.4),
                new Pair<>(2, 0.3),
                new Pair<>(3, 0.2),
                new Pair<>(4, 0.1)
        ), distribution.getSamples());
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            int sample = distribution.sample();
            assertTrue(sample >= 1 && sample <= 4);
        }
    }

    @Test
    public void testSampleInvalidSize() {
        try {
            distribution.sample(0);
            fail("Did not throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // success
        }
    }

    @Test
    public void testSampleValidSize() {
        Integer[] sample = distribution.sample(100);
        assertEquals(100, sample.length);

        for (int i = 0; i < 100; i++) {
            assertTrue(sample[i] >= 1 && sample[i] <= 4);
        }
    }
}