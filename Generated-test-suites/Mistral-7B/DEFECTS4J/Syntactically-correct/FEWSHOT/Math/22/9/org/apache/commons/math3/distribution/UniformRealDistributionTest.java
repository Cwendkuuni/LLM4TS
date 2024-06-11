package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistribution() {
        // Test default constructor
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0, dist.getSupportLowerBound(), 0.001);
        assertEquals(1, dist.getSupportUpperBound(), 0.001);
        assertEquals(0.5, dist.getNumericalMean(), 0.001);
        assertEquals(0.25, dist.getNumericalVariance(), 0.001);
        assertFalse(dist.isSupportLowerBoundInclusive());
        assertFalse(dist.isSupportUpperBoundInclusive());
        assertTrue(dist.isSupportConnected());

        // Test constructor with lower and upper bounds
        try {
            UniformRealDistribution dist1 = new UniformRealDistribution(1, 2);
            assertEquals(1, dist1.getSupportLowerBound(), 0.001);
            assertEquals(2, dist1.getSupportUpperBound(), 0.001);
            assertEquals(1.5, dist1.getNumericalMean(), 0.001);
            assertEquals(0.25, dist1.getNumericalVariance(), 0.001);
            assertFalse(dist1.isSupportLowerBoundInclusive());
            assertFalse(dist1.isSupportUpperBoundInclusive());
            assertTrue(dist1.isSupportConnected());

            // Test with invalid bounds
            try {
                new UniformRealDistribution(2, 1);
                fail("Expected NumberIsTooLargeException");
            } catch (NumberIsTooLargeException e) {
                // Expected
            }
        } catch (NumberIsTooLargeException e) {
            fail("Unexpected NumberIsTooLargeException");
        }

        // Test constructor with lower, upper and inverseCumulativeProbabilityAccuracy
        UniformRealDistribution dist2 = new UniformRealDistribution(0.1, 0.9, 1e-6);
        assertEquals(0.1, dist2.getSupportLowerBound(), 0.001);
        assertEquals(0.9, dist2.getSupportUpperBound(), 0.001);
        assertEquals(0.55, dist2.getNumericalMean(), 0.001);
        assertEquals(0.0013888888888888889, dist2.getNumericalVariance(), 0.001);
        assertFalse(dist2.isSupportLowerBoundInclusive());
        assertFalse(dist2.isSupportUpperBoundInclusive());
        assertTrue(dist2.isSupportConnected());

        // Test density
        double[] testValues = {0.1, 0.5, 0.9};
        for (double value : testValues) {
            double density = dist2.density(value);
            assertEquals(1 / (dist2.getSupportUpperBound() - dist2.getSupportLowerBound()), density, 0.001);
        }

        // Test cumulativeProbability
        double[] testCumProbValues = {0.05, 0.5, 0.95};
        for (double cumProbValue : testCumProbValues) {
            double cumProb = dist2.cumulativeProbability(cumProbValue);
            assertEquals(cumProbValue, cumProb * (dist2.getSupportUpperBound() - dist2.getSupportLowerBound()) + dist2.getSupportLowerBound(), 0.001);
        }

        // Test sample
        for (int i = 0; i < 1000; i++) {
            double sample = dist2.sample();
            assertTrue(sample >= dist2.getSupportLowerBound());
            assertTrue(sample < dist2.getSupportUpperBound());
        }
    }
}