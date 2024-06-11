import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorPopulationSizeNotPositive() {
        int populationSize = -1;
        int numberOfSuccesses = 10;
        int sampleSize = 5;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNumberOfSuccessesNotPositive() {
        int populationSize = 100;
        int numberOfSuccesses = 0;
        int sampleSize = 5;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorSampleSizeNotPositive() {
        int populationSize = 100;
        int numberOfSuccesses = 10;
        int sampleSize = -1;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 110;
        int sampleSize = 50;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 110;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 60;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(1, distr.cumulativeProbability(100), 0);
        Assert.assertEquals(0.9999925479799031, distr.cumulativeProbability(99), 1e-15);
        Assert.assertEquals(0.00141355153607, distr.cumulativeProbability(0), 1e-15);
    }

    @Test
    public void testProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 60;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(0.8671990656, distr.probability(50), 1e-10);
        Assert.assertEquals(0.8646034817, distr.probability(49), 1e-10);
        Assert.assertEquals(0.7129936184, distr.probability(40), 1e-10);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 60;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(0, distr.upperCumulativeProbability(100), 0);
        Assert.assertEquals(7.4520200969e-08, distr.upperCumulativeProbability(99), 1e-15);
        Assert.assertEquals(0.998023429909, distr.upperCumulativeProbability(0), 1e-10);
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 60;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(30.0, distr.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 60;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(11.2612612613, distr.getNumericalVariance(), 1e-10);
    }

    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 60;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(40, distr.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 60;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(50, distr.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 100;
        int numberOfSuccesses = 50;
        int sampleSize = 60;
        HypergeometricDistribution distr = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertTrue(distr.isSupportConnected());
    }
}