import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5.0, distribution.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.398942, distribution.density(0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(0.5, distribution.cumulativeProbability(0), 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0.001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        try {
            // Sample should not throw an exception
            distribution.sample();
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

}