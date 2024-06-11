package org.apache.commons.math3.distribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstructor() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, hypergeometricDistribution.getPopulationSize());
        assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(3, hypergeometricDistribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometricDistribution.probability(6), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(2), 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6428, hypergeometricDistribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }

}