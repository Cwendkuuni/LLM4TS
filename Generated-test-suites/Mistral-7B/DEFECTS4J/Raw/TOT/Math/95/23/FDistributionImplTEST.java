package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Beta;
import org.junit.Test;

import org.apache.commons.math.distribution.FDistribution;
import org.apache.commons.math.distribution.FDistributionImpl;

/**
 * Test for {@link FDistributionImpl} class.
 */
public class FDistributionImplTest {

    /**
     * Test for constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for {@link FDistribution#cumulativeProbability(double)} method.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        // typical use cases
        assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.0);
        assertEquals(0.3276, distribution.cumulativeProbability(1.0), 0.001);
        assertEquals(0.975, distribution.cumulativeProbability(2.5), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0);
        // edge cases
        assertEquals(0.5, distribution.cumulativeProbability(Integer.MIN_VALUE), 0.0);
        assertEquals(0.5, distribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0);
        // error scenarios
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(-1.0));
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(Double.NaN));
    }

    /**
     * Test for {@link FDistribution#inverseCumulativeProbability(double)} method.
     */
    @Test
    public void testInverseCumulativeProbability() throws Exception {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        // typical use cases
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(0.3276, distribution.inverseCumulativeProbability(0.3276), 0.001);
        assertEquals(2.5, distribution.inverseCumulativeProbability(0.975), 0.001);
        // edge cases
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.0);
        // error scenarios
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(-1.0));
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(0.4));
    }

    /**
     * Test for {@link FDistribution#getNumeratorDegreesOfFreedom()} method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for {@link FDistribution#getDenominatorDegreesOfFreedom()} method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(3.0, 5.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}