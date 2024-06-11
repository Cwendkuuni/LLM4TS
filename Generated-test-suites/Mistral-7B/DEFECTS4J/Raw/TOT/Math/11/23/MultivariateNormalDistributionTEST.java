package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-10;

    private double[] means;
    private double[][] covariances;
    private MultivariateNormalDistribution distribution;
    private RandomGenerator random;

    @Before
    public void setUp() {
        means = new double[]{1.0, 2.0};
        covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        random = new Well19937c();
        try {
            distribution = new MultivariateNormalDistribution(random, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Test for constructor with wrong dimension in means array.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWrongMeansDim() {
        double[] wrongMeans = new double[]{1.0, 2.0, 3.0};
        double[][] correctCovariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        new MultivariateNormalDistribution(random, wrongMeans, correctCovariances);
    }

    /**
     * Test for constructor with wrong dimension in covariances array.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWrongCovariancesDim() {
        double[] means = new double[]{1.0, 2.0};
        double[][] wrongCovariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(random, means, wrongCovariances);
    }

    /**
     * Test for constructor with incorrect dimensions in means and covariances arrays.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorIncorrectDimensions() {
        double[] means = new double[]{1.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        new MultivariateNormalDistribution(random, means, covariances);
    }

    /**
     * Test for density method.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[]{1.5, 3.5};
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(covarianceMatrixDeterminant, -0.5) * getExponentTerm(testValues);
        double actualDensity = distribution.density(testValues);
        assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    /**
     * Test for sample method.
     */
    @Test
    public void testSample() {
        double[] sample = distribution.sample();

        for (int i = 0; i < distribution.getDimension(); i++) {
            assert(sample[i] > -100 && sample[i] < 100);
        }
    }

    /**
     * Test for getMeans method.
     */
    @Test
    public void testGetMeans() {
        double[] expectedMeans = new double[]{1.0, 2.0};
        assertArrayEquals(expectedMeans, distribution.getMeans());
    }

    /**
     * Test for getCovariances method.
     */
    @Test
    public void testGetCovariances() {
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(covariances);
        RealMatrix actualCovariances = distribution.getCovariances();
        assertArrayEquals(expectedCovariances.getData(), actualCovariances.getData());
    }

    /**
     * Test for getStandardDeviations method.
     */
    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = new double[]{1.0, 1.4142135625};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations());
    }

    /**
     * Helper method to calculate exponent term for testing.
     *
     * @param values Values at which to compute density.
     * @return the multiplication factor of density calculations.
     */
    private double getExponentTerm(final double[] values) {
        final double[] centered = new double[values.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = values[i] - means[i];
        }
        final double[] preMultiplied = covarianceMatrixInverse.operate(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.exp(-0.5 * sum);
    }
}