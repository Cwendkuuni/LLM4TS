import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;

import org.apache.commons.math.distribution.NormalDistributionImpl;

public class NormalDistributionImplTest {

    private NormalDistributionImpl distribution;

    @Before
    public void setUp() {
        distribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        double mean = 0.0;
        Assert.assertEquals(mean, distribution.getMean(), 0.0);
    }

    @Test
    public void testSetMean() {
        double mean = 5.0;
        distribution.setMean(mean);
        Assert.assertEquals(mean, distribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        double sd = 1.0;
        Assert.assertEquals(sd, distribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        double sd = 2.0;
        distribution.setStandardDeviation(sd);
        Assert.assertEquals(sd, distribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        double sd = -1.0;
        distribution.setStandardDeviation(sd);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 0.0;
        double expected = 0.5;
        Assert.assertEquals(expected, distribution.cumulativeProbability(x), 0.01);
    }

    @Test
    public void testCumulativeProbabilityPositiveX() throws MathException {
        double x = 1.0;
        double expected = 0.84134;
        Assert.assertEquals(expected, distribution.cumulativeProbability(x), 0.01);
    }

    @Test
    public void testCumulativeProbabilityNegativeX() throws MathException {
        double x = -1.0;
        double expected = 0.15866;
        Assert.assertEquals(expected, distribution.cumulativeProbability(x), 0.01);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.5;
        double expected = 0.0;
        Assert.assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.01);
    }

    @Test
    public void testInverseCumulativeProbabilityZeroP() throws MathException {
        double p = 0.0;
        double expected = Double.NEGATIVE_INFINITY;
        Assert.assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.01);
    }

    @Test
    public void testInverseCumulativeProbabilityOneP() throws MathException {
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        Assert.assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.01);
    }

    @Test
    public void testDomainLowerBound() {
        double p = 0.4;
        double expected = -Double.MAX_VALUE;
        Assert.assertEquals(expected, distribution.getDomainLowerBound(p), 0.0);
    }

    @Test
    public void testDomainUpperBound() {
        double p = 0.6;
        double expected = Double.MAX_VALUE;
        Assert.assertEquals(expected, distribution.getDomainUpperBound(p), 0.0);
    }

    @Test
    public void testInitialDomain() {
        double p = 0.5;
        double expected = 0.0;
        Assert.assertEquals(expected, distribution.getInitialDomain(p), 0.0);
    }

}