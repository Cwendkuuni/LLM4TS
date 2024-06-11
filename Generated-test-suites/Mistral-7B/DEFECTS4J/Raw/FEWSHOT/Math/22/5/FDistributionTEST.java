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
    public void testFDistributionCreation() throws NotStrictlyPositiveException {
        final int numTrials = 10000;
        final double accuracy = 0.0001;

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= i; j++) {
                Random rnd = new Random();
                FDistribution fDist = new FDistribution(i, j, accuracy);
                assertEquals(i, fDist.getNumeratorDegreesOfFreedom(), 0.0);
                assertEquals(j, fDist.getDenominatorDegreesOfFreedom(), 0.0);
                assertEquals(accuracy, fDist.getSolverAbsoluteAccuracy(), 0.0);
                assertNotNull(fDist.getNumericalVariance());
                assertFalse(Double.isNaN(fDist.getNumericalVariance()));
                // Check density function
                for (double x = 0.0; x < 10.0; x += 0.01) {
                    double prob = fDist.density(x);
                    double cumProb = fDist.cumulativeProbability(x);
                    double delta = x * (i / (i + j)) / Beta.betaEstimate(i / 2.0, j / 2.0);
                    double maxError = Math.max(Math.abs(prob - delta), Math.abs(cumProb - delta));
                    assertTrue(maxError <= accuracy);
                }
                // Check inverse cumulative probability function
                for (double p = 0.0; p <= 1.0; p += 0.01) {
                    double x = fDist.inverseCumulativeProbability(p);
                    double delta = Beta.inverseBetaEstimate(p, i / 2.0, j / 2.0) * (i / j);
                    double maxError = Math.abs(x - delta);
                    assertTrue(maxError <= accuracy);
                }
                for (int k = 0; k < numTrials; k++) {
                    double x = fDist.sample();
                    assertTrue(x >= 0.0);
                }
            }
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroDegreesOfFreedom() throws NotStrictlyPositiveException {
        throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, 0);
    }

    @Test
    public void testInfiniteSupport() {
        FDistribution fDist = new FDistribution(1.0, 1.0);
        assertNotNull(fDist.getSupportLowerBound());
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.0);
        assertTrue(fDist.isSupportConnected());
        assertTrue(fDist.isSupportLowerBoundInclusive());
        assertTrue(fDist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testNoVarianceForSmallDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(1.0, 1.0);
        assertNull(fDist.getNumericalVariance());
    }

    @Test
    public void testCalculateNumericalVariance() {
        FDistribution fDist = new FDistribution(10.0, 20.0);
        double variance = fDist.calculateNumericalVariance();
        assertEquals(196.0 / 9.0, variance, 0.001);
    }
}