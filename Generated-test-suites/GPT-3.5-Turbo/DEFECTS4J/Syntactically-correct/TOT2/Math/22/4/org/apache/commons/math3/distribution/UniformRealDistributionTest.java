import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    // Test variables
    private final double LOWER = 0.0;
    private final double UPPER = 1.0;
    private final double EPSILON = 1e-9;

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertEquals(0.5, distribution.density(0.5), EPSILON);
        Assert.assertEquals(0.0, distribution.density(-1.0), EPSILON);
        Assert.assertEquals(0.0, distribution.density(2.0), EPSILON);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0.5), EPSILON);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1.0), EPSILON);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(2.0), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertEquals(0.5, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertEquals(1.0/12.0, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertEquals(LOWER, distribution.getSupportLowerBound(), EPSILON);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertEquals(UPPER, distribution.getSupportUpperBound(), EPSILON);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(LOWER, UPPER);
        Assert.assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution distribution = new UniformRealDistribution(rng, LOWER, UPPER, EPSILON);
        double sample = distribution.sample();

        Assert.assertTrue(sample >= LOWER && sample < UPPER);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        new UniformRealDistribution(UPPER, LOWER);
    }

}