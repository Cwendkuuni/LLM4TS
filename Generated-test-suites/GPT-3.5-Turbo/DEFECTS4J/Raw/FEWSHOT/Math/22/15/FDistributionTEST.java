package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class FDistributionTest {

    // Test constructor
    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistribution(5, 10);
        assertEquals(5, distribution.getNumeratorDegreesOfFreedom(), 0);
        assertEquals(10, distribution.getDenominatorDegreesOfFreedom(), 0);
    }

    // Test density method
    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(5, 10);
        assertEquals(0.006990785888358835, distribution.density(1.0), 1e-15);
        assertEquals(0, distribution.density(0), 0);
        assertEquals(0, distribution.density(-1), 0);
        assertEquals(0, distribution.density(Double.POSITIVE_INFINITY), 0);
        assertEquals(0, distribution.density(Double.NEGATIVE_INFINITY), 0);
        assertEquals(0, distribution.density(Double.NaN), 0);
    }

    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(5, 10);
        assertEquals(0.10925399867405748, distribution.cumulativeProbability(1.0), 1e-15);
        assertEquals(0, distribution.cumulativeProbability(0), 0);
        assertEquals(0, distribution.cumulativeProbability(-1), 0);
        assertEquals(1, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0);
        assertEquals(0, distribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0);
        assertEquals(Double.NaN, distribution.cumulativeProbability(Double.NaN), 0);
    }

    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean() {
        FDistribution distribution1 = new FDistribution(5, 10);
        assertEquals(1.25, distribution1.getNumericalMean(), 0);

        FDistribution distribution2 = new FDistribution(1, 10);
        assertEquals(Double.NaN, distribution2.getNumericalMean(), 0);
    }

    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution1 = new FDistribution(5, 10);
        assertEquals(3.333333333333333, distribution1.getNumericalVariance(), 1e-15);

        FDistribution distribution2 = new FDistribution(1, 10);
        assertEquals(Double.NaN, distribution2.getNumericalVariance(), 0);
    }

    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(5, 10);
        assertEquals(0, distribution.getSupportLowerBound(), 0);
    }

    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(5, 10);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0);
    }

    // Test isSupportLowerBoundInclusive method
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(5, 10);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    // Test isSupportUpperBoundInclusive method
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(5, 10);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(5, 10);
        assertTrue(distribution.isSupportConnected());
    }

}