package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistType;
import org.apache.commons.math3.special.Beta;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution dist = new FDistributionImpl(3.0, 4.0);
        assertEquals(FDistType.F_DISTRIBUTION, dist.getDistributionType());
        assertEquals(3.0, dist.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeNumeratorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(3.0, 4.0);
        dist.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeDenominatorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(3.0, 4.0);
        dist.setDenominatorDegreesOfFreedom(-1.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution dist = new FDistributionImpl(3.0, 4.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cdf = dist.cumulativeProbability(x);
            double pdf = dist.probabilityDensityFunctionValue(x);
            double expectedCdf = Beta.cumulativeProbability(cdf * dist.getNumeratorDegreesOfFreedom() / (dist.getNumeratorDegreesOfFreedom() + dist.getDenominatorDegreesOfFreedom() * x), 0.5 * dist.getNumeratorDegreesOfFreedom(), 0.5 * dist.getDenominatorDegreesOfFreedom());
            assertEquals(expectedCdf, cdf, 0.001);
            assertEquals(pdf, dist.probabilityDensityFunctionValue(x), 0.001);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution dist = new FDistributionImpl(3.0, 4.0);
        for (double p = 0.0; p <= 1.0; p += 0.05) {
            double x = dist.inverseCumulativeProbability(p);
            double cdf = dist.cumulativeProbability(x);
            assertEquals(p, cdf, 0.001);
        }
    }
}