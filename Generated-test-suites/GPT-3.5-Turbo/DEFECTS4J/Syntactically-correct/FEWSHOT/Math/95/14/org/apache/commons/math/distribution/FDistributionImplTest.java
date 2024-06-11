
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double result = fDistribution.cumulativeProbability(0.5);
        assertEquals(0.33503401570031747, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1.052273600679027, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double result = fDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double result = fDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(3.3333333333333335, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        fDistribution.setNumeratorDegreesOfFreedom(-5);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(5.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        fDistribution.setDenominatorDegreesOfFreedom(-10);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDistribution = new FDistributionImpl(5, 10);
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(10.0, result, 0.0001);
    }

}
