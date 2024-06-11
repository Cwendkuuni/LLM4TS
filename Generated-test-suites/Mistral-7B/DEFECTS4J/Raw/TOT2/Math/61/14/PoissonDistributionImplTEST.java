import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testValidConstruction() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        assertEquals(5.0, pd.getMean(), 0.0);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidConstructionNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbabilityValidInput() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        assertEquals(0.02702702702702703, pd.probability(1), 0.0001);
        assertEquals(0.3679243340240765, pd.probability(3), 0.0001);
        assertEquals(0.682689412572653, pd.probability(5), 0.0001);
        assertEquals(0.7833552884814547, pd.probability(6), 0.0001);
        assertEquals(1.0, pd.probability(Integer.MAX_VALUE), 0.0001);
    }

    /**
     * Test probability method with invalid input.
     */
    @Test
    public void testProbabilityInvalidInput() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        assertEquals(0.0, pd.probability(-1), 0.0);
        assertEquals(0.0, pd.probability(Integer.MIN_VALUE), 0.0);
    }

    /**
     * Test cumulativeProbability method with valid inputs.
     */
    @Test
    public void testCumulativeProbabilityValidInputs() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        assertEquals(0.04982854936452455, pd.cumulativeProbability(0), 0.0001);
        assertEquals(0.13533528323661275, pd.cumulativeProbability(1), 0.0001);
        assertEquals(0.4212653198422825, pd.cumulativeProbability(3), 0.0001);
        assertEquals(0.6792217784724152, pd.cumulativeProbability(5), 0.0001);
        assertEquals(1.0, pd.cumulativeProbability(Integer.MAX_VALUE), 0.0001);
    }

    /**
     * Test cumulativeProbability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        pd.cumulativeProbability(-1);
    }

    /**
     * Test normalApproximateProbability method with valid inputs.
     */
    @Test
    public void testNormalApproximateProbabilityValidInputs() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        assertEquals(0.04982854936452455, pd.normalApproximateProbability(0), 0.0001);
        assertEquals(0.13533528323661275, pd.normalApproximateProbability(1), 0.0001);
        assertEquals(0.6142086792420453, pd.normalApproximateProbability(3), 0.0001);
        assertEquals(0.8414061368365984, pd.normalApproximateProbability(5), 0.0001);
        assertEquals(1.0, pd.normalApproximateProbability(Integer.MAX_VALUE), 0.0001);
    }

    /**
     * Test normalApproximateProbability method with invalid input.
     */
    @Test(expected = MathException.class)
    public void testNormalApproximateProbabilityInvalidInput() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        pd.normalApproximateProbability(-1);
    }

    /**
     * Test sample method with valid mean.
     */
    @Test
    public void testSampleValidMean() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(5.0);
        int sample = pd.sample();
        assertTrue(sample >= 0 && sample <= 1000 * pd.getMean());
    }

    /**
     * Test sample method with large mean.
     */
    @Test
    public void testSampleLargeMean() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(100000.0);
        int sample = pd.sample();
        assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
    }

    /**
     * Test sample method with negative mean.
     */
    @Test(expected = MathRuntimeError.class)
    public void testSampleNegativeMean() throws MathException {
        new PoissonDistributionImpl(-1.0).sample();
    }
}