package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class PoissonDistributionImplTest {
    
    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new PoissonDistributionImpl(-1));
    }

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.0001);
    }
    
    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.probability(-1), 0.0001);
        assertEquals(0.0, distribution.probability(Integer.MAX_VALUE), 0.0001);
        assertEquals(0.0820849986, distribution.probability(0), 0.0001);
        assertEquals(0.2052124964, distribution.probability(1), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.00820453432999554, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.23254415793455825, distribution.cumulativeProbability(1), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.48394144903828656, distribution.normalApproximateProbability(0), 0.0001);
        assertEquals(0.7210145119129984, distribution.normalApproximateProbability(1), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 100; i++) {
            int sample = distribution.sample();
            assert(sample >= 0 && sample <= Integer.MAX_VALUE);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0, distribution.getDomainLowerBound(0.1));
        assertEquals(0, distribution.getDomainLowerBound(0.5));
        assertEquals(0, distribution.getDomainLowerBound(0.9));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.1));
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5));
        assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.9));
    }
}