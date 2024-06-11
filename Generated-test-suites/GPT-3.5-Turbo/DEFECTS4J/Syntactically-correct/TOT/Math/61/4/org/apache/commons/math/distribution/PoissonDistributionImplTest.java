import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        assertEquals(0.224041807759, distribution.probability(2), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.0);
        assertEquals(0.628836504893, distribution.cumulativeProbability(3), 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(0.0, distribution.cumulativeProbability(-3), 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithMaxIntegerInput() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(6.0);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(7.0);
        assertEquals(0.78694293883, distribution.normalApproximateProbability(6), 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(8.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(9.0);
        assertEquals(0, distribution.getDomainLowerBound(0.5));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10.0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
    }
}