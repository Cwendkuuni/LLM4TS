import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Beta;
import org.junit.BeforeClass;
import org.junit.Test;

public class FDistributionImplTest {
    
    private static FDistributionImpl fDistribution;
    
    @BeforeClass
    public static void setUp() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 10.0;
        fDistribution = new FDistributionImpl(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }
    
    @Test
    public void testCumulativeProbability() {
        // Typical use case
        double x = 2.0;
        double expected = Beta.regularizedBeta(10.0 / (5.0 + 10.0 * 2.0), 2.5, 5.0);
        double actual = fDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: x = 0
        x = 0.0;
        expected = 0.0;
        actual = fDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: x < 0
        x = -1.0;
        expected = 0.0;
        actual = fDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001);
        
        // Error scenario: x = NaN
        x = Double.NaN;
        try {
            fDistribution.cumulativeProbability(x);
        } catch (MathException e) {
            assertTrue(e.getMessage().contains("Not a Number"));
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        // Typical use case: 0 < p < 1
        double p = 0.5;
        double expected = super.inverseCumulativeProbability(p);
        double actual = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: p = 0
        p = 0.0;
        expected = 0.0;
        actual = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: p = 1
        p = 1.0;
        expected = Double.POSITIVE_INFINITY;
        actual = fDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0001);
        
        // Error scenario: p < 0
        p = -0.1;
        try {
            fDistribution.inverseCumulativeProbability(p);
        } catch (MathException e) {
            assertTrue(e.getMessage().contains("Not a valid probability"));
        }
        
        // Error scenario: p > 1
        p = 1.1;
        try {
            fDistribution.inverseCumulativeProbability(p);
        } catch (MathException e) {
            assertTrue(e.getMessage().contains("Not a valid probability"));
        }
    }
    
    @Test
    public void testGetDomainLowerBound() {
        // Typical use case: 0 < p < 1
        double p = 0.5;
        double expected = 0.0;
        double actual = fDistribution.getDomainLowerBound(p);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: p = 0
        p = 0.0;
        expected = 0.0;
        actual = fDistribution.getDomainLowerBound(p);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: p = 1
        p = 1.0;
        expected = 0.0;
        actual = fDistribution.getDomainLowerBound(p);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testGetDomainUpperBound() {
        // Typical use case: 0 < p < 1
        double p = 0.5;
        double expected = Double.MAX_VALUE;
        double actual = fDistribution.getDomainUpperBound(p);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: p = 0
        p = 0.0;
        expected = Double.MAX_VALUE;
        actual = fDistribution.getDomainUpperBound(p);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: p = 1
        p = 1.0;
        expected = Double.MAX_VALUE;
        actual = fDistribution.getDomainUpperBound(p);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testGetInitialDomain() {
        // Typical use case: 0 < p < 1
        double p = 0.5;
        double expected = 10.0 / (10.0 - 2.0);
        double actual = fDistribution.getInitialDomain(p);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: p = 0
        p = 0.0;
        expected = 10.0 / (10.0 - 2.0);
        actual = fDistribution.getInitialDomain(p);
        assertEquals(expected, actual, 0.0001);
        
        // Edge case: p = 1
        p = 1.0;
        expected = 10.0 / (10.0 - 2.0);
        actual = fDistribution.getInitialDomain(p);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        // Typical use case: positive degrees of freedom
        double degreesOfFreedom = 5.0;
        fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        double actual = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(degreesOfFreedom, actual, 0.0001);
        
        // Error scenario: zero degrees of freedom
        degreesOfFreedom = 0.0;
        try {
            fDistribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("degrees of freedom must be positive"));
        }
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        // Typical use case: positive degrees of freedom
        double degreesOfFreedom = 10.0;
        fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
        double actual = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(degreesOfFreedom, actual, 0.0001);
        
        // Error scenario: zero degrees of freedom
        degreesOfFreedom = 0.0;
        try {
            fDistribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("degrees of freedom must be positive"));
        }
    }
}