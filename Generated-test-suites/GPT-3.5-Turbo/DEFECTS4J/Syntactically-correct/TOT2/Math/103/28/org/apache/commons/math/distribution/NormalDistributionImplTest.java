import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    // Test case for getMean method
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), 0.0);
    }

    // Test case for setMean method
    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(10.0);
        assertEquals(10.0, distribution.getMean(), 0.0);
    }

    // Test case for getStandardDeviation method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    // Test case for setStandardDeviation method
    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        assertEquals(3.0, distribution.getStandardDeviation(), 0.0);
    }

    // Test case for cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            double result = distribution.cumulativeProbability(1.96);
            assertEquals(0.975, result, 0.001);
        } catch (MathException e) {
            fail("An exception was thrown");
        }
    }

    // Test case for inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            double result = distribution.inverseCumulativeProbability(0.975);
            assertEquals(1.96, result, 0.01);
        } catch (MathException e) {
            fail("An exception was thrown");
        }
    }

    // Test case for inverseCumulativeProbability method with p = 0
    @Test
    public void testInverseCumulativeProbabilityWithPZero() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            double result = distribution.inverseCumulativeProbability(0.0);
            assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
        } catch (MathException e) {
            fail("An exception was thrown");
        }
    }

    // Test case for inverseCumulativeProbability method with p = 1
    @Test
    public void testInverseCumulativeProbabilityWithPOne() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            double result = distribution.inverseCumulativeProbability(1.0);
            assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
        } catch (MathException e) {
            fail("An exception was thrown");
        }
    }

}