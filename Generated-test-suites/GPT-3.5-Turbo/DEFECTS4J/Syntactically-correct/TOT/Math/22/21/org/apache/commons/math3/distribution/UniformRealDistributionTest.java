import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class UniformRealDistributionTest {

    private UniformRealDistribution distribution;

    @Before
    public void setUp() {
        distribution = new UniformRealDistribution();
    }

    @Test
    public void testDensityWithinBounds() {
        double x = 0.5; // within the bounds of the distribution
        double expectedDensity = 1 / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound());
        double actualDensity = distribution.density(x);

        Assert.assertEquals(expectedDensity, actualDensity, 1e-9);
    }

    @Test
    public void testDensityOutsideBounds() {
        double x = 1.5; // outside the upper bound of the distribution
        double expectedDensity = 0.0;
        double actualDensity = distribution.density(x);

        Assert.assertEquals(expectedDensity, actualDensity, 1e-9);
    }

    @Test
    public void testCumulativeProbabilityWithinBounds() {
        double x = 0.5; // within the bounds of the distribution
        double expectedCumulativeProbability = (x - distribution.getSupportLowerBound()) / (distribution.getSupportUpperBound() - distribution.getSupportLowerBound());
        double actualCumulativeProbability = distribution.cumulativeProbability(x);

        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-9);
    }

    @Test
    public void testCumulativeProbabilityLowerBound() {
        double x = 0; // equal to the lower bound of the distribution
        double expectedCumulativeProbability = 0.0;
        double actualCumulativeProbability = distribution.cumulativeProbability(x);

        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-9);
    }

    @Test
    public void testCumulativeProbabilityUpperBound() {
        double x = 1; // equal to the upper bound of the distribution
        double expectedCumulativeProbability = 1.0;
        double actualCumulativeProbability = distribution.cumulativeProbability(x);

        Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        double expectedMean = 0.5 * (distribution.getSupportLowerBound() + distribution.getSupportUpperBound());
        double actualMean = distribution.getNumericalMean();

        Assert.assertEquals(expectedMean, actualMean, 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        double expectedVariance = Math.pow(distribution.getSupportUpperBound() - distribution.getSupportLowerBound(), 2) / 12;
        double actualVariance = distribution.getNumericalVariance();

        Assert.assertEquals(expectedVariance, actualVariance, 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        double expectedLowerBound = distribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY;
        double actualLowerBound = distribution.getSupportLowerBound();

        Assert.assertEquals(expectedLowerBound, actualLowerBound, 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        double expectedUpperBound = 1.0;
        double actualUpperBound = distribution.getSupportUpperBound();

        Assert.assertEquals(expectedUpperBound, actualUpperBound, 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        boolean isSampleWithinBounds = true;
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            if (sample < distribution.getSupportLowerBound() || sample > distribution.getSupportUpperBound()) {
                isSampleWithinBounds = false;
                break;
            }
        }

        Assert.assertTrue(isSampleWithinBounds);
    }
}