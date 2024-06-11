import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testConstructorValidParameters() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, pd.getMean(), 0.001);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double prob = pd.probability(1);
        assertEquals(0.223144, prob, 0.0001);
    }

    /**
     * Test probability method with negative input.
     */
    @Test
    public void testProbabilityNegativeInput() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double prob = pd.probability(-1);
        assertEquals(0.0, prob, 0.0);
    }

    /**
     * Test probability method with max value input.
     */
    @Test
    public void testProbabilityMaxValueInput() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double prob = pd.probability(Integer.MAX_VALUE);
        assertEquals(1.0, prob, 0.001);
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double cp = pd.cumulativeProbability(1);
        assertEquals(0.367925304546144, cp, 0.0001);
    }

    /**
     * Test cumulativeProbability method with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityNegativeInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        pd.cumulativeProbability(-1);
    }

    /**
     * Test cumulativeProbability method with max value input.
     */
    @Test
    public void testCumulativeProbabilityMaxValueInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double cp = pd.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(1.0, cp, 0.001);
    }

    /**
     * Test normalApproximateProbability method with valid input.
     */
    @Test
    public void testNormalApproximateProbabilityValidInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double prob = pd.normalApproximateProbability(1);
        assertEquals(0.223144, prob, 0.0001);
    }

    /**
     * Test normalApproximateProbability method with negative input.
     */
    @Test
    public void testNormalApproximateProbabilityNegativeInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double prob = pd.normalApproximateProbability(-1);
        assertEquals(0.0, prob, 0.0);
    }

    /**
     * Test normalApproximateProbability method with max value input.
     */
    @Test
    public void testNormalApproximateProbabilityMaxValueInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double prob = pd.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(1.0, prob, 0.001);
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSampleValidInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        int sample = pd.sample();
        assertEquals(1, sample);
    }

    /**
     * Test sample method with very large mean.
     */
    @Test
    public void testSampleVeryLargeMean() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(100000.0);
        int sample = pd.sample();
        // The sample should be less than or equal to 100000, but greater than or equal to 0
        assertEquals(true, sample >= 0 && sample <= 100000);
    }

    /**
     * Test sample method with very small mean.
     */
    @Test
    public void testSampleVerySmallMean() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(0.01);
        int sample = pd.sample();
        // The sample should be less than or equal to 10, but greater than or equal to 0
        assertEquals(true, sample >= 0 && sample <= 10);
    }

    /**
     * Test inverseCumulativeProbability method with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        double p = 0.367925304546144;
        int x = pd.inverseCumulativeProbability(p);
        assertEquals(1, x);
    }

    /**
     * Test inverseCumulativeProbability method with input greater than 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityGreaterThan1() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        pd.inverseCumulativeProbability(1.1);
    }

    /**
     * Test inverseCumulativeProbability method with input less than 0
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityLessThan0() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(2.5);
        pd.inverseCumulativeProbability(-0.1);
    }

}