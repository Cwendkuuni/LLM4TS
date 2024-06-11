package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        try {
            FDistribution distribution = new FDistribution(2.0, 3.0);
            assertNotNull(distribution);
        } catch (NotStrictlyPositiveException e) {
            fail("FDistribution creation should not throw NotStrictlyPositiveException");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationWithInvalidDegreesOfFreedom() {
        new FDistribution(-1.0, 3.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution distribution = new FDistribution(2.0, 3.0);
        double density = distribution.density(1.5);
        assertTrue(density > 0);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution distribution = new FDistribution(2.0, 3.0);
        double value = distribution.cumulativeProbability(1.5);
        assertTrue(value > 0);
        assertTrue(value < 1);
    }

    @Test
    public void testFDistributionGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(2.0, 3.0);
        double degreesOfFreedom = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(2.0, degreesOfFreedom);
    }

    @Test
    public void testFDistributionGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(2.0, 3.0);
        double degreesOfFreedom = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(3.0, degreesOfFreedom);
    }

    @Test
    public void testFDistributionGetSolverAbsoluteAccuracy() {
        FDistribution distribution = new FDistribution(2.0, 3.0, 0.001);
        double accuracy = distribution.getSolverAbsoluteAccuracy();
        assertEquals(0.001, accuracy, 0.00001);
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution distribution = new FDistribution(3.0, 3.0);
        double mean = distribution.getNumericalMean();
        assertEquals(3.0 / 2, mean, 0.001);
    }

    @Test
    public void testFDistributionNumericalVariance() {
        FDistribution distribution = new FDistribution(3.0, 3.0);
        double variance = distribution.getNumericalVariance();
        double expectedVariance = (3.0 * 3.0 * (3.0 + 3.0 - 2)) / (3.0 * (3.0 - 2) * (3.0 - 4));
        assertEquals(expectedVariance, variance, 0.001);
    }

    @Test
    public void testFDistributionNumericalVarianceCalculation() {
        FDistribution distribution = new FDistribution(new Random(), 3.0, 3.0);
        double variance = distribution.getNumericalVariance();
        double expectedVariance = (3.0 * 3.0 * (3.0 + 3.0 - 2)) / (3.0 * (3.0 - 2) * (3.0 - 4));
        assertEquals(expectedVariance, variance, 0.001);
    }

    @Test
    public void testFDistributionSupportBounds() {
        FDistribution distribution = new FDistribution(2.0, 3.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound());
    }

    @Test
    public void testFDistributionSupportBoundsInclusive() {
        FDistribution distribution = new FDistribution(2.0, 3.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionSupportConnected() {
        FDistribution distribution = new FDistribution(2.0, 3.0);
        assertTrue(distribution.isSupportConnected());
    }
}