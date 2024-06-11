import org.junit.Test;

public class PoissonDistributionImplTest {

    // Test cases for constructor with mean
    @Test
    public void testConstructorWithMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poisson.getMean(), 0.0001);
    }

    // Test cases for constructor with mean, epsilon, and maxIterations
    @Test
    public void testConstructorWithMeanEpsilonMaxIterations() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(3.0, 1E-10, 20000000);
        assertEquals(3.0, poisson.getMean(), 0.0001);
        assertEquals(1E-10, poisson.epsilon, 0.0001);
        assertEquals(20000000, poisson.maxIterations);
    }

    // Test case for negative mean in constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(-2.0);
    }

    // Test case for probability function with x = 0
    @Test
    public void testProbabilityX0() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0.082085, poisson.probability(0), 0.0001);
    }

    // Test case for probability function with x = 3
    @Test
    public void testProbabilityX3() {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0.213909, poisson.probability(3), 0.0001);
    }

    // Test case for cumulativeProbability function with x = 2
    @Test
    public void testCumulativeProbabilityX2() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0.7788008, poisson.cumulativeProbability(2), 0.0001);
    }

    // Test case for cumulativeProbability function with x = -1
    @Test
    public void testCumulativeProbabilityXNegative() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, poisson.cumulativeProbability(-1), 0.0001);
    }

    // Test case for normalApproximateProbability function with x = 2
    @Test
    public void testNormalApproximateProbabilityX2() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(0.842719, poisson.normalApproximateProbability(2), 0.0001);
    }

    // Test case for sample function
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        int sample = poisson.sample();
        assertTrue(sample >= 0);
    }

    // Test case for inverseCumulativeProbability function for p = 0.5
    @Test
    public void testInverseCumulativeProbabilityP0_5() throws MathException {
        PoissonDistributionImpl poisson = new PoissonDistributionImpl(2.5);
        assertEquals(3, poisson.inverseCumulativeProbability(0.5));
    }

}