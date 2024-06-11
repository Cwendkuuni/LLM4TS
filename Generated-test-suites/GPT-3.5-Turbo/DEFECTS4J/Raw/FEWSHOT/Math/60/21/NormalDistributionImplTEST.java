import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistribution normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeStandardDeviation() {
        normalDistribution = new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistribution.density(0), 0.0001);
        assertEquals(0.24197072451914337, normalDistribution.density(-1), 0.0001);
        assertEquals(0.05399096651318806, normalDistribution.density(-2), 0.0001);
        assertEquals(0.0044318484119380075, normalDistribution.density(-3), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.15865525393145707, normalDistribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.022750131948179195, normalDistribution.cumulativeProbability(-2), 0.0001);
        assertEquals(0.0013498980316301035, normalDistribution.cumulativeProbability(-3), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 0.0001);
        assertEquals(-1, normalDistribution.inverseCumulativeProbability(0.15865525393145707), 0.0001);
        assertEquals(-2, normalDistribution.inverseCumulativeProbability(0.022750131948179195), 0.0001);
        assertEquals(-3, normalDistribution.inverseCumulativeProbability(0.0013498980316301035), 0.0001);
    }

    @Test
    public void testSample() {
        double sample = normalDistribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY);
        assertTrue(sample <= Double.POSITIVE_INFINITY);
    }
}