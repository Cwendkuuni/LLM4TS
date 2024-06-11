package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution fDist = new FDistribution(2.0, 3.0);
            assertTrue(fDist instanceof FDistribution);
            assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
            assertEquals(3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            FDistribution fDist = new FDistribution(1.0, 3.0);
            fail("Expected NotStrictlyPositiveException but got a valid distribution.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            FDistribution fDist = new FDistribution(2.0, 0.0);
            fail("Expected NotStrictlyPositiveException but got a valid distribution.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double eps = 1e-9;
        double[] xValues = {0.0, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
        double[] expectedDensities = {0.0, 0.0919107121951583, 0.177283853158229, 0.308529585884145, 0.523598775599685, 0.682663903857341, 0.813317283919936, 0.993262753346222};

        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double density = fDist.density(x);
            assertEquals("Expected density of " + expectedDensities[i] + " for x = " + x + ", but got " + density, expectedDensities[i], density, eps);
        }
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[] xValues = {0.0, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
        double[] expectedProbs = {0.0, 0.117343499292863, 0.228036848375871, 0.418959171426161, 0.604050457360382, 0.772275310824518, 0.882844861597706, 1.0};

        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double prob = fDist.cumulativeProbability(x);
            assertEquals("Expected cumulative probability of " + expectedProbs[i] + " for x = " + x + ", but got " + prob, expectedProbs[i], prob, eps);
        }
    }

    @Test
    public void testFDistributionGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Expected numerator degrees of freedom to be 2.0, but got " + fDist.getNumeratorDegreesOfFreedom(), 2.0, fDist.getNumeratorDegreesOfFreedom(), eps);
    }

    @Test
    public void testFDistributionGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Expected denominator degrees of freedom to be 3.0, but got " + fDist.getDenominatorDegreesOfFreedom(), 3.0, fDist.getDenominatorDegreesOfFreedom(), eps);
    }

    @Test
    public void testFDistributionGetSolverAbsoluteAccuracy() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Expected solver absolute accuracy to be " + FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY + ", but got " + fDist.getSolverAbsoluteAccuracy(), FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, fDist.getSolverAbsoluteAccuracy(), eps);
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Expected numerical mean to be 1.5, but got " + fDist.getNumericalMean(), 1.5, fDist.getNumericalMean(), eps);
    }

    @Test
    public void testFDistributionGetNumericalVariance() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Expected numerical variance to be 1.5 / (3.0 - 2.0), but got " + fDist.getNumericalVariance(), 1.5 / (3.0 - 2.0), fDist.getNumericalVariance(), eps);
    }

    @Test
    public void testFDistributionGetSupportLowerBound() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Expected support lower bound to be 0.0, but got " + fDist.getSupportLowerBound(), 0.0, fDist.getSupportLowerBound(), eps);
    }

    @Test
    public void testFDistributionGetSupportUpperBound() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals("Expected support upper bound to be Double.POSITIVE_INFINITY, but got " + fDist.getSupportUpperBound(), Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), eps);
    }

    @Test
    public void testFDistributionIsSupportLowerBoundInclusive() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue("Expected support lower bound to be inclusive, but got " + fDist.isSupportLowerBoundInclusive(), fDist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testFDistributionIsSupportUpperBoundInclusive() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertFalse("Expected support upper bound to be exclusive, but got " + fDist.isSupportUpperBoundInclusive(), fDist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionIsSupportConnected() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(2.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue("Expected support to be connected, but got " + fDist.isSupportConnected(), fDist.isSupportConnected());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionInvalidRandomGenerator() {
        FDistribution fDist = new FDistribution(new Object(), 2.0, 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionNegativeNumeratorDegreesOfFreedom() {
        try {
            new FDistribution(-1.0, 3.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        fail("Expected IllegalArgumentException but got a valid distribution.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFDistributionNegativeDenominatorDegreesOfFreedom() {
        try {
            new FDistribution(2.0, -1.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        fail("Expected IllegalArgumentException but got a valid distribution.");
    }

    @Test
    public void testFDistributionRandomValues() {
        FDistribution fDist = null;
        try {
            fDist = new FDistribution(new Random(), 3.0, 2.0);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double eps = 0.01;
        for (int i = 0; i < 10000; i++) {
            double x = fDist.inverseCumulativeProbability(new Random().nextDouble());
            assertTrue("Expected x to be positive, but got " + x, x > 0);
            double density = fDist.density(x);
            assertTrue("Expected density to be positive, but got " + density, density > 0);
            assertTrue("Expected x to be within the support bounds, but got " + x + ", expected " + getSupportLowerBound() + " to " + getSupportUpperBound(), x >= getSupportLowerBound() && x <= getSupportUpperBound());
        }
    }
}