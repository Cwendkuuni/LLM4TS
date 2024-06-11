package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;

import java.util.Random;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        assertNotNull(fDist);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionCreationZeroDegreesOfFreedom() {
        new FDistribution(0.0, 2.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double x = 1.0;
        double density = fDist.density(x);
        assertEquals(0.20924426031173, density, 0.000001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double x = 1.0;
        double cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.69146242344913, cumulativeProbability, 0.000001);
    }

    @Test
    public void testFDistributionCumulativeProbabilityZero() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double x = 0.0;
        double cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(0.0, cumulativeProbability, 0.000001);
    }

    @Test
    public void testFDistributionCumulativeProbabilityInfinite() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double x = Double.POSITIVE_INFINITY;
        double cumulativeProbability = fDist.cumulativeProbability(x);
        assertEquals(1.0, cumulativeProbability, 0.000001);
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double numericalMean = fDist.getNumericalMean();
        assertEquals(1.5, numericalMean, 0.000001);
    }

    @Test
    public void testFDistributionNumericalVariance() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double numericalVariance = fDist.getNumericalVariance();
        assertEquals(1.5865525, numericalVariance, 0.000001);
    }

    @Test
    public void testFDistributionGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double numeratorDegreesOfFreedom = fDist.getNumeratorDegreesOfFreedom();
        assertEquals(2.0, numeratorDegreesOfFreedom, 0.000001);
    }

    @Test
    public void testFDistributionGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double denominatorDegreesOfFreedom = fDist.getDenominatorDegreesOfFreedom();
        assertEquals(3.0, denominatorDegreesOfFreedom, 0.000001);
    }

    @Test
    public void testFDistributionSupportLowerBound() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double supportLowerBound = fDist.getSupportLowerBound();
        assertEquals(0.0, supportLowerBound, 0.000001);
    }

    @Test
    public void testFDistributionSupportUpperBound() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double supportUpperBound = fDist.getSupportUpperBound();
        assertTrue(Double.isPositiveInfinity(supportUpperBound));
    }

    @Test
    public void testFDistributionSupportLowerBoundInclusive() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        boolean isSupportLowerBoundInclusive = fDist.isSupportLowerBoundInclusive();
        assertTrue(isSupportLowerBoundInclusive);
    }

    @Test
    public void testFDistributionSupportUpperBoundInclusive() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        boolean isSupportUpperBoundInclusive = fDist.isSupportUpperBoundInclusive();
        assertFalse(isSupportUpperBoundInclusive);
    }

    @Test
    public void testFDistributionSupportConnected() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        boolean isSupportConnected = fDist.isSupportConnected();
        assertTrue(isSupportConnected);
    }

    @Test
    public void testFDistributionRandomSample() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            double sample = fDist.inverseCumulativeProbability(random.nextDouble());
            assertTrue(sample > 0.0);
        }
    }
}