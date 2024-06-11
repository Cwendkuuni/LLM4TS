import org.apache.commons.math.MathException;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(1, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        assertEquals(0.17603266338214976, normalDistribution.density(0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        try {
            assertEquals(0.3085375387259869, normalDistribution.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        try {
            assertEquals(-1.0206342797781537, normalDistribution.inverseCumulativeProbability(0.3), 0.0001);
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSample() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1, 2);
        try {
            assertNotNull(normalDistribution.sample());
        } catch (MathException e) {
            e.printStackTrace();
        }
    }

}