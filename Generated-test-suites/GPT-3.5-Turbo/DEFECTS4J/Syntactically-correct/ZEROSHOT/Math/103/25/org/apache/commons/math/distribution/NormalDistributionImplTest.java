import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(1, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3);
        assertEquals(3, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(1, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3);
        assertEquals(3, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0.5, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.158655, distribution.cumulativeProbability(-1), 0.0001);
        assertEquals(0.841345, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, distribution.inverseCumulativeProbability(0.158655), 0.0001);
        assertEquals(1, distribution.inverseCumulativeProbability(0.841345), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.25), 0.0001);
        assertEquals(0, distribution.getDomainLowerBound(0.75), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(0, distribution.getDomainUpperBound(0.25), 0.0001);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.75), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        assertEquals(-1, distribution.getInitialDomain(0.25), 0.0001);
        assertEquals(1, distribution.getInitialDomain(0.75), 0.0001);
        assertEquals(0, distribution.getInitialDomain(0.5), 0.0001);
    }
}