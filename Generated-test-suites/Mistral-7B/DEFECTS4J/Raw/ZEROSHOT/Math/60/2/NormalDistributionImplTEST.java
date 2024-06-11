import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.0, dist.getMean(), 0.0);
        Assert.assertEquals(1.0, dist.getStandardDeviation(), 0.0);

        NormalDistribution distMean2 = new NormalDistributionImpl(2, 1);
        Assert.assertEquals(2.0, distMean2.getMean(), 0.0);

        NormalDistribution distSD2 = new NormalDistributionImpl(0, 2);
        Assert.assertEquals(0.0, distSD2.getMean(), 0.0);
        Assert.assertEquals(2.0, distSD2.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double eps = 1e-6;

        double result = dist.density(0.5);
        double expected = 0.398942280401;
        Assert.assertEquals(expected, result, eps);

        result = dist.density(-2.0);
        expected = 0.00022703849621;
        Assert.assertEquals(expected, result, eps);

        result = dist.density(3.0);
        expected = 0.00022703849621;
        Assert.assertEquals(expected, result, eps);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        double result = dist.cumulativeProbability(0.5);
        double expected = 0.682689462137;
        Assert.assertEquals(expected, result, 1e-6);

        result = dist.cumulativeProbability(-3.0);
        expected = 0.0;
        Assert.assertEquals(expected, result, 1e-6);

        result = dist.cumulativeProbability(3.0);
        expected = 1.0;
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSD() {
        new NormalDistributionImpl(0, -1);
    }
}