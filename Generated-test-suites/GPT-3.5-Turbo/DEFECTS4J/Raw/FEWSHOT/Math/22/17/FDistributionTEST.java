package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FDistributionTest {
    private final double EPSILON = 1e-9;

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidNumeratorDegreesOfFreedom() {
        new FDistribution(0, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidDenominatorDegreesOfFreedom() {
        new FDistribution(2, 0);
    }
    
    @Test
    public void testConstructorValidDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(2, 3);
        assertEquals(2, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
        assertEquals(3, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
    }

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(2, 3);
        assertEquals(0.1328960806, distribution.density(1), EPSILON);
        assertEquals(0.1214953271, distribution.density(2), EPSILON);
        assertEquals(0.00822254238, distribution.density(5), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(2, 3);
        assertEquals(0, distribution.cumulativeProbability(0), EPSILON);
        assertEquals(0.4825296175, distribution.cumulativeProbability(1), EPSILON);
        assertEquals(0.6080721367, distribution.cumulativeProbability(2), EPSILON);
        assertEquals(0.8588627087, distribution.cumulativeProbability(5), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(2, 3);
        assertEquals(3, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(2, 3);
        assertEquals(18, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(2, 3);
        assertEquals(0, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(2, 3);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(2, 3);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(2, 3);
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(2, 3);
        assertTrue(distribution.isSupportConnected());
    }
}