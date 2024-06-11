package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {
    private HypergeometricDistribution hypergeometricDistribution;

    @Before
    public void setUp() throws NotPositiveException, NumberIsTooLargeException, NotStrictlyPositiveException {
        hypergeometricDistribution = new HypergeometricDistribution(10, 5, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(5, hypergeometricDistribution.getNumberOfSuccesses());
        assertEquals(10, hypergeometricDistribution.getPopulationSize());
        assertEquals(3, hypergeometricDistribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(1.0, hypergeometricDistribution.cumulativeProbability(3), 0.00001);
        assertEquals(0.0, hypergeometricDistribution.cumulativeProbability(0), 0.00001);
        assertEquals(0.5, hypergeometricDistribution.cumulativeProbability(2), 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, hypergeometricDistribution.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.78947, hypergeometricDistribution.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.11765, hypergeometricDistribution.probability(2), 0.00001);
        assertEquals(0.47059, hypergeometricDistribution.probability(1), 0.00001);
        assertEquals(0.35294, hypergeometricDistribution.probability(0), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(0.0, hypergeometricDistribution.upperCumulativeProbability(3), 0.00001);
        assertEquals(1.0, hypergeometricDistribution.upperCumulativeProbability(0), 0.00001);
        assertEquals(0.5, hypergeometricDistribution.upperCumulativeProbability(2), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(2, hypergeometricDistribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, hypergeometricDistribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(hypergeometricDistribution.isSupportConnected());
    }
}