import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double result = normalDistribution.density(0);
        assertEquals(0.3989422804014327, result, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            double result = normalDistribution.cumulativeProbability(0);
            assertEquals(0.5, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0, result, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        try {
            double result = normalDistribution.sample();
            assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}