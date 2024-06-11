import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 3);
        assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 3);
        assertEquals(3, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014337, distribution.density(0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, distribution.cumulativeProbability(0), 1e-15);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0, distribution.inverseCumulativeProbability(0), 1e-15);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            double sample = distribution.sample();
            // Test that sample is not NaN or Infinite
            assertEquals(false, Double.isNaN(sample));
            assertEquals(false, Double.isInfinite(sample));
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbabilityException() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            distribution.inverseCumulativeProbability(-1);
        } catch (MathException e) {
            // Test that exception type is OutOfRangeException
            assertEquals("org.apache.commons.math.exception.OutOfRangeException", e.getClass().getName());
        }
    }

}