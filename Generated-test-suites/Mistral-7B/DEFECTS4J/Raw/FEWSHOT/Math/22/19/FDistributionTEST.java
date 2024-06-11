package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidNDegreesOfFreedom() {
        new FDistribution(-1, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidDEgreesOfFreedom() {
        new FDistribution(2, -1);
    }

    @Test
    public void testFDistributionBasic() {
        FDistribution dist = new FDistribution(5, 6);
        assertEquals(5, dist.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(6, dist.getDenominatorDegreesOfFreedom(), 0.001);
        assertEquals(1.0 / 30.0, dist.density(1.0), 0.001);
        assertEquals(0.9, dist.cumulativeProbability(3.0), 0.001);
        assertEquals(1.5, dist.inverseCumulativeProbability(0.95), 0.001);
    }

    @Test
    public void testFDistributionDensities() {
        FDistribution dist = new FDistribution(5, 6);
        double[] points = new double[1000];
        double densitySum = 0.0;
        for (int i = 0; i < points.length; i++) {
            points[i] = FastMath.nextGaussian(dist.getMean(), dist.getStandardDeviation());
            densitySum += dist.density(points[i]);
        }
        assertEquals(1.0, densitySum, 0.01);
    }

    @Test
    public void testFDistributionMeanAndVariance() {
        FDistribution dist = new FDistribution(5, 6);
        assertEquals(3.0, dist.getNumericalMean(), 0.01);
        assertEquals(5.27416824165991 / (5.0 * 4.0), dist.getNumericalVariance(), 0.01);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(0.15865525, dist.cumulativeProbability(2.33), 0.001);
        assertEquals(0.99660732, dist.cumulativeProbability(6.68), 0.001);
    }

    @Test
    public void testFDistributionInverseCumulativeProbability() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(1.35960267, dist.inverseCumulativeProbability(0.95), 0.001);
        assertEquals(0.15865525, dist.inverseCumulativeProbability(0.05), 0.001);
    }

    @Test
    public void testFDistributionGetSupportBounds() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, dist.getSupportUpperBound(), 0.0);
        assertEquals(true, dist.isSupportLowerBoundInclusive());
        assertEquals(false, dist.isSupportUpperBoundInclusive());
        assertEquals(true, dist.isSupportConnected());
    }

    @Test
    public void testFDistributionCalculateNumericalVariance() {
        FDistribution dist = new FDistribution(3, 4);
        assertEquals(5.27416824165991 / (3.0 * 2.0), dist.calculateNumericalVariance(), 0.01);
    }
}