import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HypergeometricDistributionTest {

    private static final double EPSILON = 1e-9;
    private HypergeometricDistribution distribution;
    private int populationSize;
    private int numberOfSuccesses;
    private int sampleSize;

    @Before
    public void setUp() {
        populationSize = 10;
        numberOfSuccesses = 5;
        sampleSize = 3;
        distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorThrowsNotPositiveException() {
        distribution = new HypergeometricDistribution(-1, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveException() {
        distribution = new HypergeometricDistribution(populationSize, -1, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorThrowsNumberIsTooLargeException() {
        distribution = new HypergeometricDistribution(populationSize, 6, sampleSize);
    }

    @Test
    public void testCumulativeProbability() {
        Assert.assertEquals(0.42857142857142855, distribution.cumulativeProbability(1), EPSILON);
        Assert.assertEquals(0.7714285714285715, distribution.cumulativeProbability(2), EPSILON);
        Assert.assertEquals(0.9428571428571428, distribution.cumulativeProbability(3), EPSILON);
        Assert.assertEquals(1.0, distribution.cumulativeProbability(4), EPSILON);
    }

    @Test
    public void testProbability() {
        Assert.assertEquals(0.42857142857142855, distribution.probability(1), EPSILON);
        Assert.assertEquals(0.34285714285714286, distribution.probability(2), EPSILON);
        Assert.assertEquals(0.17142857142857143, distribution.probability(3), EPSILON);
        Assert.assertEquals(0.05714285714285714, distribution.probability(4), EPSILON);
    }

    @Test
    public void testUpperCumulativeProbability() {
        Assert.assertEquals(1.0, distribution.upperCumulativeProbability(1), EPSILON);
        Assert.assertEquals(0.5714285714285714, distribution.upperCumulativeProbability(2), EPSILON);
        Assert.assertEquals(0.22857142857142856, distribution.upperCumulativeProbability(3), EPSILON);
        Assert.assertEquals(0.05714285714285714, distribution.upperCumulativeProbability(4), EPSILON);
    }

    @Test
    public void testGetNumericalMean() {
        Assert.assertEquals(1.5, distribution.getNumericalMean(), EPSILON);
    }

    @Test
    public void testGetNumericalVariance() {
        Assert.assertEquals(0.6428571428571429, distribution.getNumericalVariance(), EPSILON);
    }

    @Test
    public void testGetSupportLowerBound() {
        Assert.assertEquals(3, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        Assert.assertEquals(3, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        Assert.assertTrue(distribution.isSupportConnected());
    }

    @After
    public void tearDown() {
        distribution = null;
    }
}