import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution urd = new UniformRealDistribution();
        Assert.assertEquals(0.0, urd.getLowerBound(), 0.0);
        Assert.assertEquals(1.0, urd.getUpperBound(), 0.0);
    }

    @Test
    public void testUniformRealDistributionWithLowerAndUpperBounds() {
        UniformRealDistribution urd = new UniformRealDistribution(1.5, 3.2);
        Assert.assertEquals(1.5, urd.getLowerBound(), 0.0);
        Assert.assertEquals(3.2, urd.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidBounds() {
        new UniformRealDistribution(3.2, 1.5);
    }

    @Test
    public void testUniformRealDistributionWithLowerBoundInclusive() {
        UniformRealDistribution urd = new UniformRealDistribution(1.5, 3.2, true, false);
        Assert.assertEquals(1.5, urd.getLowerBound(), 0.0);
        Assert.assertEquals(3.2, urd.getUpperBound(), 0.0);
        Assert.assertTrue(urd.isSupportLowerBoundInclusive());
        Assert.assertFalse(urd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.0, urd.density(1.2), 1e-6);
        Assert.assertEquals(1.0 / 6, urd.density(0.5), 1e-6);
        Assert.assertEquals(0.0, urd.density(1.5), 1e-6);
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.0, urd.cumulativeProbability(0.0), 1e-6);
        Assert.assertEquals(0.3333333333333333, urd.cumulativeProbability(0.3333333333333333), 1e-6);
        Assert.assertEquals(1.0, urd.cumulativeProbability(1.0), 1e-6);
    }

    @Test
    public void testUniformRealDistributionNumericalMean() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.5, urd.getNumericalMean(), 1e-6);

        UniformRealDistribution urd1 = new UniformRealDistribution(1.5, 3.2);
        Assert.assertEquals(2.4166666666666665, urd1.getNumericalMean(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionNumericalVariance() {
        UniformRealDistribution urd = new UniformRealDistribution(0, 1);
        Assert.assertEquals(0.0014285714285714285, urd.getNumericalVariance(), 1e-6);

        UniformRealDistribution urd1 = new UniformRealDistribution(1.5, 3.2);
        Assert.assertEquals(0.08414602264150956, urd1.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution urd = new UniformRealDistribution(new Well19937c(), 0, 1);
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue(urd.getSupportLowerBound() <= urd.sample() && urd.sample() < urd.getSupportUpperBound());
        }
    }
}