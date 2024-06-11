import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for UniformRealDistribution.
 */
public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;
    private Well19937c random;

    @Before
    public void setUp() {
        random = new Well19937c();
        distribution = new UniformRealDistribution(random, 0, 1);
    }

    @Test
    public void testDefaultConstructor() {
        distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getLowerBound(), 0.0);
        assertEquals(1, distribution.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidLowerUpperBounds() {
        new UniformRealDistribution(random, 2, 1);
    }

    @Test
    public void testDensity() {
        double x = 0.5;
        double density = distribution.density(x);
        assertEquals(1.0 / (1.0 - 0.0), density, 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.5;
        double p = distribution.cumulativeProbability(x);
        assertEquals(0.5, p, 1e-9);
    }

    @Test
    public void testSample() {
        double sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= 1);
    }

    @Test
    public void testSupportProperties() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertFalse(distribution.isSupportLowerBoundInclusive());
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testNumericalMean() {
        double mean = distribution.getNumericalMean();
        assertEquals(0.5, mean, 1e-9);
    }

    @Test
    public void testNumericalVariance() {
        double variance = distribution.getNumericalVariance();
        assertEquals(0.25, variance, 1e-9);
    }

}