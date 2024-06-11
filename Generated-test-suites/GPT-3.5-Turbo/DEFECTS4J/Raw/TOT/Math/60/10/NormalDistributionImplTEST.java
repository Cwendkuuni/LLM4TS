import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution.getMean(), DELTA);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution.getStandardDeviation(), DELTA);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, distribution.density(0), DELTA);
        assertEquals(0.3520653267642995, distribution.density(-0.5), DELTA);
        assertEquals(0.05399096651318806, distribution.density(2), DELTA);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), DELTA);
        assertEquals(0.3085375387259869, distribution.cumulativeProbability(-0.5), DELTA);
        assertEquals(0.9772498680518208, distribution.cumulativeProbability(2), DELTA);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.inverseCumulativeProbability(0), DELTA);
        assertEquals(-0.5244005127080409, distribution.inverseCumulativeProbability(0.3), DELTA);
        assertEquals(1.959963984540054, distribution.inverseCumulativeProbability(0.975), DELTA);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }
}