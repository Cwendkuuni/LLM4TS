package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl distribution;

    @Before
    public void setup() {
        distribution = new FDistributionImpl(4.0, 6.0);
    }

    // Test cumulativeProbability
    @Test
    public void testCumulativeProbability() throws MathException {
        double result = distribution.cumulativeProbability(0.0);
        assertEquals(0.0, result, 0.0001);

        result = distribution.cumulativeProbability(1.0);
        assertEquals(0.1281, result, 0.0001);

        result = distribution.cumulativeProbability(2.0);
        assertEquals(0.9719, result, 0.0001);

        result = distribution.cumulativeProbability(4.0);
        assertEquals(0.9999, result, 0.0001);

        result = distribution.cumulativeProbability(6.0);
        assertEquals(1.0, result, 0.0001);
    }

    // Test inverseCumulativeProbability
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double result = distribution.inverseCumulativeProbability(0.0);
        assertEquals(0.0, result, 0.0001);

        result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(1.5970, result, 0.0001);

        result = distribution.inverseCumulativeProbability(0.9);
        assertEquals(5.9876, result, 0.0001);

        result = distribution.inverseCumulativeProbability(0.99);
        assertEquals(17.5345, result, 0.0001);

        result = distribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    }

    // Test getDomainLowerBound
    @Test
    public void testGetDomainLowerBound() {
        double result = distribution.getDomainLowerBound(0.0);
        assertEquals(0.0, result, 0.0001);

        result = distribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0001);

        result = distribution.getDomainLowerBound(0.9);
        assertEquals(0.0, result, 0.0001);

        result = distribution.getDomainLowerBound(0.99);
        assertEquals(0.0, result, 0.0001);

        result = distribution.getDomainLowerBound(1.0);
        assertEquals(0.0, result, 0.0001);
    }

    // Test getDomainUpperBound
    @Test
    public void testGetDomainUpperBound() {
        double result = distribution.getDomainUpperBound(0.0);
        assertEquals(Double.MAX_VALUE, result, 0.0001);

        result = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, result, 0.0001);

        result = distribution.getDomainUpperBound(0.9);
        assertEquals(Double.MAX_VALUE, result, 0.0001);

        result = distribution.getDomainUpperBound(0.99);
        assertEquals(Double.MAX_VALUE, result, 0.0001);

        result = distribution.getDomainUpperBound(1.0);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    // Test getInitialDomain
    @Test
    public void testGetInitialDomain() {
        double result = distribution.getInitialDomain(0.0);
        assertEquals(6.0 / 4.0, result, 0.0001);

        result = distribution.getInitialDomain(0.5);
        assertEquals(6.0 / 4.0, result, 0.0001);

        result = distribution.getInitialDomain(0.9);
        assertEquals(6.0 / 4.0, result, 0.0001);

        result = distribution.getInitialDomain(0.99);
        assertEquals(6.0 / 4.0, result, 0.0001);

        result = distribution.getInitialDomain(1.0);
        assertEquals(6.0 / 4.0, result, 0.0001);
    }

    // Test setNumeratorDegreesOfFreedom
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        distribution.setNumeratorDegreesOfFreedom(8.0);
        assertEquals(8.0, distribution.getNumeratorDegreesOfFreedom(), 0.0001);

        distribution.setNumeratorDegreesOfFreedom(10.0);
        assertEquals(10.0, distribution.getNumeratorDegreesOfFreedom(), 0.0001);
    }

    // Test setDenominatorDegreesOfFreedom
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        distribution.setDenominatorDegreesOfFreedom(10.0);
        assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0001);

        distribution.setDenominatorDegreesOfFreedom(12.0);
        assertEquals(12.0, distribution.getDenominatorDegreesOfFreedom(), 0.0001);
    }

    // Test getNumeratorDegreesOfFreedom
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double result = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(4.0, result, 0.0001);

        distribution.setNumeratorDegreesOfFreedom(6.0);
        result = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(6.0, result, 0.0001);
    }

    // Test getDenominatorDegreesOfFreedom
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double result = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(6.0, result, 0.0001);

        distribution.setDenominatorDegreesOfFreedom(8.0);
        result = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(8.0, result, 0.0001);
    }
}