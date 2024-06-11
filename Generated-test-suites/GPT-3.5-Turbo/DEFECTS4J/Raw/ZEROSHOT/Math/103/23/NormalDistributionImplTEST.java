import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        distribution.setMean(15);
        assertEquals(15, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        distribution.setStandardDeviation(3);
        assertEquals(3, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(0.3085375387259869, distribution.cumulativeProbability(12), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(11.226557366613168, distribution.inverseCumulativeProbability(0.75), 0.000001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.3), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.7), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(10, 2);
        assertEquals(12, distribution.getInitialDomain(0.6), 0);
    }

}