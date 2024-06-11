import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(3.0, 2.0);
        assertEquals(3.0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(5.0);
        assertEquals(5.0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(3.0, 2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(3.0);
        assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.0, normalDistribution.inverseCumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithInvalidInput() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            normalDistribution.inverseCumulativeProbability(-1);
            fail("Expected IllegalArgumentException");
        } catch (MathException e) {
            fail("Unexpected exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}