import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(2.5);
    }

    @Test
    public void testGetMean() {
        assertEquals(2.5, distribution.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0668, distribution.probability(2), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5430, distribution.cumulativeProbability(2), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.89121, distribution.normalApproximateProbability(2), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}