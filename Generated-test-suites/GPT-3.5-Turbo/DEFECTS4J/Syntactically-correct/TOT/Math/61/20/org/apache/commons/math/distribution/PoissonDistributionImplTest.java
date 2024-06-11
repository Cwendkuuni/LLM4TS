import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    /**
     * Test method for {@link PoissonDistributionImpl#probability(int)}.
     */
    @Test
    public void testProbability() {
        // Create a Poisson distribution with a mean of 5
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);

        // Test for x = 0
        Assert.assertEquals(0.006737946999085468, poisson.probability(0), 0.000001);

        // Test for x = 2
        Assert.assertEquals(0.08422433748856834, poisson.probability(2), 0.000001);

        // Test for x = 5
        Assert.assertEquals(0.1754673697678507, poisson.probability(5), 0.000001);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        // Create a Poisson distribution with a mean of 5
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);

        // Test for x = 0
        Assert.assertEquals(0.006737946999085469, poisson.cumulativeProbability(0), 0.000001);

        // Test for x = 2
        Assert.assertEquals(0.12465201948308107, poisson.cumulativeProbability(2), 0.000001);

        // Test for x = 5
        Assert.assertEquals(0.6159606540480196, poisson.cumulativeProbability(5), 0.000001);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#normalApproximateProbability(int)}.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        // Create a Poisson distribution with a mean of 5
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);

        // Test for x = 0
        Assert.assertEquals(0.0034669738033676733, poisson.normalApproximateProbability(0), 0.000001);

        // Test for x = 2
        Assert.assertEquals(0.06680720126885807, poisson.normalApproximateProbability(2), 0.000001);

        // Test for x = 5
        Assert.assertEquals(0.644858245964781, poisson.normalApproximateProbability(5), 0.000001);
    }

    /**
     * Test method for {@link PoissonDistributionImpl#sample()}.
     */
    @Test
    public void testSample() throws MathException {
        // Create a Poisson distribution with a mean of 5
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(5);

        // Generate 1000 random samples and check if they are within range
        for (int i = 0; i < 1000; i++) {
            int sample = poisson.sample();
            Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        }
    }
}