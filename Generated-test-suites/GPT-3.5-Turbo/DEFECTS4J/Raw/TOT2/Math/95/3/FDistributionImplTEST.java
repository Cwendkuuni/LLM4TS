import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testCumulativeProbability() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.cumulativeProbability(1.0);
        assertEquals(0.721501419, result, 0.000001);
        
        // Edge case: x <= 0
        result = fDistribution.cumulativeProbability(-1.0);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        double result = fDistribution.inverseCumulativeProbability(0.5);
        assertEquals(1.033379614, result, 0.000001);
        
        // Edge case: p = 0
        result = fDistribution.inverseCumulativeProbability(0.0);
        assertEquals(0.0, result, 0.0);
        
        // Edge case: p = 1
        result = fDistribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
    }
    
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        fDistribution.setNumeratorDegreesOfFreedom(4.0);
        assertEquals(4.0, fDistribution.getNumeratorDegreesOfFreedom(), 0.0);
        
        // Error scenario: degreesOfFreedom <= 0
        try {
            fDistribution.setNumeratorDegreesOfFreedom(-1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
    
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        // Typical use case
        FDistributionImpl fDistribution = new FDistributionImpl(3.0, 5.0);
        fDistribution.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, fDistribution.getDenominatorDegreesOfFreedom(), 0.0);
        
        // Error scenario: degreesOfFreedom <= 0
        try {
            fDistribution.setDenominatorDegreesOfFreedom(-1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
}