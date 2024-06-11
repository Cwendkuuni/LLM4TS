import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(1, dist.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.3989422804014327, dist.density(0), 1e-15);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, dist.cumulativeProbability(0), 1e-15);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.inverseCumulativeProbability(0.5), 1e-15);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, dist.getDomainLowerBound(0.4), 1e-15);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(Double.MAX_VALUE, dist.getDomainUpperBound(0.6), 1e-15);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(-1, dist.getInitialDomain(0.4), 1e-15);
    }
}