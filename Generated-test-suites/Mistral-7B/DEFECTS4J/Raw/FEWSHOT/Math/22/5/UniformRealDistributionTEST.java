package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.Random;

public class UniformRealDistributionTest {
    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution urd = new UniformRealDistribution();
        assertEquals(0.0, urd.getLowerBound(), 1e-14);
        assertEquals(1.0, urd.getUpperBound(), 1e-14);

        UniformRealDistribution urd1 = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.0, urd1.getLowerBound(), 1e-14);
        assertEquals(1.0, urd1.getUpperBound(), 1e-14);

        UniformRealDistribution urd2 = new UniformRealDistribution(0.0, 1.0, 1e-14);
        assertEquals(0.0, urd2.getLowerBound(), 1e-14);
        assertEquals(1.0, urd2.getUpperBound(), 1e-14);

        UniformRealDistribution urd3 = new UniformRealDistribution(new Random(), 0.0, 1.0);
        assertEquals(0.0, urd3.getLowerBound(), 1e-14);
        assertEquals(1.0, urd3.getUpperBound(), 1e-14);

        UniformRealDistribution urd4 = new UniformRealDistribution(new Random(), 2.0, 3.0);
        assertEquals(2.0, urd4.getLowerBound(), 1e-14);
        assertEquals(3.0, urd4.getUpperBound(), 1e-14);

        try {
            new UniformRealDistribution(new Random(), 1.0, 1.0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testUniformRealDistributionProperties() {
        UniformRealDistribution urd = new UniformRealDistribution(new Random(), 0.0, 1.0);

        assertEquals(1.0 / (1.0 - 0.0), urd.density(0.5), 1e-9);
        assertEquals(0.0, urd.density(1.0), 1e-14);
        assertEquals(1.0, urd.density(0.0), 1e-14);

        assertEquals(0.5, urd.cumulativeProbability(0.5), 1e-9);
        assertEquals(0.0, urd.cumulativeProbability(0.0), 1e-14);
        assertEquals(1.0, urd.cumulativeProbability(1.0), 1e-14);

        assertEquals(0.5, urd.getNumericalMean(), 1e-14);
        assertEquals(0.25, urd.getNumericalVariance(), 1e-14);

        assertEquals(0.0, urd.getSupportLowerBound(), 0.0);
        assertEquals(1.0, urd.getSupportUpperBound(), 1e-14);
        assertTrue(urd.isSupportLowerBoundInclusive());
        assertFalse(urd.isSupportUpperBoundInclusive());
        assertTrue(urd.isSupportConnected());

        UniformRealDistribution urdSample = new UniformRealDistribution(new Random(), 0.0, 1.0);
        for (int i = 0; i < 10000; i++) {
            double sample = urdSample.sample();
            assertTrue(sample >= 0.0);
            assertTrue(sample <= 1.0);
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution urd = new UniformRealDistribution(new Random(), 0.0, 1.0);
        Random urdRng = urd.getRNG();

        for (int i = 0; i < 10000; i++) {
            double sample = urd.sample();
            double sampleRandom = urdRng.nextDouble();
            assertEquals(sample, sampleRandom, 1e-9);
        }
    }
}