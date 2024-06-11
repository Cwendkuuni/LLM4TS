import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    private HypergeometricDistribution distribution;

    @Before
    public void setup() {
        distribution = new HypergeometricDistribution(10, 5, 5);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(10, distribution.getPopulationSize());
        Assert.assertEquals(5, distribution.getNumberOfSuccesses());
        Assert.assertEquals(5, distribution.getSampleSize());
    }

    @Test
    public void testGetDomain() {
        int[] domain = distribution.getDomain();
        Assert.assertEquals(5, domain[0]);
        Assert.assertEquals(5, domain[1]);
    }

    @Test
    public void testCumulativeProbability() {
        for (int i = 0; i <= 5; i++) {
            double probability = distribution.cumulativeProbability(i);
            double expected = SaddlePointExpansion.logBinomialProbability(i, 5, 5.0 / 10.0, 5.0 / 10.0);
            double error = Math.abs(probability - expected);
            Assert.assertEquals(expected, probability, 1e-9);
        }
    }

    @Test
    public void testProbability() {
        for (int i = 0; i <= 5; i++) {
            double probability = distribution.probability(i);
            double expected = SaddlePointExpansion.logBinomialProbability(i, 5, 5.0 / 10.0, 5.0 / 10.0);
            double error = Math.abs(probability - expected);
            Assert.assertEquals(expected, probability, 1e-9);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        for (int i = 0; i <= 5; i++) {
            double probability = distribution.upperCumulativeProbability(i);
            double expected = 1.0 - distribution.cumulativeProbability(i);
            Assert.assertEquals(expected, probability, 1e-9);
        }
    }

    @Test
    public void testNumericalMean() {
        double mean = distribution.getNumericalMean();
        double expected = 5.0 * 5.0 / 10.0;
        Assert.assertEquals(expected, mean, 1e-9);
    }

    @Test
    public void testNumericalVariance() {
        double variance = distribution.getNumericalVariance();
        double expected = ((5.0 * 5.0 * (10.0 - 5.0) * (10.0 - 5.0)) / (100.0 * 100.0 * (10.0 - 1.0)));
        Assert.assertEquals(expected, variance, 1e-9);
    }

    @Test
    public void testStandardDeviation() {
        StandardDeviation std = new StandardDeviation();
        double standardDeviation = std.evaluate(new double[] {distribution.getNumericalMean()});
        double expected = Math.sqrt(distribution.getNumericalVariance());
        Assert.assertEquals(expected, standardDeviation, 1e-9);
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroPopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 6, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

}