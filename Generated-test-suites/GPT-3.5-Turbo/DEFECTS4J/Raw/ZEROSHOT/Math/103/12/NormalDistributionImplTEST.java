import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.3085, distribution.cumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.6745, distribution.inverseCumulativeProbability(0.75), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.25), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.75), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.0, distribution.getInitialDomain(0.25), 0.0001);
    }

}