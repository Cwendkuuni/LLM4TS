import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {

    private RandomGenerator rg;
    private UniformRealDistribution dist;

    @Before
    public void setup() {
        rg = new Well19937c();
        dist = new UniformRealDistribution(rg, 0, 1);
    }

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution();
        Assert.assertEquals(0.0, urd.getSupportLowerBound(), 0.0);
        Assert.assertEquals(1.0, urd.getSupportUpperBound(), 0.0);
        Assert.assertTrue(urd.isSupportLowerBoundInclusive());
        Assert.assertFalse(urd.isSupportUpperBoundInclusive());
        Assert.assertTrue(urd.isSupportConnected());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithInvalidArguments() {
        new UniformRealDistribution(rg, 1.0, 0.0);
    }

    @Test
    public void testCustomConstructor() {
        UniformRealDistribution urd = new UniformRealDistribution(rg, 2.0, 4.0);
        Assert.assertEquals(2.0, urd.getSupportLowerBound(), 0.0);
        Assert.assertEquals(4.0, urd.getSupportUpperBound(), 0.0);
        Assert.assertTrue(urd.isSupportLowerBoundInclusive());
        Assert.assertFalse(urd.isSupportUpperBoundInclusive());
        Assert.assertTrue(urd.isSupportConnected());
    }

    @Test
    public void testDensity() {
        double x = 0.5;
        double expected = 1.0 / (1.0 - 0.0);
        Assert.assertEquals(expected, dist.density(x), 0.0);
    }

    @Test
    public void testCumulativeProbability() {
        double x = 0.25;
        double expected = 0.25;
        Assert.assertEquals(expected, dist.cumulativeProbability(x), 0.0);
    }

    @Test
    public void testGetNumericalMean() {
        double expected = 0.5;
        Assert.assertEquals(expected, dist.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        double expected = 0.25;
        Assert.assertEquals(expected, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testSample() {
        double expectedMin = 0.0;
        double expectedMax = 1.0;
        for (int i = 0; i < 100; i++) {
            double value = dist.sample();
            Assert.assertFalse(value < expectedMin);
            Assert.assertFalse(value > expectedMax);
        }
    }
}