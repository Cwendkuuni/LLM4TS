
package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {
    private PoissonDistributionImpl distribution;

    @Before
    public void setup() {
        distribution = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
    }

    @Test
    public void testGetMean() {
        assertEquals(2.5, distribution.getMean(), 0);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0820849986238988, distribution.probability(1), 1E-10);
        assertEquals(0.205212496559747, distribution.probability(2), 1E-10);
        assertEquals(0.0707372016677029, distribution.probability(3), 1E-10);
        assertEquals(0.0848846420012435, distribution.probability(4), 1E-10);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0197539863908625, distribution.cumulativeProbability(1), 1E-10);
        assertEquals(0.0928684069699312, distribution.cumulativeProbability(2), 1E-10);
        assertEquals(0.163290608637271, distribution.cumulativeProbability(3), 1E-10);
        assertEquals(0.2481752506384, distribution.cumulativeProbability(4), 1E-10);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.0912112197258673, distribution.normalApproximateProbability(1), 1E-10);
        assertEquals(0.157869165555133, distribution.normalApproximateProbability(2), 1E-10);
        assertEquals(0.183939720585721, distribution.normalApproximateProbability(3), 1E-10);
        assertEquals(0.153538091384478, distribution.normalApproximateProbability(4), 1E-10);
    }

    @Test
    public void testSample() throws MathException {
        int sample = distribution.sample();
        assertTrue(sample >= 0);
        assertTrue(sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(0, distribution.getDomainLowerBound(0.0), 0);
        assertEquals(0, distribution.getDomainLowerBound(0.5), 0);
        assertEquals(0, distribution.getDomainLowerBound(1.0), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.0), 0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(1.0), 0);
    }
}
