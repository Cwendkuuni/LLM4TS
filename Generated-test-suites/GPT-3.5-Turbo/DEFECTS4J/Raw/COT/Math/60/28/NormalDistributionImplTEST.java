
import org.apache.commons.math.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        assertEquals(10, normalDistribution.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0.001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        assertEquals(0.120985362, normalDistribution.density(12), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        assertEquals(0.90878878, normalDistribution.cumulativeProbability(12), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        assertEquals(12, normalDistribution.inverseCumulativeProbability(0.90878878), 0.001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10, 2);
        double sample = normalDistribution.sample();
        assertTrue(sample >= 6 && sample <= 14);
    }

}
