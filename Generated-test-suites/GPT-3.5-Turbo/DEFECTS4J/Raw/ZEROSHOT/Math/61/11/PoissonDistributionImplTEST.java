import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(3.0);
    }

    @Test
    public void testGetMean() {
        assertEquals(3.0, distribution.getMean(), 0.00001);
    }

    @Test
    public void testProbability() {
        assertEquals(0.049787, distribution.probability(0), 0.000001);
        assertEquals(0.149361, distribution.probability(1), 0.000001);
        assertEquals(0.224041, distribution.probability(2), 0.000001);
        assertEquals(0.224041, distribution.probability(3), 0.000001);
        assertEquals(0.168031, distribution.probability(4), 0.000001);
        assertEquals(0.100818, distribution.probability(5), 0.000001);
        assertEquals(0.050409, distribution.probability(6), 0.000001);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.049787, distribution.cumulativeProbability(0), 0.000001);
        assertEquals(0.199149, distribution.cumulativeProbability(1), 0.000001);
        assertEquals(0.42319, distribution.cumulativeProbability(2), 0.000001);
        assertEquals(0.647232, distribution.cumulativeProbability(3), 0.000001);
        assertEquals(0.815263, distribution.cumulativeProbability(4), 0.000001);
        assertEquals(0.91608, distribution.cumulativeProbability(5), 0.000001);
        assertEquals(0.966489, distribution.cumulativeProbability(6), 0.000001);
    }

    @Test
    public void testNormalApproximateProbability() {
        assertEquals(0.080301, distribution.normalApproximateProbability(0), 0.000001);
        assertEquals(0.252492, distribution.normalApproximateProbability(1), 0.000001);
        assertEquals(0.602206, distribution.normalApproximateProbability(2), 0.000001);
        assertEquals(0.849825, distribution.normalApproximateProbability(3), 0.000001);
        assertEquals(0.967421, distribution.normalApproximateProbability(4), 0.000001);
        assertEquals(0.993974, distribution.normalApproximateProbability(5), 0.000001);
        assertEquals(0.999739, distribution.normalApproximateProbability(6), 0.000001);
    }

    @Test
    public void testSample() {
        for (int i = 0; i < 1000; i++) {
            int sample = distribution.sample();
            assertTrue(sample >= 0);
            assertTrue(sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, distribution.getDomainLowerBound(0.0), 0.00001);
        assertEquals(0, distribution.getDomainLowerBound(0.5), 0.00001);
        assertEquals(0, distribution.getDomainLowerBound(1.0), 0.00001);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0), 0.00001);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0.00001);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1.0), 0.00001);
    }
}