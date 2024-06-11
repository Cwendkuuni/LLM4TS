import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl poissonDistribution;

    @Before
    public void setUp() {
        poissonDistribution = new PoissonDistributionImpl(5.0);
    }

    @Test
    public void testGetMean() {
        assertEquals(5.0, poissonDistribution.getMean(), 0.0001);
    }

    @Test
    public void testProbabilityWithPositiveX() {
        assertEquals(0.03368, poissonDistribution.probability(3), 0.0001);
    }

    @Test
    public void testProbabilityWithZeroX() {
        assertEquals(0.0067379, poissonDistribution.probability(0), 0.0001);
    }

    @Test
    public void testProbabilityWithNegativeX() {
        assertEquals(0.0, poissonDistribution.probability(-5), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithPositiveX() {
        assertEquals(0.18474, poissonDistribution.cumulativeProbability(3), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithZeroX() {
        assertEquals(0.04043, poissonDistribution.cumulativeProbability(0), 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeX() {
        assertEquals(0.0, poissonDistribution.cumulativeProbability(-5), 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityWithPositiveX() {
        assertEquals(0.17289, poissonDistribution.normalApproximateProbability(3), 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityWithZeroX() {
        assertEquals(0.080745, poissonDistribution.normalApproximateProbability(0), 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityWithNegativeX() {
        assertEquals(0.0, poissonDistribution.normalApproximateProbability(-5), 0.0001);
    }

    @Test
    public void testSample() {
        int sample = poissonDistribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, poissonDistribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(0.5));
    }

}