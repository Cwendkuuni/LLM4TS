import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution(0, 1);
    }

    @Test
    public void testDensity() {
        // Typical use case
        double result = distribution.density(0.5);
        assertEquals(1.0, result, 0.0001);

        // Edge case
        result = distribution.density(0);
        assertEquals(1.0, result, 0.0001);
        result = distribution.density(1);
        assertEquals(1.0, result, 0.0001);

        // Error scenario
        result = distribution.density(-1);
        assertEquals(0.0, result, 0.0001);
        result = distribution.density(2);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        double result = distribution.cumulativeProbability(0.5);
        assertEquals(0.5, result, 0.0001);

        // Edge case
        result = distribution.cumulativeProbability(0);
        assertEquals(0.0, result, 0.0001);
        result = distribution.cumulativeProbability(1);
        assertEquals(1.0, result, 0.0001);

        // Error scenario
        result = distribution.cumulativeProbability(-1);
        assertEquals(0.0, result, 0.0001);
        result = distribution.cumulativeProbability(2);
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        // Typical use case
        double mean = distribution.getNumericalMean();
        assertEquals(0.5, mean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        // Typical use case
        double variance = distribution.getNumericalVariance();
        assertEquals(0.0833, variance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        // Typical use case
        double lowerBound = distribution.getSupportLowerBound();
        assertEquals(0.0, lowerBound, 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        // Typical use case
        double upperBound = distribution.getSupportUpperBound();
        assertEquals(1.0, upperBound, 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        double[] samples = distribution.sample(1000);
        // Check if samples are within the bounds
        for (double sample : samples) {
            assertTrue(sample >= 0 && sample < 1);
        }
    }

}