import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10.0, 2.0);
        assertEquals(10.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.0001);
        
        // Test exception handling
        try {
            distribution.setStandardDeviation(0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0001);
        } catch (MathException e) {
            fail("Failed to calculate cumulative probability");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0.0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0001);
        } catch (MathException e) {
            fail("Failed to calculate inverse cumulative probability");
        }
    }

}