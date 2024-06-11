
package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FDistributionImplTest {

    private FDistributionImpl fd;

    @Before
    public void setUp() {
        fd = new FDistributionImpl(10, 15);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Typical use case
        double result1 = fd.cumulativeProbability(5);
        assertEquals(0.7848, result1, 0.0001);

        // Edge case
        double result2 = fd.cumulativeProbability(0);
        assertEquals(0.0, result2, 0.0001);

        // Error scenario
        try {
            fd.cumulativeProbability(-5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Test passed
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Typical use case
        double result1 = fd.inverseCumulativeProbability(0.5);
        assertEquals(1.2606, result1, 0.0001);

        // Edge cases
        double result2 = fd.inverseCumulativeProbability(0);
        assertEquals(0.0, result2, 0.0001);

        double result3 = fd.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result3, 0.0001);

        // Error scenario
        try {
            fd.inverseCumulativeProbability(-0.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Test passed
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        // Typical use case
        double result1 = fd.getDomainLowerBound(0.2);
        assertEquals(0.0, result1, 0.0001);

        // Edge case
        double result2 = fd.getDomainLowerBound(0);
        assertEquals(0.0, result2, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Typical use case
        double result1 = fd.getDomainUpperBound(0.8);
        assertEquals(Double.MAX_VALUE, result1, 0.0001);

        // Edge case
        double result2 = fd.getDomainUpperBound(1);
        assertEquals(Double.MAX_VALUE, result2, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        // Typical use case
        double result1 = fd.getInitialDomain(0.5);
        assertEquals(1.1111, result1, 0.0001);

        // Edge case
        double result2 = fd.getInitialDomain(0);
        assertEquals(1.0, result2, 0.0001);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        // Typical use case
        fd.setNumeratorDegreesOfFreedom(5.5);
        assertEquals(5.5, fd.getNumeratorDegreesOfFreedom(), 0.0001);

        // Edge case
        try {
            fd.setNumeratorDegreesOfFreedom(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        // Typical use case
        double result1 = fd.getNumeratorDegreesOfFreedom();
        assertEquals(10.0, result1, 0.0001);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        // Typical use case
        fd.setDenominatorDegreesOfFreedom(7.7);
        assertEquals(7.7, fd.getDenominatorDegreesOfFreedom(), 0.0001);

        // Edge case
        try {
            fd.setDenominatorDegreesOfFreedom(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        // Typical use case
        double result1 = fd.getDenominatorDegreesOfFreedom();
        assertEquals(15.0, result1, 0.0001);
    }
}
