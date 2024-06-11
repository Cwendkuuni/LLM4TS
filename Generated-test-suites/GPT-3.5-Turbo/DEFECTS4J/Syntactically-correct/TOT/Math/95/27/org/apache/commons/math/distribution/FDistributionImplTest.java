import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.MathException;

import static org.junit.Assert.*;

public class FDistributionImplTest {

    private FDistributionImpl fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistributionImpl(3, 5);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Typical use case: x is positive
        assertEquals(0.125, fDistribution.cumulativeProbability(0.5), 0.0001);
        
        // Edge case: x is zero
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0001);
        
        // Error scenario: x is negative
        assertEquals(0.0, fDistribution.cumulativeProbability(-1.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Typical use case: p is between 0 and 1
        assertEquals(0.58371, fDistribution.inverseCumulativeProbability(0.2), 0.0001);
        
        // Edge cases: p is 0 and 1
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0001);
        
        // Error scenario: p is negative
        try {
            fDistribution.inverseCumulativeProbability(-0.2);
            fail("Expecting MathException");
        } catch (MathException e) {
            // expected
        }
        
        // Error scenario: p is greater than 1
        try {
            fDistribution.inverseCumulativeProbability(1.2);
            fail("Expecting MathException");
        } catch (MathException e) {
            // expected
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        // Typical use case: p is between 0 and 1
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.2), 0.0001);
        
        // Edge case: p is 0
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.0), 0.0001);
        
        // Error scenario: p is negative
        try {
            fDistribution.getDomainLowerBound(-0.2);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Error scenario: p is greater than 1
        try {
            fDistribution.getDomainLowerBound(1.2);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetDomainUpperBound() {
        // Typical use case: p is between 0 and 1
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.2), 0.0001);
        
        // Edge case: p is 1
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(1.0), 0.0001);
        
        // Error scenario: p is negative
        try {
            fDistribution.getDomainUpperBound(-0.2);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Error scenario: p is greater than 1
        try {
            fDistribution.getDomainUpperBound(1.2);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetInitialDomain() {
        // Typical use case: p is between 0 and 1
        assertEquals(1.25, fDistribution.getInitialDomain(0.2), 0.0001);
        
        // Error scenario: p is negative
        try {
            fDistribution.getInitialDomain(-0.2);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Error scenario: p is greater than 1
        try {
            fDistribution.getInitialDomain(1.2);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        // Typical use case: positive degrees of freedom
        fDistribution.setNumeratorDegreesOfFreedom(4.5);
        assertEquals(4.5, fDistribution.getNumeratorDegreesOfFreedom(), 0.0001);
        
        // Edge case: degrees of freedom is zero
        try {
            fDistribution.setNumeratorDegreesOfFreedom(0.0);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Edge case: degrees of freedom is negative
        try {
            fDistribution.setNumeratorDegreesOfFreedom(-1.5);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        // Typical use case: positive degrees of freedom
        fDistribution.setDenominatorDegreesOfFreedom(7.5);
        assertEquals(7.5, fDistribution.getDenominatorDegreesOfFreedom(), 0.0001);
        
        // Edge case: degrees of freedom is zero
        try {
            fDistribution.setDenominatorDegreesOfFreedom(0.0);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Edge case: degrees of freedom is negative
        try {
            fDistribution.setDenominatorDegreesOfFreedom(-2.5);
            fail("Expecting IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}