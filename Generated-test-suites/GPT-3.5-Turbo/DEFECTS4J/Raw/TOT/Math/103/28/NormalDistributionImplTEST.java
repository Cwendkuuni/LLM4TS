import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(10);
        assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3);
        assertEquals(3, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(0.3085375, distribution.cumulativeProbability(7.2), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(7.2, distribution.inverseCumulativeProbability(0.3085375), 0.000001);
    }

    @Test
    public void testInverseCumulativeProbability_zero() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0);
    }

    @Test
    public void testInverseCumulativeProbability_one() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.4), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.6), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 2);
        assertEquals(7, distribution.getInitialDomain(0.6), 0);
    }

}