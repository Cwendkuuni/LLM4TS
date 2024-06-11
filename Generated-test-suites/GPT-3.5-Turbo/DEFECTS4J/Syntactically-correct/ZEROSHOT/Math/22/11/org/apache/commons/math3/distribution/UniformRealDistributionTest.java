import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UniformRealDistributionTest {
    private UniformRealDistribution dist;

    @Before
    public void setUp() {
        dist = new UniformRealDistribution();
    }

    @Test
    public void testDensity() {
        Assert.assertEquals(0.0, dist.density(-1), 0);
        Assert.assertEquals(1.0, dist.density(0.5), 0);
        Assert.assertEquals(0.0, dist.density(2), 0);
    }

    @Test
    public void testCumulativeProbability() {
        Assert.assertEquals(0.0, dist.cumulativeProbability(-1), 0);
        Assert.assertEquals(0.5, dist.cumulativeProbability(0.5), 0);
        Assert.assertEquals(1.0, dist.cumulativeProbability(2), 0);
    }

    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(0.5, dist.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(0.08333333333333333, dist.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals(0.0, dist.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals(1.0, dist.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        Assert.assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        Assert.assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testSample() {
        double sample = dist.sample();
        Assert.assertTrue(sample >= 0.0 && sample < 1.0);
    }
}