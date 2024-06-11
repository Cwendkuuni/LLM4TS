import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructorValidInput() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, poissonDist.getMean(), 0.0);
    }

    /**
     * Test constructor with invalid input (negative mean).
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorInvalidInput() {
        new PoissonDistributionImpl(-5.0);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        double probabilityZero = poissonDist.probability(0);
        double probabilityFive = poissonDist.probability(5);
        double probabilityTen = poissonDist.probability(10);

        assertEquals(0.27047619e-08, probabilityZero, 1.0e-8);
        assertEquals(0.31781833, probabilityFive, 1.0e-6);
        assertEquals(0.03552713, probabilityTen, 1.0e-6);
    }

    /**
     * Test probability method with edge case (minimum value).
     */
    @Test
    public void testProbabilityMinimumValue() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(0.0);
        double probabilityZero = poissonDist.probability(0);

        assertEquals(1.0, probabilityZero, 1.0e-14);
    }

    /**
     * Test probability method with edge case (maximum value).
     */
    @Test
    public void testProbabilityMaximumValue() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        double probabilityMax = poissonDist.probability(Integer.MAX_VALUE);

        assertEquals(0.0, probabilityMax, 1.0e-14);
    }

    /**
     * Test probability method with negative input.
     */
    @Test
    public void testProbabilityNegativeInput() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        double probabilityNegative = poissonDist.probability(-1);

        assertEquals(0.0, probabilityNegative, 1.0e-14);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        double cumulativeProbabilityZero = poissonDist.cumulativeProbability(0);
        double cumulativeProbabilityFive = poissonDist.cumulativeProbability(5);
        double cumulativeProbabilityTen = poissonDist.cumulativeProbability(10);

        assertEquals(0.017439646, cumulativeProbabilityZero, 1.0e-8);
        assertEquals(0.475981734, cumulativeProbabilityFive, 1.0e-8);
        assertEquals(0.999697656, cumulativeProbabilityTen, 1.0e-8);
    }

    /**
     * Test cumulativeProbability method with edge case (minimum value).
     */
    @Test
    public void testCumulativeProbabilityMinimumValue() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(0.0);
        double cumulativeProbabilityZero = poissonDist.cumulativeProbability(0);

        assertEquals(1.0, cumulativeProbabilityZero, 1.0e-14);
    }

    /**
     * Test cumulativeProbability method with edge case (maximum value).
     */
    @Test
    public void testCumulativeProbabilityMaximumValue() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        double cumulativeProbabilityMax = poissonDist.cumulativeProbability(Integer.MAX_VALUE);

        assertEquals(1.0, cumulativeProbabilityMax, 1.0e-14);
    }

    /**
     * Test cumulativeProbability method with negative input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInput() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        poissonDist.cumulativeProbability(-1);
    }

    /**
     * Test normalApproximateProbability method with valid input.
     */
    @Test
    public void testNormalApproximateProbabilityValidInput() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        double normalApproximateProbabilityZero = poissonDist.normalApproximateProbability(0);
        double normalApproximateProbabilityFive = poissonDist.normalApproximateProbability(5);
        double normalApproximateProbabilityTen = poissonDist.normalApproximateProbability(10);

        assertEquals(0.017439646, normalApproximateProbabilityZero, 1.0e-8);
        assertEquals(0.475981734, normalApproximateProbabilityFive, 1.0e-8);
        assertEquals(0.999697656, normalApproximateProbabilityTen, 1.0e-8);
    }

    /**
     * Test normalApproximateProbability method with edge case (minimum value).
     */
    @Test
    public void testNormalApproximateProbabilityMinimumValue() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(0.0);
        double normalApproximateProbabilityZero = poissonDist.normalApproximateProbability(0);

        assertEquals(1.0, normalApproximateProbabilityZero, 1.0e-14);
    }

    /**
     * Test normalApproximateProbability method with edge case (maximum value).
     */
    @Test
    public void testNormalApproximateProbabilityMaximumValue() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        double normalApproximateProbabilityMax = poissonDist.normalApproximateProbability(Integer.MAX_VALUE);

        assertEquals(1.0, normalApproximateProbabilityMax, 1.0e-14);
    }

    /**
     * Test normalApproximateProbability method with negative input.
     */
    @Test
    public void testNormalApproximateProbabilityNegativeInput() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        double normalApproximateProbabilityNegative = poissonDist.normalApproximateProbability(-1);

        assertEquals(0.0, normalApproximateProbabilityNegative, 1.0e-14);
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleValidInput() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(5.0);
        int sample = poissonDist.sample();

        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
        assertEquals(5.0, poissonDist.getMean(), 0.0);
    }

    /**
     * Test sample method with edge case (mean = 0).
     */
    @Test
    public void testSampleEdgeCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(0.0);
        int sample = poissonDist.sample();

        assertEquals(0, sample);
    }

    /**
     * Test sample method with large mean.
     */
    @Test
    public void testSampleLargeMean() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(100.0);
        int sample = 0;
        for (int i = 0; i < 1000; i++) {
            sample += poissonDist.sample();
        }
        double expectedSampleSum = 1000.0 * poissonDist.getMean();
        assertEquals(expectedSampleSum, sample, 1.0e-6);
    }
}