import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;

public class NormalDistributionImplTest {

    // Test cases for constructor with mean and standard deviation
    @Test
    public void testConstructorWithMeanAndStandardDeviation() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
            Assert.assertEquals(0, normalDistribution.getMean(), 0);
            Assert.assertEquals(1, normalDistribution.getStandardDeviation(), 0);
        } catch (Exception e) {
            Assert.fail("Constructor with mean and standard deviation throws an exception.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithMeanAndStandardDeviationThrowsException() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, -1);
    }

    // Test case for constructor with default mean and standard deviation
    @Test
    public void testConstructorWithDefaultMeanAndStandardDeviation() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
            Assert.assertEquals(0, normalDistribution.getMean(), 0);
            Assert.assertEquals(1, normalDistribution.getStandardDeviation(), 0);
        } catch (Exception e) {
            Assert.fail("Constructor with default mean and standard deviation throws an exception.");
        }
    }

    // Test case for density method
    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.3989422804014327, normalDistribution.density(0), 0.00001);
    }

    // Test cases for cumulativeProbability method
    @Test
    public void testCumulativeProbability() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
            Assert.assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0);
        } catch (MathException e) {
            Assert.fail("cumulativeProbability method throws an exception.");
        }
    }

    @Test
    public void testCumulativeProbabilityLowerBound() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
            Assert.assertEquals(0, normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0);
        } catch (MathException e) {
            Assert.fail("cumulativeProbability method throws an exception.");
        }
    }

    @Test
    public void testCumulativeProbabilityUpperBound() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
            Assert.assertEquals(1, normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0);
        } catch (MathException e) {
            Assert.fail("cumulativeProbability method throws an exception.");
        }
    }

    // Test cases for inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
            Assert.assertEquals(0, normalDistribution.inverseCumulativeProbability(0.5), 0);
        } catch (MathException e) {
            Assert.fail("inverseCumulativeProbability method throws an exception.");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
            Assert.assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
        } catch (MathException e) {
            Assert.fail("inverseCumulativeProbability method throws an exception.");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
            Assert.assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
        } catch (MathException e) {
            Assert.fail("inverseCumulativeProbability method throws an exception.");
        }
    }

    // Test case for sample method
    @Test
    public void testSample() {
        try {
            NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
            normalDistribution.sample();
        } catch (MathException e) {
            Assert.fail("sample method throws an exception.");
        }
    }

    // Test case for getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.25), 0);
    }

    // Test case for getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.75), 0);
    }

    // Test case for getInitialDomain method
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(-1, normalDistribution.getInitialDomain(0.25), 0);
    }

}