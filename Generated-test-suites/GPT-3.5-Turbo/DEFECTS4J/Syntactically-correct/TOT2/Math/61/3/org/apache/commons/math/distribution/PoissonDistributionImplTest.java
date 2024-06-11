import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, distribution.getMean(), 0.01);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.22404180765538775, distribution.probability(2), 0.00001);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.06680044930503574, distribution.cumulativeProbability(1), 0.00001);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        assertEquals(0.9293606004606256, distribution.normalApproximateProbability(6), 0.00001);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample < Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMean() {
        new PoissonDistributionImpl(-3.0);
    }

    @Test
    public void testDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0, distribution.getDomainLowerBound(0.1));
    }

    @Test
    public void testDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.9));
    }
}