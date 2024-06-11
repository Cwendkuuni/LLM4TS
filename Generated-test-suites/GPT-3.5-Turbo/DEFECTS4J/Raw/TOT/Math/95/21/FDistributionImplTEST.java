import org.junit.Test;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {

    private static final double EPSILON = 1e-12;

    @Test
    public void testCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 5.0);
        
        // Typical use case
        double result1 = distribution.cumulativeProbability(2.0);
        assertTrue(Math.abs(result1 - 0.704036) < EPSILON);
        
        // Edge case
        double result2 = distribution.cumulativeProbability(0.0);
        assertTrue(Math.abs(result2 - 0.0) < EPSILON);
        
        // Error scenario
        try {
            double result3 = distribution.cumulativeProbability(-1.0);
            fail("MathException should have been thrown");
        } catch (MathException e) {
            // Exception expected
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 5.0);
        
        // Typical use case
        double result1 = distribution.inverseCumulativeProbability(0.4);
        assertTrue(Math.abs(result1 - 1.132004) < EPSILON);
        
        // Edge case
        double result2 = distribution.inverseCumulativeProbability(0.0);
        assertTrue(Math.abs(result2 - 0.0) < EPSILON);
        
        // Error scenario
        try {
            double result3 = distribution.inverseCumulativeProbability(1.5);
            fail("MathException should have been thrown");
        } catch (MathException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 5.0);
        
        // Typical use case
        double result1 = distribution.getDomainLowerBound(0.1);
        assertEquals(0.0, result1, EPSILON);
        
        // Edge case
        double result2 = distribution.getDomainLowerBound(0.0);
        assertEquals(0.0, result2, EPSILON);
    }

    @Test
    public void testGetDomainUpperBound() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 5.0);
        
        // Typical use case
        double result1 = distribution.getDomainUpperBound(0.9);
        assertTrue(result1 > 0);
        
        // Edge case
        double result2 = distribution.getDomainUpperBound(1.0);
        assertTrue(result2 == Double.MAX_VALUE);
    }

    @Test
    public void testGetInitialDomain() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 5.0);
        
        // Typical use case
        double result1 = distribution.getInitialDomain(0.5);
        assertEquals(1.5, result1, EPSILON);
        
        // Edge case
        double result2 = distribution.getInitialDomain(0.0);
        assertEquals(1.25, result2, EPSILON);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 5.0);
        
        // Typical use case
        distribution.setNumeratorDegreesOfFreedom(4.0);
        assertEquals(4.0, distribution.getNumeratorDegreesOfFreedom(), EPSILON);
        
        // Error scenario
        try {
            distribution.setNumeratorDegreesOfFreedom(0.0);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl distribution = new FDistributionImpl(3.0, 5.0);
        
        // Typical use case
        distribution.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), EPSILON);
        
        // Error scenario
        try {
            distribution.setDenominatorDegreesOfFreedom(0.0);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
}