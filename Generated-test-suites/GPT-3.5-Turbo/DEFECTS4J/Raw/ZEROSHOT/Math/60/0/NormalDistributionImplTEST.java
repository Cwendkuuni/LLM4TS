import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.24197072451914337, distribution.density(1.0), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.8413447460685429, distribution.cumulativeProbability(1.0), 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.8413447460685429, distribution.inverseCumulativeProbability(0.5), 0);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY);
        assertTrue(sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.25), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.75), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, distribution.getInitialDomain(0.25), 0);
        assertEquals(1, distribution.getInitialDomain(0.75), 0);
        assertEquals(0, distribution.getInitialDomain(0.5), 0);
    }
}