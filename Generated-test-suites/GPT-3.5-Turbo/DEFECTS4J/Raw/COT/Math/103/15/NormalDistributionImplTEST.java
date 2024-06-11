
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0, distribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.setMean(1);
        Assert.assertEquals(1, distribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testSetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.setStandardDeviation(2);
        Assert.assertEquals(2, distribution.getStandardDeviation(), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.cumulativeProbability(0);
        Assert.assertEquals(0.5, result, 0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.inverseCumulativeProbability(0.5);
        Assert.assertEquals(0, result, 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.getDomainLowerBound(0.5);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, result, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.getDomainUpperBound(0.5);
        Assert.assertEquals(Double.MAX_VALUE, result, 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double result = distribution.getInitialDomain(0.5);
        Assert.assertEquals(1, result, 0);
    }
}
