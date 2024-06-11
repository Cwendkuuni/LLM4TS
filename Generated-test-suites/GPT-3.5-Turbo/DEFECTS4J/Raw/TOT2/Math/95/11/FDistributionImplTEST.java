import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(0.3173, fDistribution.cumulativeProbability(0.5), 0.0001);

        // Edge case: x = 0
        assertEquals(0.0, fDistribution.cumulativeProbability(0.0), 0.0);
        
        // Exception handling: Negative x
        try {
            fDistribution.cumulativeProbability(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Test passed
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(0.72, fDistribution.inverseCumulativeProbability(0.4), 0.01);

        // Edge case: p = 0
        assertEquals(0.0, fDistribution.inverseCumulativeProbability(0.0), 0.0);
        
        // Edge case: p = 1
        assertEquals(Double.POSITIVE_INFINITY, fDistribution.inverseCumulativeProbability(1.0), 0.0);
        
        // Exception handling: Invalid p
        try {
            fDistribution.inverseCumulativeProbability(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Test passed
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(0.0, fDistribution.getDomainLowerBound(0.3), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(Double.MAX_VALUE, fDistribution.getDomainUpperBound(0.3), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        assertEquals(1.5, fDistribution.getInitialDomain(0.2), 0.01);
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setNumeratorDegreesOfFreedom(4);
        assertEquals(4.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);

        // Exception handling: Non-positive degrees of freedom
        try {
            fDistribution.setNumeratorDegreesOfFreedom(-2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(2, 3);
        fDistribution.setDenominatorDegreesOfFreedom(5);
        assertEquals(5.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);

        // Exception handling: Non-positive degrees of freedom
        try {
            fDistribution.setDenominatorDegreesOfFreedom(-3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

}