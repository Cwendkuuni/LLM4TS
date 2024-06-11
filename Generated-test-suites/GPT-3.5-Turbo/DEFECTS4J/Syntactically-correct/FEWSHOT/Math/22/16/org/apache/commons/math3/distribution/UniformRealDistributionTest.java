import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class UniformRealDistributionTest {

    private static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;
    private static final double LOWER = 1.0;
    private static final double UPPER = 10.0;
    private static final double INVERSE_CUM_ACCURACY = 0.1;
    private static final double MEAN = 5.5;
    private static final double VARIANCE = 8.25;

    @Test
    public void testDensityInsideBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        double x = LOWER + (UPPER - LOWER) / 2;
        double expectedDensity = 1 / (UPPER - LOWER);
        assertEquals(expectedDensity, dist.density(x), 0.0);
    }

    @Test
    public void testDensityOutsideBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        double x = UPPER + (UPPER - LOWER) / 2;
        assertEquals(0.0, dist.density(x), 0.0);
    }

    @Test
    public void testCumulativeProbabilityInsideBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        double x = LOWER + (UPPER - LOWER) / 2;
        double expectedCumProb = (x - LOWER) / (UPPER - LOWER);
        assertEquals(expectedCumProb, dist.cumulativeProbability(x), 0.0);
    }

    @Test
    public void testCumulativeProbabilityOutsideBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        double x = UPPER + (UPPER - LOWER) / 2;
        assertEquals(1.0, dist.cumulativeProbability(x), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(MEAN, dist.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(VARIANCE, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(LOWER, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(UPPER, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        assertTrue(!dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        double sample = dist.sample();
        assertTrue(sample >= LOWER && sample < UPPER);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        new UniformRealDistribution(UPPER, LOWER);
    }

    @Test
    public void testInverseCumAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER, INVERSE_CUM_ACCURACY);
        assertEquals(INVERSE_CUM_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testDefaultInverseCumAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution(LOWER, UPPER);
        assertEquals(DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, LOWER, UPPER, INVERSE_CUM_ACCURACY);
        assertEquals(rng, dist.getRandomGenerator());
    }

    @Test
    public void testSamplingIsEmpty() {
        UniformRealDistribution dist = new UniformRealDistribution(UPPER, UPPER);
        assertTrue(dist.sample() == Double.POSITIVE_INFINITY);
    }
}