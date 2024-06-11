import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = poissonDist.probability(i);
            double expectedProbability = Math.exp(-2.5) * Math.pow(2.5, i) / Math.factorial(i);
            assertEquals("Probability for x = " + i + " is incorrect.", expectedProbability, probability, 1e-6);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDist.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(i + 1, 2.5);
            assertEquals("Cumulative Probability for x = " + i + " is incorrect.", expectedCumulativeProbability, cumulativeProbability, 1e-6);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = poissonDist.normalApproximateProbability(i);
            double expectedNormalApproximateProbability = Gamma.regularizedGammaP(i + 0.5, 2.5) / Math.sqrt(2 * Math.PI * 2.5);
            assertEquals("Normal Approximate Probability for x = " + i + " is incorrect.", expectedNormalApproximateProbability, normalApproximateProbability, 1e-6);
        }
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution poissonDist = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 100; i++) {
            int sampledValue = poissonDist.sample();
            assertTrue("Sampled value is not within the expected range.", sampledValue >= 0 && sampledValue <= 1000 * 2.5);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMean() {
        new PoissonDistributionImpl(-1.0);
    }
}