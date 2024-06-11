import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Assert.assertEquals(0.0, dist.getLowerBound(), 0.0);
        Assert.assertEquals(1.0, dist.getUpperBound(), 0.0);
        Assert.assertEquals(0.5, dist.getNumericalMean(), 0.0);
        Assert.assertEquals(0.25, dist.getNumericalVariance(), 0.0);
        RandomGenerator rng = dist.getRandomGenerator();
        Assert.assertNotNull(rng);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidLowerUpperBounds() {
        new UniformRealDistribution(3.0, 2.0);
    }

    @Test
    public void testCustomConstructor() {
        double lower = 1.0;
        double upper = 2.0;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper);
        Assert.assertEquals(lower, dist.getLowerBound(), 0.0);
        Assert.assertEquals(upper, dist.getUpperBound(), 0.0);
        Assert.assertEquals(1.5, dist.getNumericalMean(), 0.0);
        Assert.assertEquals(0.0625, dist.getNumericalVariance(), 0.0);
        RandomGenerator rng = dist.getRandomGenerator();
        Assert.assertNotNull(rng);
    }

    @Test
    public void testCustomConstructorWithInverseCumulativeProbabilityAccuracy() {
        double lower = 1.0;
        double upper = 2.0;
        double inverseCumulativeProbabilityAccuracy = 0.00001;
        UniformRealDistribution dist = new UniformRealDistribution(lower, upper, inverseCumulativeProbabilityAccuracy);
        Assert.assertEquals(lower, dist.getLowerBound(), 0.0);
        Assert.assertEquals(upper, dist.getUpperBound(), 0.0);
        Assert.assertEquals(1.5, dist.getNumericalMean(), 0.0);
        Assert.assertEquals(0.0625, dist.getNumericalVariance(), 0.0);
        RandomGenerator rng = dist.getRandomGenerator();
        Assert.assertNotNull(rng);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double x = 0.5;
        double density = dist.density(x);
        Assert.assertEquals(2.0 / (dist.getUpperBound() - dist.getLowerBound()), density, 0.001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double x = 0.5;
        double cumulativeProbability = dist.cumulativeProbability(x);
        Assert.assertEquals(0.5, cumulativeProbability, 0.001);
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double sample = dist.sample();
        Assert.assertTrue(sample >= dist.getLowerBound());
        Assert.assertTrue(sample < dist.getUpperBound());
    }

    @Test
    public void testSampleMultipleTimes() {
        UniformRealDistribution dist = new UniformRealDistribution();
        int numSamples = 10000;
        double[] samples = new double[numSamples];
        RandomGenerator rng = dist.getRandomGenerator();
        for (int i = 0; i < numSamples; i++) {
            samples[i] = dist.sample();
        }
        for (int i = 0; i < numSamples; i++) {
            Assert.assertTrue(samples[i] >= dist.getLowerBound());
            Assert.assertTrue(samples[i] < dist.getUpperBound());
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        UniformRealDistribution dist = new UniformRealDistribution();
        Assert.assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY,
                dist.getSolverAbsoluteAccuracy(), 0.0);

        UniformRealDistribution distWithCustomAccuracy = new UniformRealDistribution(new Well19937c(),
                0.0, 1.0, 0.001);
        Assert.assertEquals(0.001, distWithCustomAccuracy.getSolverAbsoluteAccuracy(), 0.0);
    }
}