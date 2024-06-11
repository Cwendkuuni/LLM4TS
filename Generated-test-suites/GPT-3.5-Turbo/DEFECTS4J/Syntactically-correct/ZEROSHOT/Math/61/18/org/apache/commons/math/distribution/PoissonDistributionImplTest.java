import org.junit.Test;
import org.apache.commons.math.MathException;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        double mean = distribution.getMean();
        assertEquals(3.5, mean, 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        double prob = distribution.probability(2);
        assertEquals(0.21191483565, prob, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        double cumProb = distribution.cumulativeProbability(4);
        assertEquals(0.78513038747, cumProb, 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        double normProb = distribution.normalApproximateProbability(4);
        assertEquals(0.80286109072, normProb, 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        int sample = distribution.sample();
        assertNotNull(sample);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        int lowerBound = distribution.getDomainLowerBound(0.5);
        assertEquals(0, lowerBound);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.5);
        int upperBound = distribution.getDomainUpperBound(0.5);
        assertEquals(Integer.MAX_VALUE, upperBound);
    }
}