import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(2.0, 3.0);
        Assert.assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);

        fd = new FDistribution(2.0, 3.0, 0.0001);
        Assert.assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(0.0001, fd.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        double density = fd.density(0.5);
        Assert.assertNotNaN(density);
        Assert.assertEquals(density, fd.density(0.5), 1e-6);

        density = fd.density(Double.POSITIVE_INFINITY);
        Assert.assertEquals(density, 0.0, 1e-6);

        density = fd.density(Double.NEGATIVE_INFINITY);
        Assert.assertEquals(density, 0.0, 1e-6);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);

        double cdf = fd.cumulativeProbability(0.5);
        Assert.assertNotNaN(cdf);
        Assert.assertEquals(cdf, 0.691114713050329, 1e-6);

        cdf = fd.cumulativeProbability(0.0);
        Assert.assertEquals(cdf, 0.0, 1e-6);

        cdf = fd.cumulativeProbability(2.0);
        Assert.assertEquals(cdf, 1.0, 1e-6);

        cdf = fd.cumulativeProbability(Double.NEGATIVE_INFINITY);
        Assert.assertEquals(cdf, 0.0, 1e-6);

        cdf = fd.cumulativeProbability(Double.POSITIVE_INFINITY);
        Assert.assertEquals(cdf, 1.0, 1e-6);
    }

    @Test
    public void testFDistributionMean() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double mean = fd.getNumericalMean();
        Assert.assertEquals(mean, 3.0 / 2.0, 1e-6);

        fd = new FDistribution(1.0, 3.0);
        mean = fd.getNumericalMean();
        Assert.assertEquals(mean, Double.NaN);

        fd = new FDistribution(2.0, 1.0);
        mean = fd.getNumericalMean();
        Assert.assertEquals(mean, Double.NaN);
    }

    @Test
    public void testFDistributionVariance() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double variance = fd.getNumericalVariance();
        Assert.assertEquals(variance, 1.5 * (3.0 / (3.0 - 2.0)) / (2.0 * (3.0 - 2.0) * (3.0 - 4.0)), 1e-6);

        fd = new FDistribution(1.0, 3.0);
        variance = fd.getNumericalVariance();
        Assert.assertEquals(variance, Double.NaN);

        fd = new FDistribution(2.0, 1.0);
        variance = fd.getNumericalVariance();
        Assert.assertEquals(variance, Double.NaN);
    }

    @Test
    public void testFDistributionSupport() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double supportLowerBound = fd.getSupportLowerBound();
        Assert.assertEquals(supportLowerBound, 0.0, 1e-6);

        supportLowerBound = fd.getSupportUpperBound();
        Assert.assertEquals(supportLowerBound, Double.POSITIVE_INFINITY, 1e-6);

        boolean isSupportLowerBoundInclusive = fd.isSupportLowerBoundInclusive();
        Assert.assertTrue(isSupportLowerBoundInclusive);

        isSupportLowerBoundInclusive = fd.isSupportUpperBoundInclusive();
        Assert.assertFalse(isSupportLowerBoundInclusive);

        boolean isSupportConnected = fd.isSupportConnected();
        Assert.assertTrue(isSupportConnected);
    }
}