import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidParameter() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidParameter() {
        new PoissonDistributionImpl(-1.5);
    }

    @Test
    public void testProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double probability = poissonDistribution.probability(0);
        assertEquals(0.2704859831086532E-07, probability, 1E-11);
    }

    @Test
    public void testProbabilityPositive() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double probability = poissonDistribution.probability(1);
        assertEquals(0.3679781640625451E-01, probability, 1E-11);
    }

    @Test
    public void testProbabilityNegativeAndMaxValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double probability = poissonDistribution.probability(-1);
        double probabilityMaxValue = poissonDistribution.probability(Integer.MAX_VALUE);
        assertEquals(0.0, probability, 1E-11);
        assertEquals(0.0, probabilityMaxValue, 1E-11);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
        assertEquals(0.5403023058681185E-01, cumulativeProbability, 1E-11);
    }

    @Test
    public void testCumulativeProbabilityMaxValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(Integer.MAX_VALUE);
        assertEquals(0.9993383152382384, cumulativeProbability, 1E-11);
    }

    @Test
    public void testCumulativeProbabilityIntermediate() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1);
        assertEquals(0.7182818284590453, cumulativeProbability, 1E-11);
    }

    @Test
    public void testNormalApproximateProbabilityZero() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(0);
        assertEquals(0.04737053324572484, normalApproximateProbability, 1E-11);
    }

    @Test
    public void testNormalApproximateProbabilityMaxValue() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(Integer.MAX_VALUE);
        assertEquals(0.0, normalApproximateProbability, 1E-11);
    }

    @Test
    public void testSample() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        int expectedMinValue = 0;
        int expectedMaxValue = 1000 * 2.5;
        assertTrue(expectedMinValue <= sample && sample <= expectedMaxValue);
    }

    @Test
    public void testInverseCumulativeProbability() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1.0);
        double inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.25);
        int expectedValue = 0;
        int actualValue = (int) inverseCumulativeProbability;
        assertEquals(expectedValue, actualValue);

        inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.75);
        expectedValue = 1;
        actualValue = (int) inverseCumulativeProbability;
        assertEquals(expectedValue, actualValue);
    }
}