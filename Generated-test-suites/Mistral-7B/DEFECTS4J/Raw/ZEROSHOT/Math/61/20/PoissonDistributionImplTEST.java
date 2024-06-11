import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImplConstructor() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionImplNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbabilityMethod() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = poissonDistribution.probability(i);
            double expectedProbability = Gamma.regularizedGammaP(i + 1, 2.5);
            assertEquals(expectedProbability, probability, 0.001);
        }
    }

    @Test
    public void testCumulativeProbabilityMethod() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(i + 1, 2.5);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testNormalApproximateProbabilityMethod() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(i);
            double probability = poissonDistribution.probability(i);
            double expectedProbability = Gamma.regularizedGammaP(i + 1, 2.5);
            assertEquals(expectedProbability, probability, 0.001);
            assertEquals(expectedProbability, normalApproximateProbability, 0.001);
        }
    }

    @Test
    public void testSampleMethod() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i < 10; i++) {
            int sampledValue = poissonDistribution.sample();
            int expectedValue = poissonDistribution.inverseCumulativeProbability(Math.random()).intValue();
            assertEquals(expectedValue, sampledValue);
        }
    }

    @Test
    public void testInverseCumulativeProbabilityMethod() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = poissonDistribution.cumulativeProbability(i);
            int inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(probability).intValue();
            assertEquals(i, inverseCumulativeProbability);
        }
    }
}