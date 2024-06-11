import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistortion;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);

        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cdf = distribution.cumulativeProbability(x);
            double pdf = distribution.probabilityDensityFunctionValue(x);
            double sum = cdf + pdf;
            Assert.assertEquals("Error at x=" + x, sum, sum, 1e-8);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double x = distribution.inverseCumulativeProbability(p);
            double pdf = distribution.probabilityDensityFunctionValue(x);
            double cdf = distribution.cumulativeProbability(x);
            double sum = p + pdf * (x - cdf);
            Assert.assertEquals("Error at p=" + p, sum, sum, 1e-8);
        }
    }

    @Test
    public void testSetGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);

        distribution.setNumeratorDegreesOfFreedom(4.0);
        Assert.assertEquals(4.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

        distribution.setDenominatorDegreesOfFreedom(5.0);
        Assert.assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(-2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(-3.0);
    }

    @Test
    public void testFDistortion() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        FDistortion distortion = new FDistortion();

        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double value = distortion.value(distribution, x);
            RealDistribution standardNormal = new org.apache.commons.math3.stat.NormalDistribution();
            double z = standardNormal.inverseCumulativeProbability(value);
            double percentile = distribution.inverseCumulativeProbability(z);
            Assert.assertEquals("Error at x=" + x, percentile, x, 1e-6);
        }
    }
}