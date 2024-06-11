import org.junit.Test;

import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-5.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);

        // Typical use cases
        assertEquals(0.0337, distribution.probability(2), 0.0001);
        assertEquals(0.0337, distribution.probability(3), 0.0001);
        assertEquals(0.0337, distribution.probability(4), 0.0001);

        //Edge cases
        assertEquals(0.1754, distribution.probability(0), 0.0001);
        assertEquals(0.1366, distribution.probability(1), 0.0001);
        assertEquals(0.0361, distribution.probability(10), 0.0001);

        //Error scenarios
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(-100), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);

        // Typical use cases
        assertEquals(0.0337, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.1348, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(0.9288, distribution.cumulativeProbability(10), 0.0001);

        //Edge cases
        assertEquals(0.0337, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.1754, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.0001);

        //Error scenarios
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, distribution.cumulativeProbability(-100), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);

        // Typical use cases
        assertEquals(0.9772, distribution.normalApproximateProbability(10), 0.0001);
        assertEquals(0.3085, distribution.normalApproximateProbability(3), 0.0001);
        assertEquals(0.0001, distribution.normalApproximateProbability(0), 0.0001);

        //Edge cases
        assertEquals(0.9832, distribution.normalApproximateProbability(11), 0.0001);
        assertEquals(0.2449, distribution.normalApproximateProbability(2), 0.0001);
        assertEquals(0.00003, distribution.normalApproximateProbability(0), 0.0001);

        //Error scenarios
        assertEquals(0.0, distribution.normalApproximateProbability(-1), 0.0001);
        assertEquals(0.0, distribution.normalApproximateProbability(-100), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);

        // Generate and validate a sample set
        int sampleSize = 1000;
        int[] sample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = distribution.sample();
            assertTrue(sample[i] >= 0 && sample[i] <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);

        assertEquals(0, distribution.getDomainLowerBound(0.0), 0.0001);
        assertEquals(0, distribution.getDomainLowerBound(0.5), 0.0001);
        assertEquals(0, distribution.getDomainLowerBound(1.0), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);

        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0), 0.0001);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.0001);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1.0), 0.0001);
    }

}