import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Assert;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithPositiveMean() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        Assert.assertEquals(mean, poissonDistribution.getMean());
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructorWithNegativeMean() {
        double mean = -2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
    }

    @Test
    public void testProbabilityMethodWithZero() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double probability = poissonDistribution.probability(0);
        Assert.assertEquals(FastMath.exp(-mean), probability, 1e-6);
    }

    @Test
    public void testProbabilityMethodWithPositiveValue() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int x = 3;
        double probability = poissonDistribution.probability(x);
        Assert.assertEquals(FastMath.exp(-mean) * Math.pow(mean, x) / Math.sqrt(Math.PI * x), probability, 1e-6);
    }

    @Test
    public void testProbabilityMethodWithMaxIntegerValue() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int x = Integer.MAX_VALUE;
        double probability = poissonDistribution.probability(x);
        Assert.assertEquals(0, probability, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityMethodWithNegativeValue() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int x = -1;
        poissonDistribution.cumulativeProbability(x);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMethodWithMaxIntegerValue() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int x = Integer.MAX_VALUE;
        poissonDistribution.cumulativeProbability(x);
    }

    @Test
    public void testCumulativeProbabilityMethodWithValidValue() throws MathException {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int x = 3;
        double cumulativeProbability = poissonDistribution.cumulativeProbability(x);
        double expectedCumulativeProbability = Gamma.regularizedGammaP(x + 1, mean);
        Assert.assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-6);
    }

    @Test
    public void testNormalApproximateProbabilityMethodWithZero() throws MathException {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int x = 0;
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
        Assert.assertEquals(FastMath.exp(-mean) / Math.sqrt(Math.PI), normalApproximateProbability, 1e-6);
    }

    @Test
    public void testNormalApproximateProbabilityMethodWithValidValue() throws MathException {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int x = 3;
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
        double expectedNormalApproximateProbability = FastMath.exp(-mean + (x - mean) / 2) / Math.sqrt(Math.PI * (x + 0.5));
        Assert.assertEquals(expectedNormalApproximateProbability, normalApproximateProbability, 1e-6);
    }

    @Test
    public void testSampleMethod() throws MathException {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        int sample = poissonDistribution.sample();
        int expectedSample = FastMath.min(randomData.nextPoisson(mean), Integer.MAX_VALUE);
        Assert.assertEquals(expectedSample, sample);
    }

    @Test
    public void testGetDomainLowerBoundMethodWithZeroProbability() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double p = 0.0;
        int domainLowerBound = poissonDistribution.getDomainLowerBound(p);
        Assert.assertEquals(0, domainLowerBound);
    }

    @Test
    public void testGetDomainUpperBoundMethodWithOneProbability() {
        double mean = 2.5;
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(mean);
        double p = 1.0;
        int domainUpperBound = poissonDistribution.getDomainUpperBound(p);
        Assert.assertEquals(Integer.MAX_VALUE, domainUpperBound);
    }
}