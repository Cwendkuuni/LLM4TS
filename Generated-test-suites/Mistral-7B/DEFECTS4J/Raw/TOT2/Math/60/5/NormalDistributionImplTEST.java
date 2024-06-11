import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_mean0_stdDev1() throws MathException {
        NormalDistribution distribution = new NormalDistributionImpl();

        // Check mean and standard deviation
        assertEquals("Mean should be equal to 0.0", 0.0, distribution.getMean(), 0.001);
        assertEquals("Standard deviation should be equal to 1.0", 1.0, distribution.getStandardDeviation(), 0.001);

        // Check density at some specific points
        assertEquals("Density at mean should be 0.39894228", 0.39894228, distribution.density(0.0), 0.001);
        assertEquals("Density at mean + 1.5 * standard deviation should be very small", 0.0005, distribution.density(1.5), 0.001);
        assertEquals("Density at mean - 1.5 * standard deviation should be very small", 0.0005, distribution.density(-1.5), 0.001);

        // Check cumulative probability at some specific points
        assertEquals("Probability of X < 0 should be 0.5", 0.5, distribution.cumulativeProbability(0.0), 0.01);
        assertEquals("Probability of X < 2 should be 0.9772", 0.9772, distribution.cumulativeProbability(2.0), 0.01);
        assertEquals("Probability of X < -2 should be 0.1587", 0.1587, distribution.cumulativeProbability(-2.0), 0.01);

        // Check inverse cumulative probability at some specific probabilities
        assertEquals("Inverse cumulative probability of 0.25 should be -0.6794661", -0.6794661, distribution.inverseCumulativeProbability(0.25), 0.01);
        assertEquals("Inverse cumulative probability of 0.5 should be 0.0", 0.0, distribution.inverseCumulativeProbability(0.5), 0.01);
        assertEquals("Inverse cumulative probability of 0.75 should be 0.6794661", 0.6794661, distribution.inverseCumulativeProbability(0.75), 0.01);

        // Check sample method
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue("All samples should be valid normal distribution values", sample > -5.0 && sample < 5.0);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_invalidStandardDeviation() {
        new NormalDistributionImpl(-1.0, 1.0);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_inverseCumulativeProbability_invalidProbability() throws MathException {
        NormalDistributionImpl distribution = new NormalDistributionImpl(0.0, 1.0);
        distribution.inverseCumulativeProbability(-1.0);
    }
}