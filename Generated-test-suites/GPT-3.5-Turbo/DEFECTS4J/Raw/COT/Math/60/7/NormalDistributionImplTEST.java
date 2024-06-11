import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        Assert.assertEquals(mean, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        Assert.assertEquals(sd, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        double x = 15.0;
        double expected = 0.12098536225957168;
        Assert.assertEquals(expected, distribution.density(x), 1e-9);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        double x = 12.0;
        double expected = 0.6914624612740131;
        Assert.assertEquals(expected, distribution.cumulativeProbability(x), 1e-9);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        double p = 0.8;
        double expected = 11.841621238760101;
        Assert.assertEquals(expected, distribution.inverseCumulativeProbability(p), 1e-9);
    }

    @Test
    public void testSample() throws MathException {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        double p = 0.3;
        double expected = -Double.MAX_VALUE;
        Assert.assertEquals(expected, distribution.getDomainLowerBound(p), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        double p = 0.7;
        double expected = Double.MAX_VALUE;
        Assert.assertEquals(expected, distribution.getDomainUpperBound(p), 0);
    }

    @Test
    public void testGetInitialDomain() {
        double mean = 10.0;
        double sd = 2.0;
        NormalDistributionImpl distribution = new NormalDistributionImpl(mean, sd);
        double p = 0.5;
        double expected = mean;
        Assert.assertEquals(expected, distribution.getInitialDomain(p), 0);
    }
}