
//Corresponding JUnit 4 Test Cases:
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {
    private static UniformRealDistribution distribution;

    @BeforeClass
    public static void setUp() throws NumberIsTooLargeException {
        distribution = new UniformRealDistribution(0, 1);
    }

    @Test
    public void testDensity() {
        assertEquals(0.0, distribution.density(-1), 0);
        assertEquals(0.0, distribution.density(2), 0);
        assertEquals(1.0, distribution.density(0.5), 0);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0);
        assertEquals(1.0, distribution.cumulativeProbability(2), 0);
        assertEquals(0.5, distribution.cumulativeProbability(0.5), 0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(0.5, distribution.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(0.08333333333333333, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(1.0, distribution.getSupportUpperBound(), 0);
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
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution sampleDistribution = new UniformRealDistribution(rng, 0, 1);
        for (int i = 0; i < 100; i++) {
            double sample = sampleDistribution.sample();
            assertTrue(sample >= 0.0 && sample <= 1.0);
        }
    }
}
