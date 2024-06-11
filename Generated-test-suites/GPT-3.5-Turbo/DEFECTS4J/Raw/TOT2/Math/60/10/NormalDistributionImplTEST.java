import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.0);
        assertEquals(2.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 1.5);
        assertEquals(1.5, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.3989422804014337, distribution.density(0.0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(0.8413447460685429, distribution.cumulativeProbability(1.0), 1e-12);
        } catch (Exception e) {
            fail("Exception thrown during cumulativeProbability test");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            assertEquals(1.0, distribution.inverseCumulativeProbability(0.8413447460685429), 1e-12);
        } catch (Exception e) {
            fail("Exception thrown during inverseCumulativeProbability test");
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        try {
            double sample = distribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        } catch (Exception e) {
            fail("Exception thrown during sample test");
        }
    }

    @Test
    public void testErrorConditions() {
        try {
            NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 0.0);
            fail("Expected NotStrictlyPositiveException was not thrown");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEdgeCases() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        
        // test when x is more than 40 standard deviations from the mean
        try {
            assertEquals(0.0, distribution.cumulativeProbability(-50.0), 0.0);
            assertEquals(1.0, distribution.cumulativeProbability(50.0), 0.0);
        } catch (Exception e) {
            fail("Exception thrown during edge case test");
        }
    }
}