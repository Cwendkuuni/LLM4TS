import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testPoissonDistributionWithDefaultConstructor() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl();
        Assert.assertEquals(PoissonDistribution.class, poissonDistribution.getClass());
        Assert.assertNotNull(poissonDistribution.getMean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPoissonDistributionWithNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbability() {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 5; i++) {
            double probability = poissonDistribution.probability(i);
            double expectedProbability = FastMath.exp(-2.5) * Math.pow(2.5, i) / MathUtils.factorial(i);
            Assert.assertEquals(expectedProbability, probability, 0.0001);
        }
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 5; i++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaP(i + 1, 2.5);
            Assert.assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.0001);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 5; i++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(i);
            double expectedNormalApproximateProbability = normal.cumulativeProbability(i + 0.5);
            Assert.assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 0.0001);
        }
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        int sample = poissonDistribution.sample();
        int expectedSample = random.nextInt(1000 * 2.5) + 1;
        Assert.assertEquals(expectedSample, sample);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(2.5);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            int inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(p);
            int expectedInverseCumulativeProbability = poissonDistribution.getDomainLowerBound(p) + poissonDistribution.sampleIntBound(p);
            Assert.assertEquals(expectedInverseCumulativeProbability, inverseCumulativeProbability);
        }
    }
}