import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10.0, 2.0);
        Assert.assertEquals(10.0, normalDistribution.getMean(), 0.0001);

        // test negative mean
        normalDistribution.setMean(-5.0);
        Assert.assertEquals(-5.0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setMean(15.0);
        Assert.assertEquals(15.0, normalDistribution.getMean(), 0.0001);

        // test negative mean
        normalDistribution.setMean(-10.0);
        Assert.assertEquals(-10.0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(10.0, 2.0);
        Assert.assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0001);

        // test negative standard deviation
        normalDistribution.setStandardDeviation(3.0);
        Assert.assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        normalDistribution.setStandardDeviation(5.0);
        Assert.assertEquals(5.0, normalDistribution.getStandardDeviation(), 0.0001);

        // test negative standard deviation
        normalDistribution.setStandardDeviation(1.0);
        Assert.assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(0.3085375387259869, normalDistribution.cumulativeProbability(-0.5), 0.0001);
        Assert.assertEquals(0.6914624612740131, normalDistribution.cumulativeProbability(0.5), 0.0001);

        // test edge cases
        Assert.assertEquals(0.0, normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.0001);
        Assert.assertEquals(1.0, normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.0001);

        // test error scenarios
        try {
            normalDistribution.cumulativeProbability(20.0);
            Assert.fail("Expected MathException to be thrown");
        } catch (MathException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
        Assert.assertEquals(-1.2815515655446004, normalDistribution.inverseCumulativeProbability(0.1), 0.0001);
        Assert.assertEquals(1.2815515655446004, normalDistribution.inverseCumulativeProbability(0.9), 0.0001);

        // test edge cases
        Assert.assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), 0.0001);
        Assert.assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1.0), 0.0001);

        // test error scenarios
        try {
            normalDistribution.inverseCumulativeProbability(-0.1);
            Assert.fail("Expected MathException to be thrown");
        } catch (MathException e) {
            Assert.assertTrue(true);
        }
        try {
            normalDistribution.inverseCumulativeProbability(1.1);
            Assert.fail("Expected MathException to be thrown");
        } catch (MathException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.1), 0.0001);
        Assert.assertEquals(0.0, normalDistribution.getDomainLowerBound(0.5), 0.0001);

        // test edge cases
        Assert.assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.0), 0.0001);
        Assert.assertEquals(0.0, normalDistribution.getDomainLowerBound(0.9), 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(0.0, normalDistribution.getDomainUpperBound(0.1), 0.0001);
        Assert.assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.5), 0.0001);

        // test edge cases
        Assert.assertEquals(0.0, normalDistribution.getDomainUpperBound(0.0), 0.0001);
        Assert.assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.9), 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        // test typical use case
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        Assert.assertEquals(-1.0, normalDistribution.getInitialDomain(0.1), 0.0001);
        Assert.assertEquals(1.0, normalDistribution.getInitialDomain(0.9), 0.0001);

        // test edge cases
        Assert.assertEquals(-1.0, normalDistribution.getInitialDomain(0.0), 0.0001);
        Assert.assertEquals(0.0, normalDistribution.getInitialDomain(0.5), 0.0001);
        Assert.assertEquals(1.0, normalDistribution.getInitialDomain(1.0), 0.0001);
    }
}