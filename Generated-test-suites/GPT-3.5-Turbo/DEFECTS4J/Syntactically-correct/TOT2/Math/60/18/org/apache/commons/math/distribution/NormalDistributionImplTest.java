import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    // Test case for getMean method
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution.getMean(), 0.0);
    }

    // Test case for getStandardDeviation method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0.0);
    }

    // Test case for density method
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), 0.0001);
    }

    // Test case for cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, distribution.cumulativeProbability(0), 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test case for inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test case for sample method
    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double sample = distribution.sample();
            // Since it's a random value, just check if it's within a specific range
            assertTrue(sample >= -10 && sample <= 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}