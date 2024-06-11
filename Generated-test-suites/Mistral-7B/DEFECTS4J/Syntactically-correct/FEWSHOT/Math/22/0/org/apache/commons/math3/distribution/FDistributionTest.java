package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroNumeratorDegreesOfFreedom() {
        new FDistribution(0.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDenominatorDegreesOfFreedom() {
        new FDistribution(2.0, 0.0);
    }

    @Test
    public void testFDistributionDefault() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(3.0, dist.getDenominatorDegreesOfFreedom(), 0.001);
        assertEquals(0.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testFDistributionNonZeroMean() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        assertEquals(3.0 / 2.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testFDistributionNonZeroVariance() {
        FDistribution dist = new FDistribution(3.0, 4.0);
        double numericalVariance = dist.getNumericalVariance();
        double analyticalVariance = (2.0 * 4.0 * 4.0) / (3.0 * 1.0 * 1.0 * (4.0 - 2.0) * (4.0 - 2.0));
        assertEquals(analyticalVariance, numericalVariance, 0.001);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double density = dist.density(1.5);
        double expectedDensity = FastMath.exp(2.0 * FastMath.log(2.0) + 2.0 * FastMath.log(1.5) -
                FastMath.log(1.5) + 1.5 * FastMath.log(2.0) - 1.5 * FastMath.log(3.0) -
                3.0 * FastMath.log(3.0) - Beta.logBeta(1.5, 1.5)) / (2.0 * FastMath.SQRT_2PI);
        assertEquals(expectedDensity, density, 0.001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution dist = new FDistribution(2.0, 3.0);
        double cumulativeProbability = dist.cumulativeProbability(1.5);
        double expectedCumulativeProbability = Beta.regularizedBeta(1.5 * 1.5 * 1.5 / (3.0 * 3.0 + 2.0 * 1.5 * 1.5),
                0.5 * 2.0, 0.5 * 3.0);
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
    }
}