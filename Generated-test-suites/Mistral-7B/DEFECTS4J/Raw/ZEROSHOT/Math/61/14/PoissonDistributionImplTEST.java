import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionImplWithDefaultConstructor() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl();
        assertEquals(PoissonDistribution.DEFAULT_MEAN, poissonDistribution.getMean(), 0.0);
    }

    @Test
    public void testPoissonDistributionImplWithCustomConstructor() {
        double mean = 3.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, poissonDistribution.getMean(), 0.0);
    }

    @Test
    public void testProbability() {
        double mean = 5.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 10; i++) {
            double probability = poissonDistribution.probability(i);
            double expectedProbability = Gamma.regularizedGammaP(i + 1, mean);
            assertEquals(expectedProbability, probability, 1e-9);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        double mean = 5.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(i + 1, mean);
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-9);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        double mean = 5.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(i);
            double expectedNormalApproximateProbability = 0.5 * FastMath.erf(Math.sqrt(mean) / FastMath.SQRT2)
                    + 0.5 * FastMath.erfc(Math.sqrt(mean) / FastMath.SQRT2) * FastMath.exp(-mean + i - 0.5);
            assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 1e-6);
        }
    }

    @Test
    public void testSample() throws Exception {
        double mean = 5.0;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i < 1000; i++) {
            int sample = poissonDistribution.sample();
            int expectedSample = (int) FastMath.poisson(mean);
            assertEquals(expectedSample, sample);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(-1.0);
    }
}