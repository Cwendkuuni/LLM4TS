package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FServiceRegistry;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Test for {@link FDistributionImpl} class.
 */
public class FDistributionImplTest {

    /**
     * Test for constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test for cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        double result = 0.0;
        for (double x = 0.0; x <= 4.0; x += 0.1) {
            result = fd.cumulativeProbability(x);
            assertEquals(Math.round(result * 1000) / 1000.0, Beta.regularizedBeta((2.0 * x) / (3.0 + 2.0 * x), 1.0, 1.5), 0.001);
        }
    }

    /**
     * Test for cumulativeProbability method with negative input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInput() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        fd.cumulativeProbability(-1.0);
    }

    /**
     * Test for inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        double p = 0.1;
        double result = fd.inverseCumulativeProbability(p);
        assertEquals(Math.round(result * 1000) / 1000.0, Beta.incompleteBetaQuantile(p, 1.5, 2.0), 0.001);

        p = 0.95;
        result = fd.inverseCumulativeProbability(p);
        assertEquals(Math.round(result * 1000) / 1000.0, Beta.incompleteBetaQuantile(1.0 - p, 1.5, 2.0), 0.001);
    }

    /**
     * Test for inverseCumulativeProbability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        fd.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test for inverseCumulativeProbability method with invalid probability.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        fd.inverseCumulativeProbability(1.2);
    }

    /**
     * Test for setNumeratorDegreesOfFreedom method with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegativeInput() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    /**
     * Test for setNumeratorDegreesOfFreedom method with valid input.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedomValidInput() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        fd.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test for setDenominatorDegreesOfFreedom method with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegativeInput() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }

    /**
     * Test for setDenominatorDegreesOfFreedom method with valid input.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedomValidInput() {
        FDistribution fd = new FDistributionImpl(2.0, 3.0);
        fd.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
    }
}