package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.cumulativeProbability(2.0);
        assertEquals(0.22101684805900464, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1.7773, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.getDomainLowerBound(0.2);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.getDomainUpperBound(0.8);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        double result = fDistribution.getInitialDomain(0.5);
        assertEquals(1.1111, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomWithNegativeValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        fDistribution.setNumeratorDegreesOfFreedom(-5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomWithNegativeValue() {
        FDistributionImpl fDistribution = new FDistributionImpl(5.0, 10.0);
        fDistribution.setDenominatorDegreesOfFreedom(-10.0);
    }

}