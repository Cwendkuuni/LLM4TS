package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FProbabilityDensityFunction;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumeratorDegreesOfFreedom() {
        new FDistributionImpl(-2.0, 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDenominatorDegreesOfFreedom() {
        new FDistributionImpl(2.0, -3.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double[] xValues = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0};
        double[] probValues = {0.0, 0.1282, 0.3231, 0.4616, 0.4981, 0.5};

        for (int i = 0; i < xValues.length; i++) {
            assertEquals(probValues[i], distribution.cumulativeProbability(xValues[i]), 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testNegativeXValueForCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.cumulativeProbability(-1.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        double[] probValues = {0.0, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99, 0.999};
        double[] xValues = new FProbabilityDensityFunction(distribution).inverseCumulativeProbability(probValues);

        for (int i = 0; i < xValues.length; i++) {
            assertEquals(xValues[i], distribution.inverseCumulativeProbability(probValues[i]), 0.001);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidProbabilityForInverseCumulativeProbability() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.inverseCumulativeProbability(-1.0);
    }

    @Test
    public void testGettersAndSetters() {
        FDistributionImpl distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(4.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(4.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}