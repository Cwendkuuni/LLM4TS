package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link UniformRealDistribution} class.
 */
public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
        // test density, cumulativeProbability, and sample methods
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            double cumProb = distribution.cumulativeProbability(x);
            double sample = distribution.sample();
            assertEquals(density, 1.0 / (1.0 - 0.0), 1e-6); // check density accuracy
            assertEquals(cumProb, x, 1e-6); // check cumulativeProbability accuracy
            assertEquals(sample, x * (1.0 - 0.0) + 0.0 * (1.0 - x), 1e-6); // check sample accuracy
        }
    }

    @Test
    public void testCustomConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.4, 0.001);
        assertEquals(0.1, distribution.getSupportLowerBound(), 0.0);
        assertEquals(0.4, distribution.getSupportUpperBound(), 0.0);
        for (double x = 0.1; x <= 0.4; x += 0.01) {
            double density = distribution.density(x);
            double cumProb = distribution.cumulativeProbability(x);
            double sample = distribution.sample();
            assertEquals(density, 1.0 / (0.4 - 0.1), 1e-6); // check density accuracy
            assertEquals(cumProb, (x - 0.1) / (0.4 - 0.1), 1e-6); // check cumulativeProbability accuracy
            assertEquals(sample, x * (0.4 - 0.1) + 0.1 * (1.0 - x), 1e-6); // check sample accuracy
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidLowerUpperBounds() {
        UniformRealDistribution distribution = null;
        try {
            distribution = new UniformRealDistribution(2.0, 1.0);
        } catch (NumberIsTooLargeException e) {
            // expected
        }
        assertNull(distribution);
    }
}