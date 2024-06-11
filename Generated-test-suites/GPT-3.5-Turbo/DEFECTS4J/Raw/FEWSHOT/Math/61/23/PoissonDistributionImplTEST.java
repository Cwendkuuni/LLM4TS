import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoissonDistributionImplTest {
    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(0.006737946999085467, distribution.probability(0), 0.0001);
        assertEquals(0.03368973499542734, distribution.probability(1), 0.0001);
        assertEquals(0.0842243374885677, distribution.probability(2), 0.0001);
        assertEquals(0.140373561314279, distribution.probability(3), 0.0001);
        assertEquals(0.17546695164339937, distribution.probability(4), 0.0001);
        assertEquals(0.17546695164339937, distribution.probability(4), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(0.006737946999085476, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.040427681874070184, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.1246520194830815, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.26502591529736167, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.4404932850570, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(0.6159606548169185, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        assertEquals(0.9826324989327846, distribution.normalApproximateProbability(4), 0.0001);
        assertEquals(0.972453084777717, distribution.normalApproximateProbability(5), 0.0001);
        assertEquals(0.45687603571368023, distribution.normalApproximateProbability(9), 0.0001);
        assertEquals(0.03326586096403274, distribution.normalApproximateProbability(18), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.0);
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }
}