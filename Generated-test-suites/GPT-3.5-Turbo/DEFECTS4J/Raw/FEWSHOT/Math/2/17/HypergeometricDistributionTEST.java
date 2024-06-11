import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test
    public void testConstructor() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(4, distribution.getNumberOfSuccesses());
        assertEquals(5, distribution.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -4, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_NegativeSampleSize() {
        new HypergeometricDistribution(10, 4, -5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroPopulationSize() {
        new HypergeometricDistribution(0, 4, 5);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertEquals(0.4, distribution.probability(2), 1e-15);
        assertEquals(0.0, distribution.probability(10), 1e-15);
        assertEquals(1.0, distribution.probability(4), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertEquals(0.1, distribution.cumulativeProbability(2), 1e-15);
        assertEquals(0.0, distribution.cumulativeProbability(10), 1e-15);
        assertEquals(1.0, distribution.cumulativeProbability(4), 1e-15);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertEquals(0.9, distribution.upperCumulativeProbability(2), 1e-15);
        assertEquals(1.0, distribution.upperCumulativeProbability(10), 1e-15);
        assertEquals(0.0, distribution.upperCumulativeProbability(4), 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertEquals(2.0, distribution.getNumericalMean(), 1e-15);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertEquals(1.0, distribution.getNumericalVariance(), 1e-15);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertEquals(4, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 4, 5);
        assertTrue(distribution.isSupportConnected());
    }
}