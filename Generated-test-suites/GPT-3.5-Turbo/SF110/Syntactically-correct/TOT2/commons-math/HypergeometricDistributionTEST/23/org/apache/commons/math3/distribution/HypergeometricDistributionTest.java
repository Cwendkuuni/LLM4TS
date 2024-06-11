package org.apache.commons.math3.distribution;
import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, hypergeometricDistribution.getPopulationSize());
        assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(3, hypergeometricDistribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(-1), 0.0001);
        assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(10), 0.0001);
        assertEquals(0.4667, hypergeometricDistribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometricDistribution.probability(-1), 0.0001);
        assertEquals(0.0, hypergeometricDistribution.probability(11), 0.0001);
        assertEquals(0.0917, hypergeometricDistribution.probability(3), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(5), 0.0001);
        assertEquals(0.8044, hypergeometricDistribution.upperCumulativeProbability(3), 0.0001);
    }

    @Test
    public void testNumericalMean() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.4285, hypergeometricDistribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }
}