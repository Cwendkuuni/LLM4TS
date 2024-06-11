import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for PoissonDistributionImpl.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor when mean is positive.
     * @throws MathException if an error occurs in constructor
     */
    @Test
    public void testConstructorPositiveMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.5);
        assertEquals("Mean should be set correctly", 1.5, dist.getMean(), 0.001);
    }

    /**
     * Test constructor when mean is non-positive.
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorNonPositiveMean() {
        new PoissonDistributionImpl(-2.0);
    }

    /**
     * Test probability method when x is less than 0.
     */
    @Test
    public void testProbabilityLessThanZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals("Probability should be 0 for negative x", 0, dist.probability(-1), 0.0);
    }

    /**
     * Test probability method when x is equal to 0.
     */
    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals("Probability should be e^(-mean) for x = 0", Math.E, dist.probability(0), 0.001);
    }

    /**
     * Test probability method when x is greater than 0.
     */
    @Test
    public void testProbabilityGreaterThanZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double x = 3;
        double expectedProbability = FastMath.exp(-1.0) * FastMath.pow(FastMath.E, x) / MathUtils.TWO_PI * Math.sqrt(x);
        assertEquals("Probability should match expected value", expectedProbability, dist.probability(x), 0.001);
    }

    /**
     * Test cumulativeProbability method when x is less than 0.
     */
    @Test
    public void testCumulativeProbabilityLessThanZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        assertEquals("Cumulative probability should be 0 for negative x", 0, dist.cumulativeProbability(-1), 0.0);
    }

    /**
     * Test cumulativeProbability method when x is equal to 0.
     */
    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double expectedValue = Math.E;
        double actualValue = dist.cumulativeProbability(0);
        assertEquals("Cumulative probability should match expected value", expectedValue, actualValue, 0.001);
    }

    /**
     * Test cumulativeProbability method when x is greater than 0.
     * @throws MathException if an error occurs computing the cumulative probability
     */
    @Test
    public void testCumulativeProbabilityGreaterThanZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double x = 3;
        double expectedValue = Gamma.regularizedGammaQ((double) x + 1, 1.0, 0.0, Integer.MAX_VALUE);
        double actualValue = dist.cumulativeProbability(x);
        assertEquals("Cumulative probability should match expected value", expectedValue, actualValue, 0.001);
    }

    /**
     * Test normalApproximateProbability method when x is less than 0.
     */
    @Test
    public void testNormalApproximateProbabilityLessThanZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double x = -1.0;
        try {
            dist.normalApproximateProbability((int) x);
            fail("Exception should be thrown for negative x");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test normalApproximateProbability method when x is equal to 0.
     */
    @Test
    public void testNormalApproximateProbabilityZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double x = 0.0;
        double expectedValue = 0.5;
        double actualValue = dist.normalApproximateProbability((int) x);
        assertEquals("Normal approximate probability should match expected value", expectedValue, actualValue, 0.001);
    }

    /**
     * Test normalApproximateProbability method when x is greater than 0.
     * @throws MathException if an error occurs computing the normal approximation
     */
    @Test
    public void testNormalApproximateProbabilityGreaterThanZero() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        double x = 3.0;
        double expectedValue = 0.5 * (1 + FastMath.erf(FastMath.sqrt(2 * 1.0) * (x - 1.0) / 2) / Math.sqrt(2 * Math.PI));
        double actualValue = dist.normalApproximateProbability((int) x);
        assertEquals("Normal approximate probability should match expected value", expectedValue, actualValue, 0.001);
    }

    /**
     * Test sample method when mean is small.
     * @throws MathException if an error occurs generating the random value
     */
    @Test
    public void testSampleSmallMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(0.5);
        int expectedValue = dist.sample();
        int actualValue = dist.sample();
        assertEquals("Sample value should match expected value", expectedValue, actualValue);
    }

    /**
     * Test sample method when mean is large.
     * @throws MathException if an error occurs generating the random value
     */
    @Test
    public void testSampleLargeMean() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1000.0);
        int expectedValue = dist.sample();
        int actualValue = dist.sample();
        assertTrue("Sample value should not be greater than mean", expectedValue <= 1000);
        assertTrue("Sample value should not be less than 0", expectedValue >= 0);
        assertEquals("Sample value should match expected value", expectedValue, actualValue);
    }

    /**
     * Test inverseCumulativeProbability method when p is less than 0.
     */
    @Test(expected = MathRuntimeException.class)
    public void testInverseCumulativeProbabilityLessThanZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        dist.inverseCumulativeProbability(-0.1);
    }

    /**
     * Test inverseCumulativeProbability method when p is greater than 1.
     */
    @Test
    public void testInverseCumulativeProbabilityGreaterThanOne() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int x = dist.inverseCumulativeProbability(1.1);
        assertTrue("Inverse cumulative probability should be greater than or equal to 0", x >= 0);
        assertTrue("Inverse cumulative probability should be less than or equal to the mean", x <= 1);
    }

    /**
     * Test inverseCumulativeProbability method when p is equal to 0.
     */
    @Test
    public void testInverseCumulativeProbabilityZero() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1.0);
        int x = dist.inverseCumulativeProbability(0.0);
        assertEquals("Inverse cumulative probability should be 0 for p = 0", 0, x);
    }
}