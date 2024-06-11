import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(1.5);
        Assert.assertEquals(1.5, distribution.getMean(), 0.01);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        new PoissonDistributionImpl(-2.5);
    }

    @Test
    public void testProbabilityMethod() {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.5);
        for (int i = 0; i <= 10; i++) {
            double probability = distribution.probability(i);
            double expectedProbability = Gamma.regularizedGammaP(i + 1, 2.5);
            Assert.assertEquals(expectedProbability, probability, 0.001);
        }
    }

    @Test
    public void testCumulativeProbabilityMethod() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(3.0);
        for (int i = 0; i <= 10; i++) {
            double cumulativeProbability = distribution.cumulativeProbability(i);
            double expectedCumulativeProbability = Gamma.regularizedGammaQ(i + 1, 3.0);
            Assert.assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testNormalApproximateProbabilityMethod() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(5.5);
        for (int i = 0; i <= 10; i++) {
            double normalApproximateProbability = distribution.normalApproximateProbability(i);
            double probability = distribution.probability(i);
            double epsilon = 0.01;
            Assert.assertTrue(Precision.isClose(probability, normalApproximateProbability, epsilon));
        }
    }

    @Test
    public void testSampleMethod() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(4.5);
        int sample = distribution.sample();
        int expectedSample = RandomData.nextInt(RandomData.getDefaultRandom(), 10 * 4.5);
        Assert.assertEquals(expectedSample, sample);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityWithNegativeX() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        distribution.cumulativeProbability(-1);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCumulativeProbabilityWithMaxInteger() throws MathException {
        PoissonDistributionImpl distribution = new PoissonDistributionImpl(2.0);
        distribution.cumulativeProbability(Integer.MAX_VALUE);
    }
}