import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(0.0, distribution.getLowerBound(), 0.0);
        Assert.assertEquals(1.0, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithLowerAndUpperBounds() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
        Assert.assertEquals(0.1, distribution.getLowerBound(), 0.0);
        Assert.assertEquals(0.3, distribution.getUpperBound(), 0.0);
    }

    @Test
    public void testConstructorWithInvalidLowerAndUpperBounds() {
        try {
            new UniformRealDistribution(0.5, 0.5);
            Assert.fail();
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
        for (double x = 0.1; x <= 0.3; x += 0.01) {
            double density = distribution.density(x);
            Assert.assertEquals(1.0 / (0.3 - 0.1), density, 1e-6);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
        for (double x = 0.1, p = 0.0; x <= 0.3; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            Assert.assertEquals(p, cumProb, 1e-6);
            p += 1.0 / (0.3 - 0.1);
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(new Well19937c(), 0.1, 0.3);
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            Assert.assertTrue(sample >= 0.1 && sample < 0.3);
        }
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
        double mean = distribution.getNumericalMean();
        Assert.assertEquals(0.2, mean, 1e-6);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
        double variance = distribution.getNumericalVariance();
        Assert.assertEquals(0.0004166666666666667, variance, 1e-10);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);

        distribution = new UniformRealDistribution(0.1, 0.3);
        Assert.assertEquals(0.1, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);

        distribution = new UniformRealDistribution(0.1, 0.3);
        Assert.assertEquals(0.3, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());

        distribution = new UniformRealDistribution(0.1, 0.3);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());

        distribution = new UniformRealDistribution(0.1, 0.3);
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        Assert.assertTrue(distribution.isSupportConnected());

        distribution = new UniformRealDistribution(0.1, 0.3);
        Assert.assertTrue(distribution.isSupportConnected());
    }
}