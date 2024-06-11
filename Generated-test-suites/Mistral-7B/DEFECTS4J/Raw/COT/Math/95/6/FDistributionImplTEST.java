package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for the {@link FDistributionImpl} class.
 */
public class FDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid inputs.
     */
    @Test
    public void testCumulativeProbabilityValidInputs() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        for (double x = 0.0; x <= 10.0; x += 0.1) {
            double cumulativeProbability = 0.0;
            try {
                cumulativeProbability = distribution.cumulativeProbability(x);
            } catch (MathException e) {
                // Should not occur for valid inputs
            }
            double expectedCumulativeProbability = StatUtils.cdf(x, 5.0, 10.0);
            assertEquals(Precision.round(expectedCumulativeProbability, 5),
                    Precision.round(cumulativeProbability, 5), 0.0);
        }
    }

    /**
     * Test cumulativeProbability method with negative input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInput() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        double x = -1.0;
        distribution.cumulativeProbability(x);
    }

    /**
     * Test inverseCumulativeProbability method with valid inputs.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInputs() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double inverseCumulativeProbability = 0.0;
            try {
                inverseCumulativeProbability = distribution.inverseCumulativeProbability(p);
            } catch (MathException e) {
                // Should not occur for valid inputs
            }
            double expectedInverseCumulativeProbability = StatUtils.percentile(p, 5.0, 10.0);
            assertEquals(Precision.round(expectedInverseCumulativeProbability, 5),
                    Precision.round(inverseCumulativeProbability, 5), 0.0);
        }
    }

    /**
     * Test inverseCumulativeProbability method with invalid inputs.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInputs() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(-0.1));
        assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(1.1));
        assertThrows(IllegalArgumentException.class, () -> distribution.setNumeratorDegreesOfFreedom(-1.0));
        assertThrows(IllegalArgumentException.class, () -> distribution.setDenominatorDegreesOfFreedom(-1.0));
    }

    /**
     * Test setNumeratorDegreesOfFreedom method with valid inputs.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedomValidInputs() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        double newNumeratorDegreesOfFreedom = 7.0;
        distribution.setNumeratorDegreesOfFreedom(newNumeratorDegreesOfFreedom);
        assertEquals(newNumeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom method with valid inputs.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedomValidInputs() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        double newDenominatorDegreesOfFreedom = 12.0;
        distribution.setDenominatorDegreesOfFreedom(newDenominatorDegreesOfFreedom);
        assertEquals(newDenominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

}