package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setUp() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        distribution = new HypergeometricDistribution(10, 5, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(3, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.00001);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(3), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(4), 0.00001);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.00001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0, distribution.probability(-1), 0.00001);
        assertEquals(0.0, distribution.probability(4), 0.00001);
        assertEquals(0.4, distribution.probability(2), 0.00001);
        assertEquals(0.4, distribution.probability(3), 0.00001);
        assertEquals(0.0, distribution.probability(5), 0.00001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.00001);
        assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.00001);
        assertEquals(0.5, distribution.upperCumulativeProbability(3), 0.00001);
        assertEquals(0.0, distribution.upperCumulativeProbability(10), 0.00001);
        assertEquals(0.0, distribution.upperCumulativeProbability(11), 0.00001);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(1.5, distribution.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.375, distribution.getNumericalVariance(), 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

}