package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Tests for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Tests {@link FDistributionImpl#FDistributionImpl(double, double)} with valid
     * inputs.
     */
    @Test
    public void testConstruction() {
        final double numeratorDegreesOfFreedom = 4.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistributionImpl(numeratorDegreesOfFreedom,
                denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);

        // Test cumulative probability
        double x = 2.5;
        double cdf = 0.95;
        double result = 0.0;
        try {
            result = distribution.cumulativeProbability(x);
        } catch (MathException e) {
            fail("Exception should not be thrown");
        }
        double betaResult = Beta.regularizedBeta((numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x),
                0.5 * numeratorDegreesOfFreedom,
                0.5 * denominatorDegreesOfFreedom);
        assertEquals(cdf, result, 1e-6);
        assertEquals(cdf, betaResult, 1e-6);

        // Test inverse cumulative probability
        double p = cdf;
        double inverseResult = 0.0;
        try {
            inverseResult = distribution.inverseCumulativeProbability(p);
        } catch (MathException e) {
            fail("Exception should not be thrown");
        }
        assertEquals(x, inverseResult, 1e-6);
    }

    /**
     * Tests {@link FDistributionImpl#FDistributionImpl(double, double)} with invalid
     * input (negative degrees of freedom).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDegreesOfFreedom() {
        final double numeratorDegreesOfFreedom = -2.0;
        final double denominatorDegreesOfFreedom = 5.0;
        new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    /**
     * Tests {@link FDistributionImpl#cumulativeProbability(double)} with invalid
     * input (negative x).
     */
    @Test(expected = MathException.class)
    public void testNegativeX() {
        final double numeratorDegreesOfFreedom = 4.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        distribution.cumulativeProbability(-1.0);
    }

    /**
     * Tests {@link FDistributionImpl#inverseCumulativeProbability(double)} with
     * invalid input (p < 0 or p > 1).
     */
    @Test(expected = MathException.class)
    public void testInvalidProbability() {
        final double numeratorDegreesOfFreedom = 4.0;
        final double denominatorDegreesOfFreedom = 5.0;
        FDistribution distribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        distribution.inverseCumulativeProbability(0.0);
        distribution.inverseCumulativeProbability(1.2);
    }
}