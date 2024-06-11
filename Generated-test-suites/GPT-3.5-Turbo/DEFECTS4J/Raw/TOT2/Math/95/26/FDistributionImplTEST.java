import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionImplTest {

    // Test for cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(3.0, 4.0);
        
        // Typical case
        assertEquals(0.0, fDist.cumulativeProbability(0.0), 0.0001);
        
        // Edge case
        assertEquals(0.2703, fDist.cumulativeProbability(1.0), 0.0001);
    }
    
    // Test for inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fDist = new FDistributionImpl(3.0, 4.0);
        
        // Typical case
        assertEquals(0.0, fDist.inverseCumulativeProbability(0.0), 0.0001);
        
        // Exception case
        try {
            fDist.inverseCumulativeProbability(1.0);
            fail("Expected MathException was not thrown");
        } catch (MathException e) {
            // Expected exception
        }
    }
    
    // Test for setNumeratorDegreesOfFreedom method
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(3.0, 4.0);
        
        // Typical case
        fDist.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, fDist.getNumeratorDegreesOfFreedom(), 0.0001);
        
        // Exception case
        try {
            fDist.setNumeratorDegreesOfFreedom(0.0);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    // Test for setDenominatorDegreesOfFreedom method
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistributionImpl fDist = new FDistributionImpl(3.0, 4.0);
        
        // Typical case
        fDist.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, fDist.getDenominatorDegreesOfFreedom(), 0.0001);
        
        // Exception case
        try {
            fDist.setDenominatorDegreesOfFreedom(0.0);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}