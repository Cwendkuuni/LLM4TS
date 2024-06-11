package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FDistributionTest {

    private static FDistribution distribution;

    @BeforeClass
    public static void setUp() {
        distribution = new FDistribution(new Well19937c(), 4.0, 6.0, 1e-9);
    }

    @Test
    public void testConstructorWithNegativeNumeratorDegreesOfFreedom() {
        try {
            new FDistribution(-2.0, 6.0);
            fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorWithNegativeDenominatorDegreesOfFreedom() {
        try {
            new FDistribution(4.0, -6.0);
            fail("NotStrictlyPositiveException expected");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDensity() {
        assertEquals(0.40891, distribution.density(1.0), 1e-5);
        assertEquals(0.12086, distribution.density(2.0), 1e-5);
        assertEquals(0.06251, distribution.density(3.0), 1e-5);
        assertEquals(0.04346, distribution.density(4.0), 1e-5);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.27447, distribution.cumulativeProbability(1.0), 1e-5);
        assertEquals(0.48137, distribution.cumulativeProbability(2.0), 1e-5);
        assertEquals(0.62980, distribution.cumulativeProbability(3.0), 1e-5);
        assertEquals(0.71183, distribution.cumulativeProbability(4.0), 1e-5);
    }

    @Test
    public void testNumeratorDegreesOfFreedom() {
        assertEquals(4.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testDenominatorDegreesOfFreedom() {
        assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testNumericalMean() {
        assertEquals(1.5, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testNumericalVariance() {
        assertEquals(18.0, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }
}