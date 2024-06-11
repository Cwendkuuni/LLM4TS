import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);
        Assert.assertEquals(mean, poissonDistribution.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -1.5;
        new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbability() {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int x = 0; x <= 6; x++) {
            double probability = poissonDistribution.probability(x);
            double expectedProbability = FastMath.exp(-mean) * FastMath.pow(mean, x) / FastMath.factorial(x);
            Assert.assertEquals(expectedProbability, probability, 1e-5);
        }
    }

    @Test
    public void testCumulativeProbabilityWithSmallMean() throws MathException {
        double mean = 0.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int x = 0; x <= 5; x++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(x);
            double expectedCumulativeProbability = Gamma.regularizedGammaP(x + 1, mean);
            Assert.assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-5);
        }
    }

    @Test
    public void testCumulativeProbabilityWithLargeMean() throws MathException {
        double mean = 100.0;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int x = 0; x <= 100; x++) {
            double cumulativeProbability = poissonDistribution.cumulativeProbability(x);
            double expectedCumulativeProbability = Gamma.regularizedGammaP(x + 1, mean);
            Assert.assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-5);
        }
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int x = 0; x <= 6; x++) {
            double normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
            double probability = poissonDistribution.probability(x);
            Assert.assertEquals(probability, normalApproximateProbability, 1e-5);
        }
    }

    @Test
    public void testSample() throws MathException {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int i = 0; i < 1000; i++) {
            int sample = poissonDistribution.sample();
            Assert.assertEquals(true, poissonDistribution.isProbable(sample));
            Assert.assertEquals(mean, poissonDistribution.getMean(), 0.1);
            Assert.assertEquals(sample, poissonDistribution.inverseCumulativeProbability(FastMath.random()), 1);
        }
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        for (int x = 0; x <= 6; x++) {
            int inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(FastMath.random());
            Assert.assertEquals(x, inverseCumulativeProbability);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithInvalidInput() throws MathException {
        double mean = 2.5;
        PoissonDistribution poissonDistribution = new PoissonDistributionImpl(mean);

        poissonDistribution.cumulativeProbability(-1);
    }

}