package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefault() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // Test bounds
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);

        // Test density
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            double expectedDensity = 1 / (1.0 - 0.0);
            assertEquals(expectedDensity, density, 0.001);
        }

        // Test cumulative probability
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = x;
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }

        // Test sample
        RandomGenerator random = new Well19937c();
        UniformRealDistribution sampledDistribution = new UniformRealDistribution(random, 0.0, 1.0, 0.0);
        double sample = sampledDistribution.sample();
        assertTrue(sample >= 0.0 && sample < 1.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 0.0);
    }

    @Test
    public void testUniformRealDistributionCustomBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(3.0, 7.0);

        // Test bounds
        assertEquals(3.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(7.0, distribution.getSupportUpperBound(), 0.0);

        // Test density
        for (double x = 3.0; x <= 7.0; x += 0.01) {
            double density = distribution.density(x);
            double expectedDensity = 1 / (7.0 - 3.0);
            assertEquals(expectedDensity, density, 0.001);
        }

        // Test cumulative probability
        for (double x = 3.0; x <= 7.0; x += 0.01) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = (x - 3.0) / (7.0 - 3.0);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }

        // Test sample
        RandomGenerator random = new Well19937c();
        UniformRealDistribution sampledDistribution = new UniformRealDistribution(random, 3.0, 7.0, 0.0);
        double sample = sampledDistribution.sample();
        assertTrue(sample >= 3.0 && sample < 7.0);
    }

    @Test
    public void testUniformRealDistributionCustomBoundsAndAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(5.0, 10.0, 0.0001);

        // Test bounds
        assertEquals(5.0, distribution.getSupportLowerBound(), 0.0);
        assertEquals(10.0, distribution.getSupportUpperBound(), 0.0);

        // Test density
        for (double x = 5.0; x <= 10.0; x += 0.01) {
            double density = distribution.density(x);
            double expectedDensity = 1 / (10.0 - 5.0);
            assertEquals(expectedDensity, density, 0.0001);
        }

        // Test cumulative probability
        for (double x = 5.0; x <= 10.0; x += 0.01) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = (x - 5.0) / (10.0 - 5.0);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
        }

        // Test sample
        RandomGenerator random = new Well19937c();
        UniformRealDistribution sampledDistribution = new UniformRealDistribution(random, 5.0, 10.0, 0.0001);
        double sample = sampledDistribution.sample();
        assertTrue(sample >= 5.0 && sample < 10.0);
    }
}