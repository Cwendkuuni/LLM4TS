import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl();

    // Test for getMean()
    @Test
    public void testGetMean() {
        double mean = normalDistributionImpl.getMean();

        Assert.assertEquals(0, mean, 0);
    }

    // Test for setMean()
    @Test
    public void testSetMean() {
        normalDistributionImpl.setMean(5.5);
        double mean = normalDistributionImpl.getMean();

        Assert.assertEquals(5.5, mean, 0);
    }

    // Test for getStandardDeviation()
    @Test
    public void testGetStandardDeviation() {
        double standardDeviation = normalDistributionImpl.getStandardDeviation();

        Assert.assertEquals(1, standardDeviation, 0);
    }

    // Test for setStandardDeviation()
    @Test
    public void testSetStandardDeviation() {
        normalDistributionImpl.setStandardDeviation(2.5);
        double standardDeviation = normalDistributionImpl.getStandardDeviation();

        Assert.assertEquals(2.5, standardDeviation, 0);
    }

    // Test for cumulativeProbability() when x is within 20 standard deviations from the mean
    @Test
    public void testCumulativeProbabilityWithinRange() throws MathException {
        double x = 1.5;
        double cumulativeProbability = normalDistributionImpl.cumulativeProbability(x);

        Assert.assertEquals(0.9331927987311419, cumulativeProbability, 0);
    }

    // Test for cumulativeProbability() when x is more than 20 standard deviations from the mean
    @Test
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        double x = 50;
        double cumulativeProbability = normalDistributionImpl.cumulativeProbability(x);

        Assert.assertEquals(1, cumulativeProbability, 0);
    }

    // Test for inverseCumulativeProbability() when p = 0
    @Test
    public void testInverseCumulativeProbabilityP0() throws MathException {
        double p = 0;
        double inverseCumulativeProbability = normalDistributionImpl.inverseCumulativeProbability(p);

        Assert.assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    // Test for inverseCumulativeProbability() when p = 1
    @Test
    public void testInverseCumulativeProbabilityP1() throws MathException {
        double p = 1;
        double inverseCumulativeProbability = normalDistributionImpl.inverseCumulativeProbability(p);

        Assert.assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    // Test for inverseCumulativeProbability() when 0 < p < 1
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.75;
        double inverseCumulativeProbability = normalDistributionImpl.inverseCumulativeProbability(p);

        Assert.assertEquals(0.6744897501960817, inverseCumulativeProbability, 0);
    }

    // Test for getDomainLowerBound() when p < 0.5
    @Test
    public void testGetDomainLowerBoundPLessThanHalf() {
        double p = 0.25;
        double domainLowerBound = normalDistributionImpl.getDomainLowerBound(p);

        Assert.assertEquals(-Double.MAX_VALUE, domainLowerBound, 0);
    }

    // Test for getDomainLowerBound() when p >= 0.5
    @Test
    public void testGetDomainLowerBoundPGreaterThanHalf() {
        double p = 0.75;
        double domainLowerBound = normalDistributionImpl.getDomainLowerBound(p);

        Assert.assertEquals(0, domainLowerBound, 0);
    }

    // Test for getDomainUpperBound() when p < 0.5
    @Test
    public void testGetDomainUpperBoundPLessThanHalf() {
        double p = 0.25;
        double domainUpperBound = normalDistributionImpl.getDomainUpperBound(p);

        Assert.assertEquals(0, domainUpperBound, 0);
    }

    // Test for getDomainUpperBound() when p >= 0.5
    @Test
    public void testGetDomainUpperBoundPGreaterThanHalf() {
        double p = 0.75;
        double domainUpperBound = normalDistributionImpl.getDomainUpperBound(p);

        Assert.assertEquals(Double.MAX_VALUE, domainUpperBound, 0);
    }

    // Test for getInitialDomain() when p < 0.5
    @Test
    public void testGetInitialDomainPLessThanHalf() {
        double p = 0.25;
        double initialDomain = normalDistributionImpl.getInitialDomain(p);

        Assert.assertEquals(-1, initialDomain, 0);
    }

    // Test for getInitialDomain() when p > 0.5
    @Test
    public void testGetInitialDomainPGreaterThanHalf() {
        double p = 0.75;
        double initialDomain = normalDistributionImpl.getInitialDomain(p);

        Assert.assertEquals(1, initialDomain, 0);
    }

}