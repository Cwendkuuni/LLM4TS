import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        assertEquals(2, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        assertEquals(3, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        assertEquals(0.13304864722919943, distribution.density(1), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        assertEquals(0.4012936743170763, distribution.cumulativeProbability(1), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        assertEquals(-1.1503493803760086, distribution.inverseCumulativeProbability(0.2), 0.001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        double sample = distribution.sample();
        assertTrue(sample >= -Double.MAX_VALUE && sample <= Double.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.1), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.9), 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2, 3);
        assertEquals(5, distribution.getInitialDomain(0.7), 0);
    }
}