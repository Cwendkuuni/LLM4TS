import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 2.0);
        Assert.assertEquals(2.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setMean(3.0);
        Assert.assertEquals(3.0, distribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 2.0);
        Assert.assertEquals(2.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl();
        distribution.setStandardDeviation(3.0);
        Assert.assertEquals(3.0, distribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 2.0);
        double result = distribution.cumulativeProbability(1.0);
        Assert.assertEquals(0.1587, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 2.0);
        double result = distribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 2.0);
        double result = distribution.getDomainLowerBound(0.3);
        Assert.assertEquals(-1.7976931348623157E308, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 2.0);
        double result = distribution.getDomainUpperBound(0.7);
        Assert.assertEquals(1.7976931348623157E308, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(2.0, 2.0);
        double result = distribution.getInitialDomain(0.5);
        Assert.assertEquals(4.0, result, 0.0001);
    }
}