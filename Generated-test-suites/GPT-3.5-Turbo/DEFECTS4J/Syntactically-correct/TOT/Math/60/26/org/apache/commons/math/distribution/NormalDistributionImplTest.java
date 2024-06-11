import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(2.5, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(1.5, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(0.10798193302637613, distribution.density(4), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(0.9937903346742238, distribution.cumulativeProbability(5.5), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveException() {
        new NormalDistributionImpl(2.5, -1.5);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(3.9207602172684916, distribution.inverseCumulativeProbability(0.5), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityReturnsNegativeInfinity() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(0), 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityReturnsPositiveInfinity() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.25), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.25), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.5, 1.5);
        Assert.assertEquals(4, distribution.getInitialDomain(0.75), 0.0001);
    }

}