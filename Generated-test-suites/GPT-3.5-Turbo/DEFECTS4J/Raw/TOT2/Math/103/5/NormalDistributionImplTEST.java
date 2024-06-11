import static org.junit.Assert.*;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(10, 2);
        assertEquals(10, normalDist.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setMean(5);
        assertEquals(5, normalDist.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(10, 2);
        assertEquals(2, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl();
        normalDist.setStandardDeviation(3);
        assertEquals(3, normalDist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, normalDist.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown during cumulativeProbability test");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0, normalDist.inverseCumulativeProbability(0), 0.0001);
            assertEquals(Double.POSITIVE_INFINITY, normalDist.inverseCumulativeProbability(1), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown during inverseCumulativeProbability test");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityEdgeCases() {
        NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDist.inverseCumulativeProbability(0.5), 0.0001);
        } catch (MathException e) {
            fail("Exception thrown during inverseCumulativeProbability edge case test");
        }
    }
}