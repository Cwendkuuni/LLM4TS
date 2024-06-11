import org.junit.Test;
import org.apache.commons.math.MathException;

import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5);
        assertEquals(5, distribution.getMean(), 0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(0.049787068367863944, distribution.probability(0), 0);
        assertEquals(0.011109003495123981, distribution.probability(5), 0);
        assertEquals(0, distribution.probability(-1), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2);
        assertEquals(0.1353352832366127, distribution.cumulativeProbability(0), 0);
        assertEquals(0.857123460498547, distribution.cumulativeProbability(5), 0);
        assertEquals(0, distribution.cumulativeProbability(-1), 0);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4);
        assertEquals(0.5665281477242335, distribution.normalApproximateProbability(0), 0);
        assertEquals(0.8028147307240348, distribution.normalApproximateProbability(5), 0);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(6);
        int sample = distribution.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-5);
    }

    @Test
    public void testProbabilityNegativeValue() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2);
        assertEquals(0, distribution.probability(-1), 0);
        assertEquals(0, distribution.probability(Integer.MAX_VALUE), 0);
    }

    @Test
    public void testCumulativeProbabilityNegativeValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3);
        assertEquals(0, distribution.cumulativeProbability(-1), 0);
        assertEquals(1, distribution.cumulativeProbability(Integer.MAX_VALUE), 0);
    }

    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityNegativeValue() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4);
        distribution.normalApproximateProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testSampleError() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(6) {
            @Override
            public int sample() throws MathException {
                throw new MathException("Error generating random value");
            }
        };
        distribution.sample();
    }
}