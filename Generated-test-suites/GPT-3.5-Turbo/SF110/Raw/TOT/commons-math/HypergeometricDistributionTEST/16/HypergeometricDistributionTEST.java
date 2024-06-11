import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructor() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            Assert.assertEquals(populationSize, distribution.getPopulationSize());
            Assert.assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
            Assert.assertEquals(sampleSize, distribution.getSampleSize());
        } catch (Exception e) {
            Assert.fail("Constructor threw an exception");
        }
    }

    @Test
    public void testConstructorWithRandomGenerator() {
        RandomGenerator rng = mock(RandomGenerator.class);

        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(rng, populationSize, numberOfSuccesses, sampleSize);
            Assert.assertEquals(populationSize, distribution.getPopulationSize());
            Assert.assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
            Assert.assertEquals(sampleSize, distribution.getSampleSize());
        } catch (Exception e) {
            Assert.fail("Constructor threw an exception");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() throws NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException {
        int populationSize = -10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() throws NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = -5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() throws NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = -3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() throws NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 15;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() throws NotStrictlyPositiveException, NotPositiveException, NumberIsTooLargeException {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 15;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0001);
        Assert.assertEquals(0.0, distribution.cumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.461538, distribution.cumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.923076, distribution.cumulativeProbability(2), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(3), 0.0001);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(4), 0.0001);
    }

    @Test
    public void testGetDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(2, domain.length);
        Assert.assertEquals(2, domain[0]);
        Assert.assertEquals(3, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int lowerDomain = distribution.getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        Assert.assertEquals(2, lowerDomain);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(populationSize, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(sampleSize, distribution.getSampleSize());
    }

    @Test
    public void testGetUpperDomain() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int upperDomain = distribution.getUpperDomain(numberOfSuccesses, sampleSize);
        Assert.assertEquals(3, upperDomain);
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(0.0, distribution.probability(-1), 0.0001);
        Assert.assertEquals(0.0, distribution.probability(0), 0.0001);
        Assert.assertEquals(0.065934, distribution.probability(1), 0.0001);
        Assert.assertEquals(0.263736, distribution.probability(2), 0.0001);
        Assert.assertEquals(0.4175827, distribution.probability(3), 0.0001);
        Assert.assertEquals(0.0, distribution.probability(4), 0.0001);
        Assert.assertEquals(0.0, distribution.probability(5), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(-1), 0.0001);
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(0), 0.0001);
        Assert.assertEquals(0.538461, distribution.upperCumulativeProbability(1), 0.0001);
        Assert.assertEquals(0.076923, distribution.upperCumulativeProbability(2), 0.0001);
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(3), 0.0001);
        Assert.assertEquals(0.0, distribution.upperCumulativeProbability(4), 0.0001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double innerCumulativeProbability = distribution.innerCumulativeProbability(2, 4, -1);
        Assert.assertEquals(0.681319, innerCumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(0.642857, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double numericalVariance = distribution.calculateNumericalVariance();
        Assert.assertEquals(0.642857, numericalVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(0, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 3;

        HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        Assert.assertTrue(distribution.isSupportConnected());
    }

}