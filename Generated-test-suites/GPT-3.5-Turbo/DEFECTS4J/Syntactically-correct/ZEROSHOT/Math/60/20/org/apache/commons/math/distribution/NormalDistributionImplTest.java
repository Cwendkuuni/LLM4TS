import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), 1e-9);
        assertEquals(0.24197072451914337, normalDistribution.density(1), 1e-9);
        assertEquals(0.05399096651318806, normalDistribution.density(3), 1e-9);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0, normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 1e-9);
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 1e-9);
        assertEquals(0.8413447460685429, normalDistribution.cumulativeProbability(1), 1e-9);
        assertEquals(0.9999683287581669, normalDistribution.cumulativeProbability(6), 1e-9);
        assertEquals(1, normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 1e-9);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 1e-9);
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0.5), 1e-9);
        assertEquals(1, normalDistribution.inverseCumulativeProbability(0.8413447460685429), 1e-9);
        assertEquals(6, normalDistribution.inverseCumulativeProbability(0.9999683287581669), 1e-9);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 1e-9);
    }

    @Test
    public void testSample() throws MathException {
        double sample = normalDistribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY);
        assertTrue(sample <= Double.POSITIVE_INFINITY);
    }
}