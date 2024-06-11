// JUnit 4 test cases for NormalDistributionImpl class

import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    // Test getMean method
    @Test
    public void testGetMean() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0, distribution.getMean(), 0);
    }

    // Test getStandardDeviation method
    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1, distribution.getStandardDeviation(), 0);
    }

    // Test density method
    @Test
    public void testDensity() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.3989422804014327, distribution.density(0), 1e-9);
    }

    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.5, distribution.cumulativeProbability(0), 1e-9);
    }

    // Test inverseCumulativeProbability method with an invalid probability
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalid() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        distribution.inverseCumulativeProbability(1.5);
    }

    // Test inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(1.2815515655446006, distribution.inverseCumulativeProbability(0.9), 1e-9);
    }

    // Test sample method
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        double sample = distribution.sample();
        Assert.assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    // Test getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(-Double.MAX_VALUE, distribution.getDomainLowerBound(0.4), 0);
    }

    // Test getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(Double.MAX_VALUE, distribution.getDomainUpperBound(0.4), 0);
    }

    // Test getInitialDomain method
    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(-1, distribution.getInitialDomain(0.4), 0);
    }

}