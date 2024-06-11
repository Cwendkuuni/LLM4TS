import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    /**
     * Test construction of PoissonDistributionImpl with valid parameters.
     */
    @Test
    public void testConstruction() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        assertEquals(2.5, poissonDistributionImpl.getMean(), 0.01);
    }

    /**
     * Test construction of PoissonDistributionImpl with negative mean.
     */
    @Test(expected = MathRuntimeException.class)
    public void testNegativeMeanConstruction() {
        new PoissonDistributionImpl(-1.5);
    }

    /**
     * Test probability method with valid input.
     */
    @Test
    public void testProbability() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        double probability = poissonDistributionImpl.probability(1);
        double expectedProbability = Gamma.regularizedGammaP(1.5, 2.5);
        assertEquals(expectedProbability, probability, 0.01);
    }

    /**
     * Test probability method with edge case input (Integer.MAX_VALUE).
     */
    @Test
    public void testProbabilityEdgeCase() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        double probability = poissonDistributionImpl.probability(Integer.MAX_VALUE);
        double expectedProbability = Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, 2.5);
        assertEquals(expectedProbability, probability, 0.01);
    }

    /**
     * Test probability method with error case input (negative number).
     */
    @Test
    public void testProbabilityErrorCase() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        try {
            poissonDistributionImpl.probability(-1);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    /**
     * Test cumulativeProbability method with valid input.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        double cumulativeProbability = poissonDistributionImpl.cumulativeProbability(1);
        double expectedCumulativeProbability = Gamma.regularizedGammaQ(1.5, 2.5);
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.01);
    }

    /**
     * Test cumulativeProbability method with edge case input (Integer.MAX_VALUE).
     */
    @Test
    public void testCumulativeProbabilityEdgeCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        double cumulativeProbability = poissonDistributionImpl.cumulativeProbability(Integer.MAX_VALUE);
        double expectedCumulativeProbability = 1.0;
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.01);
    }

    /**
     * Test cumulativeProbability method with error case input (negative number).
     */
    @Test
    public void testCumulativeProbabilityErrorCase() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        try {
            poissonDistributionImpl.cumulativeProbability(-1);
            fail("Expected MathRuntimeException");
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test normalApproximateProbability method with valid input.
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        double normalApproximateProbability = poissonDistributionImpl.normalApproximateProbability(1);
        double expectedNormalProbability = Gamma.regularizedGammaP(1.5, 2.5) + 0.1245;
        assertEquals(expectedNormalProbability, normalApproximateProbability, 0.01);
    }

    /**
     * Test normalApproximateProbability method with edge case input (Integer.MAX_VALUE).
     */
    @Test
    public void testNormalApproximateProbabilityEdgeCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        double normalApproximateProbability = poissonDistributionImpl.normalApproximateProbability(Integer.MAX_VALUE);
        double expectedNormalProbability = 1.0;
        assertEquals(expectedNormalProbability, normalApproximateProbability, 0.01);
    }

    /**
     * Test normalApproximateProbability method with error case input (negative number).
     */
    @Test
    public void testNormalApproximateProbabilityErrorCase() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        try {
            poissonDistributionImpl.normalApproximateProbability(-1);
            fail("Expected MathRuntimeException");
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test sample method with valid input.
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        int sample = poissonDistributionImpl.sample();
        assertEquals(Precision.round(2.5 * Math.log(Math.random()), 0.01), poissonDistributionImpl.getMean());
        assertTrue(sample >= 0 && sample <= 1000 * 2.5);
    }

    /**
     * Test sample method with edge case input (maximum number of iterations).
     */
    @Test
    public void testSampleEdgeCase() throws MathException {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5, 10000000);
        PoissonDistributionImpl poissonDistributionImpl = (PoissonDistributionImpl) poissonDist;
        int sample = poissonDistributionImpl.sample();
        assertEquals(Precision.round(2.5 * Math.log(Math.random()), 0.01), poissonDistributionImpl.getMean());
        assertTrue(sample >= 0 && sample <= 1000 * 2.5);
    }

    /**
     * Test sample method with error case input (negative mean).
     */
    @Test(expected = MathRuntimeException.class)
    public void testSampleErrorCase() {
        new PoissonDistributionImpl(-1.5);
    }
}