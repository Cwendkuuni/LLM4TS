import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        assertEquals(0.3989423, distribution.density(5), 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        assertEquals(0.5, distribution.cumulativeProbability(5), 0);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        assertEquals(5.0, distribution.inverseCumulativeProbability(0.5), 0.00001);
    }

    @Test
    public void testSample() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        double sample = distribution.sample();
        assertTrue(sample >= -Double.MAX_VALUE && sample <= Double.MAX_VALUE);
    }

    @Test
    public void testUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        double upperBound = distribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, upperBound, 0);
    }

    @Test
    public void testLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        double lowerBound = distribution.getDomainLowerBound(0.5);
        assertEquals(-Double.MAX_VALUE, lowerBound, 0);
    }

    @Test
    public void testInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        double initialDomain = distribution.getInitialDomain(0.5);
        assertEquals(6, initialDomain, 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, -1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityException() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(5, 1);
        distribution.cumulativeProbability(10);
    }
}