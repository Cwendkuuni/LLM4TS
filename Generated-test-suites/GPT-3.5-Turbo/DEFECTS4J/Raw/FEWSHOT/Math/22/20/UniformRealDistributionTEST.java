package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {
    private static final double EPSILON = 1e-9;
    private static final double LOWER = 0;
    private static final double UPPER = 1;
    private static final double LOWER_NEG = -1;
    private static final double UPPER_NEG = 0;
    private static final double INVERSE_CUM_ACCURACY = 1e-12;
    private static final double INVERSE_CUM_ACCURACY_NEG = -1e-12;
    private static final RandomGenerator RANDOM_GENERATOR = new Well19937c();

    @Test
    public void testCreateStandardUniformRealDistribution() {
        UniformRealDistribution ud = new UniformRealDistribution();
        assertEquals(LOWER, ud.getSupportLowerBound(), EPSILON);
        assertEquals(UPPER, ud.getSupportUpperBound(), EPSILON);
        assertEquals(LOWER, ud.getNumericalMean(), EPSILON);
        assertEquals(Math.pow(UPPER - LOWER, 2) / 12, ud.getNumericalVariance(), EPSILON);
        assertTrue(ud.isSupportConnected());
    }

    @Test
    public void testCreateUniformRealDistribution() {
        UniformRealDistribution ud = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(LOWER, ud.getSupportLowerBound(), EPSILON);
        assertEquals(UPPER, ud.getSupportUpperBound(), EPSILON);
        assertEquals(0.5 * (LOWER + UPPER), ud.getNumericalMean(), EPSILON);
        assertEquals(Math.pow(UPPER - LOWER, 2) / 12, ud.getNumericalVariance(), EPSILON);
        assertTrue(ud.isSupportConnected());
    }

    @Test
    public void testCreateUniformRealDistributionWithInverseCumAccuracy() {
        UniformRealDistribution ud = new UniformRealDistribution(LOWER, UPPER, INVERSE_CUM_ACCURACY);
        assertEquals(LOWER, ud.getSupportLowerBound(), EPSILON);
        assertEquals(UPPER, ud.getSupportUpperBound(), EPSILON);
        assertEquals(0.5 * (LOWER + UPPER), ud.getNumericalMean(), EPSILON);
        assertEquals(Math.pow(UPPER - LOWER, 2) / 12, ud.getNumericalVariance(), EPSILON);
        assertTrue(ud.isSupportConnected());
    }

    @Test
    public void testCreateUniformRealDistributionWithNegativeInterval() {
        try {
            new UniformRealDistribution(LOWER_NEG, UPPER_NEG);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testCreateUniformRealDistributionWithNegativeInverseCumAccuracy() {
        try {
            new UniformRealDistribution(LOWER, UPPER, INVERSE_CUM_ACCURACY_NEG);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        UniformRealDistribution ud = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(1 / (UPPER - LOWER), ud.density(0.5), EPSILON);
        assertEquals(0, ud.density(LOWER - 1), EPSILON);
        assertEquals(0, ud.density(UPPER + 1), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution ud = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(0, ud.cumulativeProbability(LOWER - 1), EPSILON);
        assertEquals(1, ud.cumulativeProbability(UPPER + 1), EPSILON);
        assertEquals((LOWER - LOWER) / (UPPER - LOWER), ud.cumulativeProbability(LOWER), EPSILON);
        assertEquals((LOWER + UPPER - LOWER) / (UPPER - LOWER), ud.cumulativeProbability(LOWER / 2 + UPPER / 2), EPSILON);
    }

    @Test
    public void testGetters() {
        UniformRealDistribution ud = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(LOWER, ud.getSupportLowerBound(), EPSILON);
        assertEquals(UPPER, ud.getSupportUpperBound(), EPSILON);
        assertEquals(0.5 * (LOWER + UPPER), ud.getNumericalMean(), EPSILON);
        assertEquals(Math.pow(UPPER - LOWER, 2) / 12, ud.getNumericalVariance(), EPSILON);
        assertTrue(ud.isSupportConnected());
    }

    @Test
    public void testSampling() {
        UniformRealDistribution ud = new UniformRealDistribution(LOWER, UPPER);
        for (int i = 0; i < 100; i++) {
            double sample = ud.sample();
            assertTrue(sample >= LOWER && sample < UPPER);
        }
    }

    @Test
    public void testSupportMethods() {
        UniformRealDistribution ud = new UniformRealDistribution(LOWER, UPPER);
        assertTrue(ud.isSupportLowerBoundInclusive());
        assertFalse(ud.isSupportUpperBoundInclusive());
    }
}