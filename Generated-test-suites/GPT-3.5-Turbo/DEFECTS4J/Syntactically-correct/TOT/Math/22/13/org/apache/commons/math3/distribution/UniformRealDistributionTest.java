import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        double result = distribution.density(0.5);
        Assert.assertEquals(1, result, 0);

        // edge case
        result = distribution.density(1);
        Assert.assertEquals(0, result, 0);

        // error scenario
        result = distribution.density(-1);
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        double result = distribution.cumulativeProbability(0.5);
        Assert.assertEquals(0.5, result, 0);

        // edge case
        result = distribution.cumulativeProbability(1);
        Assert.assertEquals(1, result, 0);

        // error scenario
        result = distribution.cumulativeProbability(-1);
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        double result = distribution.getNumericalMean();
        Assert.assertEquals(0.5, result, 0);

        // edge case
        distribution = new UniformRealDistribution(1, 2);
        result = distribution.getNumericalMean();
        Assert.assertEquals(1.5, result, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        double result = distribution.getNumericalVariance();
        Assert.assertEquals(1/12.0, result, 0);

        // edge case
        distribution = new UniformRealDistribution(1, 2);
        result = distribution.getNumericalVariance();
        Assert.assertEquals(0.25, result, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        double result = distribution.getSupportLowerBound();
        Assert.assertEquals(0, result, 0);

        // edge case
        distribution = new UniformRealDistribution(1, 2);
        result = distribution.getSupportLowerBound();
        Assert.assertEquals(1, result, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        double result = distribution.getSupportUpperBound();
        Assert.assertEquals(1, result, 0);

        // edge case
        distribution = new UniformRealDistribution(1, 2);
        result = distribution.getSupportUpperBound();
        Assert.assertEquals(2, result, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        boolean result = distribution.isSupportLowerBoundInclusive();
        Assert.assertEquals(true, result);

        // edge case
        distribution = new UniformRealDistribution(1, 2);
        result = distribution.isSupportLowerBoundInclusive();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        boolean result = distribution.isSupportUpperBoundInclusive();
        Assert.assertEquals(false, result);

        // edge case
        distribution = new UniformRealDistribution(1, 2);
        result = distribution.isSupportUpperBoundInclusive();
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        boolean result = distribution.isSupportConnected();
        Assert.assertEquals(true, result);

        // edge case
        distribution = new UniformRealDistribution(1, 2);
        result = distribution.isSupportConnected();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        // typical use case
        double result = distribution.sample();
        Assert.assertTrue(result >= 0 && result < 1);

        // edge case
        distribution = new UniformRealDistribution(1, 2);
        result = distribution.sample();
        Assert.assertTrue(result >= 1 && result < 2);
    }
}