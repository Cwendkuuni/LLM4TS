import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -2.5;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbabilityMethod() {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int x = 0; x <= 5; x++) {
            double probability = poissonDistribution.probability(x);
            double expectedProbability = Gamma.regularizedGammaP(x + 1, mean);
            assertEquals(expectedProbability, probability, 0.001);
        }
    }

    @Test
    public void testCumulativeProbabilityMethod() throws Exception {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int x = 0; x <= 5; x++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(x);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(x + 1, mean);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testNormalApproximateProbabilityMethod() throws Exception {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int x = 0; x <= 5; x++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
            double expectedNormalApproximateProbability = Gamma.regularizedGammaQ(x + 0.5, mean);
            assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 0.001);
        }
    }

    @Test
    public void testSampleMethod() throws Exception {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i < 100; i++) {
            int sampledValue = poissonDistribution.sample();
            int expectedValue = FastMath.min(random.nextInt(1000 * mean), Integer.MAX_VALUE);
            assertEquals(expectedValue, sampledValue);
        }
    }
}