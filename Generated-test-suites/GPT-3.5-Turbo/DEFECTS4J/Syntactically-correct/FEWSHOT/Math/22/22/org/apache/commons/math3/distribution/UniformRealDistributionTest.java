
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution(0, 10);
    }

    @Test
    public void testDensity() {
        assertEquals(0.1, distribution.density(5), 0);
        assertEquals(0.0, distribution.density(-5), 0);
        assertEquals(0.0, distribution.density(15), 0);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.5, distribution.cumulativeProbability(5), 0);
        assertEquals(0.0, distribution.cumulativeProbability(-5), 0);
        assertEquals(1.0, distribution.cumulativeProbability(15), 0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(5, distribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(8.333, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, distribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(10, distribution.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertTrue(!distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        RandomDataGenerator randomData = new RandomDataGenerator();
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0 && sample < 10);
        }
    }
}
