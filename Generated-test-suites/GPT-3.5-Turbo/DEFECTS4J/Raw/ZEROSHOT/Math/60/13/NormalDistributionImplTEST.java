import static org.junit.Assert.*;

import org.junit.Test;

public class NormalDistributionImplTest {

    // test cases for getMean() method
    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertEquals(2, dist.getMean(), 0);
    }

    // test cases for getStandardDeviation() method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertEquals(3, dist.getStandardDeviation(), 0);
    }

    // test cases for density() method
    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertEquals(0.05399096651318806, dist.density(1), 0);
        assertEquals(0.3989422804014327, dist.density(2), 0);
        assertEquals(0.05399096651318806, dist.density(3), 0);
    }

    // test cases for cumulativeProbability() method
    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertEquals(0.15865525393145705, dist.cumulativeProbability(1), 0);
        assertEquals(0.5, dist.cumulativeProbability(2), 0);
        assertEquals(0.8413447460685429, dist.cumulativeProbability(3), 0);
    }

    // test cases for inverseCumulativeProbability() method
    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertEquals(1.001349985586977, dist.inverseCumulativeProbability(0.1), 0);
        assertEquals(2.0, dist.inverseCumulativeProbability(0.5), 0);
        assertEquals(2.998650014413023, dist.inverseCumulativeProbability(0.9), 0);
    }

    // test cases for sample() method
    @Test
    public void testSample() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertTrue(dist.sample() >= -Double.MAX_VALUE);
        assertTrue(dist.sample() <= Double.MAX_VALUE);
    }

    // test cases for getDomainLowerBound() method
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0), 0);
        assertEquals(2.0, dist.getDomainLowerBound(0.5), 0);
    }

    // test cases for getDomainUpperBound() method
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertEquals(2.0, dist.getDomainUpperBound(0), 0);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.5), 0);
    }

    // test cases for getInitialDomain() method
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(2, 3);
        assertEquals(-1.0, dist.getInitialDomain(0), 0);
        assertEquals(3.0, dist.getInitialDomain(0.5), 0);
    }
}