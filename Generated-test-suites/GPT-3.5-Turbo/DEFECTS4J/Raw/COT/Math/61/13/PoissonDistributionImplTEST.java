import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.PoissonDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testGetMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        Assert.assertEquals(10, distribution.getMean(), 0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        Assert.assertEquals(0.1251, distribution.probability(5), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        Assert.assertEquals(0.1711, distribution.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        Assert.assertEquals(0.6915, distribution.normalApproximateProbability(5), 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        int sample = distribution.sample();
        Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        Assert.assertEquals(0, distribution.getDomainLowerBound(0.5), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        Assert.assertEquals(Integer.MAX_VALUE, distribution.getDomainUpperBound(0.5), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMean() {
        new PoissonDistributionImpl(0);
    }

    @Test(expected = MathException.class)
    public void testInvalidCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        distribution.cumulativeProbability(-5);
    }
  
    @Test(expected = MathException.class)
    public void testInvalidNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(10);
        distribution.normalApproximateProbability(-5);
    }
  
    @Test(expected = MathException.class)
    public void testInvalidSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(0);
        distribution.sample();
    }

    // Additional test cases for edge cases and exception handling scenarios should be added here

    // 

    // 
}