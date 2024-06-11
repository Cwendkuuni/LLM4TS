package org.apache.commons.math.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    private final double EPSILON = 1e-9;

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl f = new FDistributionImpl(2, 5);
        assertEquals(0.7639287761035389, f.cumulativeProbability(0.5), EPSILON);
        assertEquals(0.9165824702809592, f.cumulativeProbability(1), EPSILON);
        assertEquals(0.9979678602326021, f.cumulativeProbability(5), EPSILON);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl f = new FDistributionImpl(2, 5);
        assertEquals(0.5, f.inverseCumulativeProbability(0.7639287761035389), EPSILON);
        assertEquals(1, f.inverseCumulativeProbability(0.9165824702809592), EPSILON);
        assertEquals(5, f.inverseCumulativeProbability(0.9979678602326021), EPSILON);
    }

    @Test
    public void testInverseCumulativeProbability_InvalidP() throws MathException {
        FDistributionImpl f = new FDistributionImpl(2, 5);
        assertEquals(0, f.inverseCumulativeProbability(0), EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, f.inverseCumulativeProbability(1), EPSILON);
        try {
            f.inverseCumulativeProbability(-0.1);
            fail("Excepted MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
        try {
            f.inverseCumulativeProbability(1.1);
            fail("Excepted MathException");
        } catch (MathException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl f = new FDistributionImpl(2, 5);
        assertEquals(0.0, f.getDomainLowerBound(0), EPSILON);
        assertEquals(0.0, f.getDomainLowerBound(0.5), EPSILON);
        assertEquals(0.0, f.getDomainLowerBound(1), EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl f = new FDistributionImpl(2, 5);
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(0), EPSILON);
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(0.5), EPSILON);
        assertEquals(Double.MAX_VALUE, f.getDomainUpperBound(1), EPSILON);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl f = new FDistributionImpl(2, 5);
        assertEquals(2, f.getInitialDomain(0.5), EPSILON);
        assertEquals(2, f.getInitialDomain(1), EPSILON);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl f = new FDistributionImpl(2, 5);
        f.setNumeratorDegreesOfFreedom(3);
        assertEquals(3, f.getNumeratorDegreesOfFreedom(), EPSILON);
        try {
            f.setNumeratorDegreesOfFreedom(0);
            fail("Excepted IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl f = new FDistributionImpl(2, 5);
        f.setDenominatorDegreesOfFreedom(6);
        assertEquals(6, f.getDenominatorDegreesOfFreedom(), EPSILON);
        try {
            f.setDenominatorDegreesOfFreedom(0);
            fail("Excepted IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

}