package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import java.util.Random;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        try {
            new FDistribution(1.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() {
        try {
            new FDistribution(1.0, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testFDistributionDensity() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        double density = fDist.density(1.5);
        assertTrue("Expected positive density", density > 0);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        double probability = fDist.cumulativeProbability(1.5);
        assertTrue("Expected probability between 0 and 1", probability >= 0 && probability <= 1);
    }

    @Test
    public void testFDistributionGetNumeratorDegreesOfFreedom() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        assertEquals("Expected numerator degrees of freedom to be 2", 2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionGetDenominatorDegreesOfFreedom() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        assertEquals("Expected denominator degrees of freedom to be 3", 3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionGetSolverAbsoluteAccuracy() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        assertEquals("Expected solver absolute accuracy to be 1e-9", 1e-9, fDist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 3.0, 3.0);
        double mean = fDist.getNumericalMean();
        assertEquals("Expected numerical mean to be 3 / 2", 1.5, mean, 0.0);

        fDist = new FDistribution(rng, 1.0, 3.0);
        mean = fDist.getNumericalMean();
        assertNull("Expected numerical mean to be undefined for 1 degree of freedom in denominator", mean);
    }

    @Test
    public void testFDistributionGetNumericalVariance() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 4.0, 5.0);
        double variance = fDist.getNumericalVariance();
        assertEquals("Expected numerical variance to be [2 * 5^2 * (4 + 5 - 2)] / [4 * (5 - 2)^2 * (5 - 4)]", 3.273218803366639, variance, 0.0);

        fDist = new FDistribution(rng, 2.0, 3.0);
        variance = fDist.getNumericalVariance();
        assertNull("Expected numerical variance to be undefined for 2 degrees of freedom in denominator", variance);
    }

    @Test
    public void testFDistributionGetSupportLowerBound() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        assertEquals("Expected support lower bound to be 0", 0.0, fDist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testFDistributionGetSupportUpperBound() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        assertEquals("Expected support upper bound to be Double.POSITIVE_INFINITY", Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testFDistributionIsSupportLowerBoundInclusive() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        assertTrue("Expected support lower bound to be inclusive", fDist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testFDistributionIsSupportUpperBoundInclusive() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        assertFalse("Expected support upper bound to be exclusive", fDist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionIsSupportConnected() {
        Random rng = new Random(1L);
        FDistribution fDist = new FDistribution(rng, 2.0, 3.0);
        assertTrue("Expected support to be connected", fDist.isSupportConnected());
    }
}