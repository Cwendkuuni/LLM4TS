import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    NormalDistributionImpl distribution;

    @Test
    public void testGetMean() {
        distribution = new NormalDistributionImpl();
        assertEquals(0.0, distribution.getMean(), 0.0);
        distribution.setMean(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        distribution = new NormalDistributionImpl();
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0);
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationInvalid() {
        distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.5, distribution.cumulativeProbability(0.0), 0.0001);
        assertEquals(0.99996833, distribution.cumulativeProbability(5.0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, distribution.inverseCumulativeProbability(0.0), 0.0001);
        assertEquals(0.0, distribution.inverseCumulativeProbability(0.5), 0.0001);
        assertEquals(Double.MAX_VALUE, distribution.inverseCumulativeProbability(1.0), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.0), 0.0001);
        assertEquals(0.0, distribution.getDomainLowerBound(0.5), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getDomainUpperBound(0.5), 0.0001);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(1.0), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(-1.0, distribution.getInitialDomain(0.0), 0.0001);
        assertEquals(1.0, distribution.getInitialDomain(1.0), 0.0001);
        assertEquals(0.0, distribution.getInitialDomain(0.5), 0.0001);
    }
}