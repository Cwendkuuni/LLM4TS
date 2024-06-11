import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    // Test case for constructor with positive mean
    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(2.5, poisson.getMean(), 0.0001);
    }

    // Test case for constructor with non-positive mean
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonPositiveMean() {
        new PoissonDistributionImpl(-2.5);
    }

    // Test case for probability method with x = 0
    @Test
    public void testProbabilityWithXZero() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.0820849986238988, poisson.probability(0), 0.0001);
    }

    // Test case for probability method with x = 5
    @Test
    public void testProbabilityWithXFive() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.0764692862381605, poisson.probability(5), 0.0001);
    }

    // Test case for probability method with negative x
    @Test
    public void testProbabilityWithNegativeX() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0, poisson.probability(-2), 0.0001);
    }

    // Test case for cumulativeProbability method with x = 0
    @Test
    public void testCumulativeProbabilityWithXZero() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.082085, poisson.cumulativeProbability(0), 0.0001);
    }

    // Test case for cumulativeProbability method with x = 5
    @Test
    public void testCumulativeProbabilityWithXFive() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0.977469, poisson.cumulativeProbability(5), 0.0001);
    }

    // Test case for cumulativeProbability method with negative x
    @Test
    public void testCumulativeProbabilityWithNegativeX() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        Assert.assertEquals(0, poisson.cumulativeProbability(-2), 0.0001);
    }

    // Test case for sample method
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        int sample = poisson.sample();
        Assert.assertTrue(sample >= 0);
        Assert.assertTrue(sample <= Integer.MAX_VALUE);
    }

}