import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poisson.getMean(), 0.001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0.055, poisson.probability(2), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0.543, poisson.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0.622, poisson.normalApproximateProbability(2), 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        int sample = poisson.sample();
        assertTrue(sample >= 0);
    }

    @Test
    public void testDomainLowerBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0, poisson.getDomainLowerBound(0.2), 0.001);
    }

    @Test
    public void testDomainUpperBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.8), 0.001);
    }
}