import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(5.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.3989422804014327, distribution.density(0.0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.0);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            double sample = distribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

}