
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        double mean = normalDistribution.getMean();
        assertEquals(1, mean, 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        double standardDeviation = normalDistribution.getStandardDeviation();
        assertEquals(2, standardDeviation, 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        double density = normalDistribution.density(3);
        assertEquals(0.12098536225957168, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        try {
            double cumulativeProbability = normalDistribution.cumulativeProbability(2);
            assertEquals(0.6914624612740131, cumulativeProbability, 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        try {
            double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(1.0, inverseCumulativeProbability, 0.0001);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        try {
            double sample = normalDistribution.sample();
            assertNotNull(sample);
        } catch (MathException e) {
            fail("Exception thrown");
        }
    }

}
