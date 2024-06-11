import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class FDistributionImplTest {
    
    @Test
    public void testCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        try {
            assertEquals(0.0, fd.cumulativeProbability(-1), 0.0001);
            assertEquals(0.9669, fd.cumulativeProbability(1), 0.0001); // Typical use case
            // Add more test cases for edge cases and error scenarios
        } catch (MathException e) {
            fail("Cumulative Probability calculation failed.");
        }
    }
    
    @Test
    public void testInverseCumulativeProbability() {
        FDistributionImpl fd = new FDistributionImpl(5, 10);
        try {
            assertEquals(0.0, fd.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, fd.inverseCumulativeProbability(1), 0.0001);
            // Add more test cases for edge cases and error scenarios
        } catch (MathException e) {
            fail("Inverse Cumulative Probability calculation failed.");
        }
    }
    
    // Add more test cases for other methods like getDomainLowerBound, getDomainUpperBound, etc.
    
}