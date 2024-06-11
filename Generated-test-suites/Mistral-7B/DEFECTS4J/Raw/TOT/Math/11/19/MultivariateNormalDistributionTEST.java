package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-15;
    private static final double[] MEANS = {1.0, 2.0};
    private static final double[][] COVARIANCES = {
            {1.0, 0.5}, {0.5, 2.5}
    };
    private static final double[] MEANS_ERROR = {0.1, 3.0};
    private static final double[][] COVARIANCES_ERROR = {
            {-1.0, 0.5}, {0.5, 2.5}
    };
    private static final double[][] COVARIANCES_SINGULAR = {
            {1.0, 1.0}, {1.0, 1.0}
    };
    private static final double[] SAMPLE_VALUES = {1.5, 3.0};
    private MultivariateNormalDistribution dist;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        try {
            dist = new MultivariateNormalDistribution(rng, MEANS, COVARIANCES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test constructor with correct input.
     */
    @Test
    public void testConstruction() {
        Assert.assertArrayEquals(MEANS, dist.getMeans());
        RealMatrix covariance = dist.getCovariances();
        Assert.assertEquals(COVARIANCES.length, covariance.getRowDimension());
        Assert.assertEquals(COVARIANCES[0].length, covariance.getColumnDimension());
        for (int i = 0; i < covariance.getRowDimension(); i++) {
            Assert.assertEquals(covariance.getColumnDimension(), covariance.getColumnDimension());
            for (int j = 0; j < covariance.getColumnDimension(); j++) {
                Assert.assertEquals(COVARIANCES[i][j], covariance.getEntry(i, j), EPSILON);
            }
        }
    }

    /**
     * Test getMeans() and getCovariances() methods.
     */
    @Test
    public void testGetters() {
        Assert.assertArrayEquals(MEANS, dist.getMeans());
        RealMatrix covariance = dist.getCovariances();
        for (int i = 0; i < covariance.getRowDimension(); i++) {
            for (int j = 0; j < covariance.getColumnDimension(); j++) {
                Assert.assertEquals(COVARIANCES[i][j], covariance.getEntry(i, j), EPSILON);
            }
        }
    }

    /**
     * Test density() method.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[dist.getDimension()];
        double density;

        // Test with valid values
        for (int i = 0; i < testValues.length; i++) {
            testValues[i] = i + 1.0;
            density = dist.density(testValues);
            Assert.assertTrue(density > 0.0);
        }

        // Test with means
        testValues = MEANS.clone();
        density = dist.density(testValues);
        Assert.assertEquals(dist.probabilityDensityFunctionValue(testValues), density, EPSILON);

        // Test with edge values
        testValues[0] = MEANS[0] - 3.0;
        testValues[1] = MEANS[1] - 3.0;
        density = dist.density(testValues);
        Assert.assertEquals(dist.cumulativeProbabilityFunctionValue(testValues), 0.0, EPSILON);
        Assert.assertTrue(density > 0.0);

        testValues[0] = MEANS[0] + 3.0;
        testValues[1] = MEANS[1] + 3.0;
        density = dist.density(testValues);
        Assert.assertEquals(dist.cumulativeProbabilityFunctionValue(testValues), 1.0, EPSILON);
        Assert.assertTrue(density > 0.0);

        // Test with error values
        testValues = MEANS_ERROR.clone();
        density = dist.density(testValues);
        Assert.assertEquals(Double.NaN, density, EPSILON);

        testValues = new double[dist.getDimension() + 1];
        density = dist.density(testValues);
        Assert.assertEquals(Double.NaN, density, EPSILON);
    }

    /**
     * Test sample() method.
     */
    @Test
    public void testSample() {
        double[] sample;

        // Test with valid sample size
        sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(sample[i] >= -10.0 && sample[i] <= 10.0);
        }

        // Test with error sample size
        sample = dist.sample(dist.getDimension() + 1);
        Assert.assertEquals(Double.NaN, sample[dist.getDimension()].doubleValue(), EPSILON);
    }

    /**
     * Test exception handling in constructor.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() throws DimensionMismatchException {
        double[][] covariances = new double[2][3];
        new MultivariateNormalDistribution(rng, MEANS, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrix() throws SingularMatrixException {
        new MultivariateNormalDistribution(rng, MEANS, COVARIANCES_SINGULAR);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix()
            throws SingularMatrixException, NonPositiveDefiniteMatrixException {
        new MultivariateNormalDistribution(rng, MEANS, COVARIANCES_ERROR);
    }
}