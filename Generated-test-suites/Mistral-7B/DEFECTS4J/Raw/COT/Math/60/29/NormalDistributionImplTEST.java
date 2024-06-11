import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_meanAndStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals("Mean is not set correctly", 0.0, dist.getMean(), 0.01);
        assertEquals("Standard deviation is not set correctly", 1.0, dist.getStandardDeviation(), 0.01);
    }

    @Test
    public void testNormalDistributionImpl_meanAndStandardDeviationAndAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1, 0.001);
        assertEquals("Mean is not set correctly", 0.0, dist.getMean(), 0.001);
        assertEquals("Standard deviation is not set correctly", 1.0, dist.getStandardDeviation(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_negativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test(expected = MathException.class)
    public void testNormalDistributionImpl_density() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.density(200); // Out of domain
        fail("Density should not be calculated for values out of domain");
    }

    @Test
    public void testNormalDistributionImpl_densityInDomain() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.density(0);
        assertEquals("Density is not correct for x = 0", 0.398942280401, result, 0.0001);
    }

    @Test
    public void testNormalDistributionImpl_cumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(0.5);
        assertEquals("CDF is not correct for x = mean", 0.5, result, 0.0001);

        try {
            dist.cumulativeProbability(1000); // Out of domain
            fail("CDF should not be calculated for values out of domain");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testNormalDistributionImpl_inverseCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.inverseCumulativeProbability(0);
        assertEquals("Inverse CDF is not correct for p = 0", Double.NEGATIVE_INFINITY, result, 0.0);

        result = dist.inverseCumulativeProbability(0.5);
        assertEquals("Inverse CDF is not correct for p = 0.5", 0.0, result, 0.01);

        result = dist.inverseCumulativeProbability(1);
        assertEquals("Inverse CDF is not correct for p = 1", Double.POSITIVE_INFINITY, result, 0.0);

        try {
            dist.inverseCumulativeProbability(-0.1); // Invalid probability
            fail("Inverse CDF should not be calculated for invalid probabilities");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(1.1); // Invalid probability
            fail("Inverse CDF should not be calculated for invalid probabilities");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNormalDistributionImpl_sample() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result;
        for (int i = 0; i < 10; i++) {
            result = dist.sample();
            assertTrue("Sample is not within domain", result >= -3.0 && result <= 3.0);
        }
    }
}