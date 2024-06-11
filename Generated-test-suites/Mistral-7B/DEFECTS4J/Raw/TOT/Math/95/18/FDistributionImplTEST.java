package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FRectangularSamples;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

/**
 * Tests for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test of constructor, when valid degrees of freedom are provided.
     */
    @Test
    public void testConstructValidDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test of constructor, when invalid degrees of freedom are provided.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidDegreesOfFreedom() {
        new FDistributionImpl(-5.0, 10.0);
    }

    /**
     * Test of cumulativeProbability method with valid arguments.
     */
    @Test
    public void testCumulativeProbabilityValidArguments() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cp = fd.cumulativeProbability(x);
            double expected = Beta.regularizedBeta(5.0 * x / (10.0 + 5.0 * x), 2.5, 2.5);
            assertEquals("Unexpected CDF value for x=" + x, expected, cp, 1e-8);
        }
    }

    /**
     * Test of cumulativeProbability method with invalid arguments.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidArguments() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.cumulativeProbability(-1.0);
    }

    /**
     * Test of inverseCumulativeProbability method with valid arguments.
     */
    @Test
    public void testInverseCumulativeProbabilityValidArguments() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = fd.inverseCumulativeProbability(p);
            double expected = Beta.inverseRegularizedBeta(p, 2.5, 2.5);
            assertEquals("Unexpected inverse CDF value for p=" + p, expected, x, 1e-8);
        }
    }

    /**
     * Test of inverseCumulativeProbability method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidArguments() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.inverseCumulativeProbability(-1.0);
        fd.inverseCumulativeProbability(1.1);
    }

    /**
     * Test of getDomainLowerBound, getDomainUpperBound and getInitialDomain methods.
     */
    @Test
    public void testGetDomainBounds() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double lowerBound = fd.getDomainLowerBound(p);
            double upperBound = fd.getDomainUpperBound(p);
            double initialDomain = fd.getInitialDomain(p);
            assertFalse("Unexpected non-zero lower bound for p=" + p, lowerBound != 0.0);
            assertTrue("Unexpected negative lower bound for p=" + p, lowerBound >= 0.0);
            assertTrue("Unexpected non-positive upper bound for p=" + p, upperBound > 0.0);
            assertTrue("Unexpected negative initial domain value for p=" + p, initialDomain > 0.0);
            assertEquals("Unexpected initial domain value for p=" + p,
                    initialDomain, fd.cumulativeProbability(initialDomain), 1e-8);
        }
    }

    /**
     * Test of setNumeratorDegreesOfFreedom and getNumeratorDegreesOfFreedom methods.
     */
    @Test
    public void testSetGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        fd.setNumeratorDegreesOfFreedom(10.0);
        assertEquals(10.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test of setDenominatorDegreesOfFreedom and getDenominatorDegreesOfFreedom methods.
     */
    @Test
    public void testSetGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        fd.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test of getDistributionFunction method.
     */
    @Test
    public void testGetDistributionFunction() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        FRectangularSamples samples = new FRectangularSamples(fd, 100000);
        double sum = 0.0;
        for (int i = 0; i < samples.getSize(); i++) {
            sum += samples.getSample(i);
        }
        assertEquals("Unexpected sum of samples", 1.0, sum, 1e-8);
    }
}