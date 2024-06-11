import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

/**
 * Test class for {@link PoissonDistributionImpl}
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid parameters
     * @throws MathException
     */
    @Test
    public void testConstructor() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, distribution.getMean(), 0.01);
    }

    /**
     * Test constructor with invalid parameters
     * @throws MathException
     */
    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() throws MathException {
        new PoissonDistributionImpl(-1.2);
    }

    /**
     * Test probability method
     * @throws MathException
     */
    @Test
    public void testProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.18045752525825933, distribution.probability(1), 0.0001);
        assertEquals(0.47879751686423444, distribution.probability(2), 0.0001);
        assertEquals(0.13559993953128045, distribution.probability(3), 0.0001);
        assertEquals(0.02274645313282281, distribution.probability(4), 0.0001);
    }

    /**
     * Test cumulative probability method
     * @throws MathException
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(0.18045752525825933, distribution.cumulativeProbability(0), 0.0001);
        assertEquals(0.36091505051651867, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.55137267577477801, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.81621019644335265, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.97769590187545355, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    /**
     * Test normalApproximateProbability method
     * @throws MathException
     */
    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        assertEquals(0.18045752525825933, distribution.normalApproximateProbability(0), 0.0001);
        assertEquals(0.36091505051651867, distribution.normalApproximateProbability(1), 0.0001);
        assertEquals(0.55137267577477801, distribution.normalApproximateProbability(2), 0.0001);
        assertEquals(0.81621019644335265, distribution.normalApproximateProbability(3), 0.0001);
        assertEquals(0.97769590187545355, distribution.normalApproximateProbability(4), 0.0001);
    }

    /**
     * Test sample method
     * @throws MathException
     */
    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        int sample = distribution.sample();
        assertEquals(1, sample, 0);
        sample = distribution.sample();
        assertEquals(2, sample, 0);
        sample = distribution.sample();
        assertEquals(1, sample, 0);
    }

    /**
     * Test inverseCumulativeProbability method
     * @throws MathException
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        double p = 0.1;
        int x = distribution.inverseCumulativeProbability(p);
        assertEquals(1, x, 0);

        p = 0.36091505051651867;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(1, x, 0);

        p = 0.55137267577477801;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(2, x, 0);

        p = 0.81621019644335265;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(3, x, 0);

        p = 0.97769590187545355;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(4, x, 0);
    }

    /**
     * Test Gamma.regularizedGammaQ method with default values
     * @throws MathException
     */
    @Test
    public void testCumulativeProbabilityWithDefaultValues() throws MathException {
        double mean = 2.5;
        int x = 4;
        double expected = Gamma.regularizedGammaQ(x + 1, mean);
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean);
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, Precision.SAFE_EPSILON);
    }

    /**
     * Test Gamma.regularizedGammaQ method with custom convergence criterion and maximum iterations
     * @throws MathException
     */
    @Test
    public void testCumulativeProbabilityWithCustomValues() throws MathException {
        double mean = 2.5;
        int x = 12;
        double epsilon = 1e-6;
        int maxIterations = 100000;
        double expected = Gamma.regularizedGammaQ(x + 1, mean, epsilon, maxIterations);
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(mean, epsilon, maxIterations);
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, Precision.SAFE_EPSILON);
    }
}