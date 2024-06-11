package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

/**
 * Test cases for FDistributionImpl class.
 */
public class FDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, dist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, dist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid inputs.
     */
    @Test
    public void testCumulativeProbabilityValidInputs() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        
        double[] xValues = {0.5, 1.0, 2.0, StatUtils.PERCENTILE_95, StatUtils.PERCENTILE_99};
        double[] expectedProbabilities = {0.3989, 0.6827, 0.9532, 0.9993, 1.0};
        
        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double actualProbability = dist.cumulativeProbability(x);
            assertEquals(expectedProbabilities[i], actualProbability, 0.001);
        }
    }

    /**
     * Test cumulativeProbability method with invalid input (negative number).
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeNumber() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        dist.cumulativeProbability(-1.0);
    }

    /**
     * Test inverseCumulativeProbability method with valid inputs.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInputs() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        
        double[] probabilities = {0.01, 0.5, 0.99};
        double[] expectedValues = {0.4747, 1.5864, 3.6312};
        
        for (int i = 0; i < probabilities.length; i++) {
            double p = probabilities[i];
            double x = dist.inverseCumulativeProbability(p);
            assertEquals(expectedValues[i], x, 0.001);
        }
    }

    /**
     * Test inverseCumulativeProbability method with invalid input (negative number).
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeNumber() throws MathException {
        FDistributionImpl dist = new FDistributionImpl(2.0, 4.0);
        dist.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test cumulativeProbability method with invalid input (non-positive numerator or denominator degrees of freedom).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumeratorOrDenominatorDegreesOfFreedom() {
        FDistributionImpl dist = new FDistributionImpl(-2.0, 4.0);
    }

}