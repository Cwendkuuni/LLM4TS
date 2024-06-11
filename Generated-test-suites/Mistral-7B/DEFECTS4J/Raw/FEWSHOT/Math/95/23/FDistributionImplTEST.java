package org.apache.commons.math.distribution;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.special.Beta;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(2.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        double probability = distribution.cumulativeProbability(0.5);
        assertEquals(0.6699841626751697, probability, 0.0000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        double probability = 0.95;
        double expectedValue = 2.125198528316328;
        double result = distribution.inverseCumulativeProbability(probability);
        assertEquals(expectedValue, result, 0.000001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        distribution.setNumeratorDegreesOfFreedom(3.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        distribution.setDenominatorDegreesOfFreedom(3.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        distribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(1.0, 2.0);
        distribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}