import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(1.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(0.19947114, distribution.density(0.0), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(0.3085375, distribution.cumulativeProbability(0.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(-3.2897073, distribution.inverseCumulativeProbability(0.001), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        double sample = distribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY);
        assertTrue(sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.0), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.0), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1.0, 2.0);
        assertEquals(-1.0, distribution.getInitialDomain(0.0), 0.0001);
    }
}