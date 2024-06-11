import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(2, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(0.05399096651318806, distribution.density(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(0.02275013194817921, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(0.9710624191935675, distribution.inverseCumulativeProbability(0.8), 0.0001);
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        double sample = distribution.sample();
        assertTrue(sample <= 3);
        assertTrue(sample >= 1);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.3), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.7), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 1);
        assertEquals(3, distribution.getInitialDomain(0.8), 0);
    }
}