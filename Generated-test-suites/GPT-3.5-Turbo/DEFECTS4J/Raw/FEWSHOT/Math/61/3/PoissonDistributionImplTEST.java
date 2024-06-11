package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PoissonDistributionImplTest {

    private PoissonDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new PoissonDistributionImpl(2.5, 1E-12, 10000000);
    }

    @Test
    public void testGetMean() {
        assertEquals(2.5, distribution.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        assertEquals(0.0820849986238988, distribution.probability(0), 1E-12);
        assertEquals(0.205212496559747, distribution.probability(1), 1E-12);
        assertEquals(0.256515620699684, distribution.probability(2), 1E-12);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.0820849986238988, distribution.cumulativeProbability(0), 1E-12);
        assertEquals(0.287297495183645, distribution.cumulativeProbability(1), 1E-12);
        assertEquals(0.54381311588333, distribution.cumulativeProbability(2), 1E-12);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        assertEquals(0.79112288351642, distribution.normalApproximateProbability(0), 1E-12);
        assertEquals(0.7066769769724832, distribution.normalApproximateProbability(1), 1E-12);
        assertEquals(0.5824776348613365, distribution.normalApproximateProbability(2), 1E-12);
    }

    @Test
    public void testSample() throws MathException {
        int sample = distribution.sample();
        assertEquals(true, sample >= 0 && sample <= Integer.MAX_VALUE);
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