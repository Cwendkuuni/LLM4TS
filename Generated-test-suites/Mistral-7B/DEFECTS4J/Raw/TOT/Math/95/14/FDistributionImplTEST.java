package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistType;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

/**
 * Tests for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test construction with valid parameters.
     */
    @Test
    public void testConstruction() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        assertNotNull(distribution);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test construction with invalid parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstruction() {
        new FDistributionImpl(-1.0, 4.0);
    }

    /**
     * Test getting and setting degrees of freedom.
     */
    @Test
    public void testGetSetDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        distribution.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test inverse cumulative probability with valid parameters.
     * @throws MathException if the inverse cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double p = 0.95;
        double x = distribution.inverseCumulativeProbability(p);
        assertNotNull(x);
        // check values based on F-distribution formula
        double n = distribution.getNumeratorDegreesOfFreedom();
        double m = distribution.getDenominatorDegreesOfFreedom();
        double d = m / (m + n);
        double df = Math.sqrt(2.0 * m * (Math.log(1.0 - p) + Beta.logBeta(n / 2.0, m / 2.0) - Math.log(Beta.regularizedBeta(n / 2.0, m / 2.0))));
        double expectedX = d * (1.0 + df);
        assertEquals(expectedX, x, 0.0001);
    }

    /**
     * Test inverse cumulative probability with invalid parameters.
     * @throws MathException if the inverse cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     * @throws IllegalArgumentException if <code>p</code> is not a valid
     *         probability.
     */
    @Test(expected = MathException.class)
    public void testInvalidInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double p = 1.1;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test inverse cumulative probability with p=0 and p=1.
     * @throws MathException if the inverse cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double p = 0.0;
        double x = distribution.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.0);
        p = 1.0;
        x = distribution.inverseCumulativeProbability(p);
        assertTrue(Double.isInfinite(x));
    }

    /**
     * Test cumulative probability with valid parameters.
     * @throws MathException if the cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double x = 2.5;
        double p = distribution.cumulativeProbability(x);
        // check values based on F-distribution formula
        double n = distribution.getNumeratorDegreesOfFreedom();
        double m = distribution.getDenominatorDegreesOfFreedom();
        double d = Beta.regularizedBeta((n * x) / (m + n * x), 0.5 * n, 0.5 * m);
        assertEquals(d, p, 0.0001);
    }

    /**
     * Test cumulative probability with invalid parameters.
     * @throws MathException if the cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     */
    @Test(expected = MathException.class)
    public void testInvalidCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.cumulativeProbability(-1.0);
    }

    /**
     * Test cumulative probability with edge cases.
     * @throws MathException if the cumulative probability can not be
     *            computed due to convergence or other numerical errors.
     */
    @Test
    public void testCumulativeProbabilityEdgeCases() throws MathException {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double x = 0.0;
        double p = distribution.cumulativeProbability(x);
        assertEquals(0.0, p, 0.0);
        x = Double.POSITIVE_INFINITY;
        p = distribution.cumulativeProbability(x);
        assertTrue(p == 1.0 || Double.isNaN(p));
    }
}