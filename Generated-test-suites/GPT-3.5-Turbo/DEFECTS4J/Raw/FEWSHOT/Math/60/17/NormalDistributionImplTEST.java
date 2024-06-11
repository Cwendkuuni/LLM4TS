import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989, normalDistribution.density(-1), 0.0001);
        assertEquals(0.2419, normalDistribution.density(-2), 0.0001);
        assertEquals(0.0539, normalDistribution.density(-3), 0.0001);
        assertEquals(0.0539, normalDistribution.density(3), 0.0001);
        assertEquals(0.2419, normalDistribution.density(2), 0.0001);
        assertEquals(0.3989, normalDistribution.density(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.8413, normalDistribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.9772, normalDistribution.cumulativeProbability(-2), 0.0001);
        assertEquals(0.9987, normalDistribution.cumulativeProbability(-3), 0.0001);
        assertEquals(0.0013, normalDistribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.0228, normalDistribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.1587, normalDistribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        assertEquals(-1, normalDistribution.inverseCumulativeProbability(0.1587), 0.0001);
        assertEquals(-2, normalDistribution.inverseCumulativeProbability(0.0228), 0.0001);
        assertEquals(-3, normalDistribution.inverseCumulativeProbability(0.0013), 0.0001);
        assertEquals(3, normalDistribution.inverseCumulativeProbability(0.9987), 0.0001);
        assertEquals(2, normalDistribution.inverseCumulativeProbability(0.9772), 0.0001);
        assertEquals(1, normalDistribution.inverseCumulativeProbability(0.8413), 0.0001);
    }

    @Test
    public void testSample() {
        double sample = normalDistribution.sample();
        // Do some basic sanity checks on the generated sample
        assertTrue(sample >= -10 && sample <= 10);
    }
}