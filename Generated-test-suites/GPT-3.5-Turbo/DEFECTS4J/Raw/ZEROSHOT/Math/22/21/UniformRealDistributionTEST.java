import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class UniformRealDistributionTest {
    private UniformRealDistribution distribution;

    @Before
    public void setup() {
        distribution = new UniformRealDistribution();
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidBounds() {
        distribution = new UniformRealDistribution(1, 1);
    }

    @Test
    public void testConstructorWithDefaultInverseCumAccuracy() {
        Assert.assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testConstructorWithBounds() {
        distribution = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(1, distribution.getSupportUpperBound(), 0);
        Assert.assertEquals(true, distribution.isSupportLowerBoundInclusive());
        Assert.assertEquals(false, distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testConstructorWithInverseCumAccuracy() {
        distribution = new UniformRealDistribution(0, 1, 0.01);
        Assert.assertEquals(0, distribution.getSupportLowerBound(), 0);
        Assert.assertEquals(1, distribution.getSupportUpperBound(), 0);
        Assert.assertEquals(true, distribution.isSupportLowerBoundInclusive());
        Assert.assertEquals(false, distribution.isSupportUpperBoundInclusive());
        Assert.assertEquals(0.01, distribution.getSolverAbsoluteAccuracy(), 0);
    }

    @Test
    public void testDensityWithInBoundsValue() {
        double result = distribution.density(0.5);
        Assert.assertEquals(1, result, 0);
    }

    @Test
    public void testDensityWithOutOfBoundsValue() {
        double result = distribution.density(1.5);
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testCumulativeProbabilityWithInBoundsValue() {
        double result = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.5, result, 0);
    }

    @Test
    public void testCumulativeProbabilityWithLowerBoundValue() {
        double result = distribution.cumulativeProbability(0);
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testCumulativeProbabilityWithUpperBoundValue() {
        double result = distribution.cumulativeProbability(1);
        Assert.assertEquals(1, result, 0);
    }

    @Test
    public void testGetNumericalMean() {
        double result = distribution.getNumericalMean();
        Assert.assertEquals(0.5, result, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        double result = distribution.getNumericalVariance();
        Assert.assertEquals(0.08333333333333333, result, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        double result = distribution.getSupportLowerBound();
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double result = distribution.getSupportUpperBound();
        Assert.assertEquals(1, result, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean result = distribution.isSupportLowerBoundInclusive();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean result = distribution.isSupportUpperBoundInclusive();
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsSupportConnected() {
        boolean result = distribution.isSupportConnected();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testSample() {
        double result = distribution.sample();
        Assert.assertTrue(result >= 0 && result < 1);
    }
}