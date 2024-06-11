import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.PoissonDistribution;
import org.apache.commons.math.distribution.PoissonDistributionImpl;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4.5);
        assertEquals(4.5, dist.getMean(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMeanNegative() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(-4.5);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        assertEquals(0.01831563888873418, dist.probability(3), 0.0000001);
    }

    @Test
    public void testProbabilityInvalidValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        assertTrue(Double.isNaN(dist.probability(-3)));
        assertTrue(Double.isNaN(dist.probability(Integer.MAX_VALUE)));
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        assertEquals(0.6288365228, dist.cumulativeProbability(5), 0.0000001);
    }

    @Test
    public void testCumulativeProbabilityInvalidValue() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        assertEquals(0.0, dist.cumulativeProbability(-5), 0.0000001);
        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), 0.0000001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        assertEquals(0.7559317357, dist.normalApproximateProbability(5), 0.0000001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        int sample = dist.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        assertEquals(0, dist.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(4);
        assertEquals(Integer.MAX_VALUE, dist.getDomainUpperBound(0.5));
    }
}