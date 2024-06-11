import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setMean(10.0);
        assertEquals(10.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(1.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.setStandardDeviation(2.0);
        assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.cumulativeProbability(1.0);
        assertEquals(0.8413, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        double result = distribution.getInitialDomain(0.5);
        assertEquals(0.0, result, 0.0001);
    }
}