import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.17546736976785068, distribution.probability(3), 0);
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.26502591529736186, distribution.cumulativeProbability(3), 0);
    }

    @Test
    public void testNormalApproximateProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0.6924028502898689, distribution.normalApproximateProbability(3), 0);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(0, distribution.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
    }
}