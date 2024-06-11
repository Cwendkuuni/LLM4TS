import static org.junit.Assert.*;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, poisson.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        assertEquals(0.006737946999085467, poisson.probability(0), 0.0001);
        assertEquals(0.03368973499542734, poisson.probability(1), 0.0001);
        assertEquals(0.08422433748856845, poisson.probability(2), 0.0001);
        assertEquals(0.14037389581428068, poisson.probability(3), 0.0001);
        assertEquals(0.17546736976716596, poisson.probability(4), 0.0001);
        assertEquals(0.17546736976716596, poisson.probability(5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        assertEquals(0.006737946999085467, poisson.cumulativeProbability(0), 0.0001);
        assertEquals(0.04042768199451263, poisson.cumulativeProbability(1), 0.0001);
        assertEquals(0.12465201948208109, poisson.cumulativeProbability(2), 0.0001);
        assertEquals(0.2650249156003618, poisson.cumulativeProbability(3), 0.0001);
        assertEquals(0.4404922853675278, poisson.cumulativeProbability(4), 0.0001);
        assertEquals(0.6159596551346936, poisson.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        assertEquals(0.6181082260675084, poisson.normalApproximateProbability(0), 0.0001);
        assertEquals(0.743993617667602, poisson.normalApproximateProbability(1), 0.0001);
        assertEquals(0.8287420216743281, poisson.normalApproximateProbability(2), 0.0001);
        assertEquals(0.8798403145183559, poisson.normalApproximateProbability(3), 0.0001);
        assertEquals(0.912451421206047, poisson.normalApproximateProbability(4), 0.0001);
        assertEquals(0.9327155735694794, poisson.normalApproximateProbability(5), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        int sample = poisson.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        assertEquals(0, poisson.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5.0);
        assertEquals(Integer.MAX_VALUE, poisson.getDomainUpperBound(0.5));
    }
}