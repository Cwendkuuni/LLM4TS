package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealArray2DSampleGenerator;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    /** Vector of means. */
    private static final double[] MEANS = {1.0, 2.0};

    /** Covariance matrix. */
    private static final double[][] COVARIANCES = {
        {1.0, 0.5},
        {0.5, 2.5}
    };

    /** Number of dimensions (p). */
    private static final int DIM = MEANS.length;

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testValidConstructor() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), MEANS, COVARIANCES);
            assertEquals(DIM, dist.getDimension());
            assertArrayEquals(MEANS, dist.getMeans());
            RealMatrix covariance = dist.getCovariances();
            RealMatrix expectedCovariance = new Array2DRowRealMatrix(COVARIANCES);
            assertArrayEquals(expectedCovariance.getData(), covariance.getData());
            assertEquals(Precision.SAFE_EPSILON, dist.getCovarianceMatrixDeterminant(), covariance.determinant(), 0.0);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test constructor with invalid length of means.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorLengthMeans() {
        new MultivariateNormalDistribution(new Well19937c(), new double[]{}, COVARIANCES);
    }

    /**
     * Test constructor with invalid length of covariances.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorLengthCovariances() {
        new MultivariateNormalDistribution(new Well19937c(), MEANS, new double[][]{});
    }

    /**
     * Test constructor with inconsistent length of means and covariances.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstructorInconsistentLength() {
        new MultivariateNormalDistribution(new Well19937c(), MEANS, new double[][]{COVARIANCES[0], new double[1]});
    }

    /**
     * Test constructor with non positive definite covariance matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidConstructorNonPositiveDefinite() {
        new MultivariateNormalDistribution(new Well19937c(), MEANS, new double[][]{-COVARIANCES});
    }

    /**
     * Test constructor with singular covariance matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testInvalidConstructorSingular() {
        new MultivariateNormalDistribution(new Well19937c(), MEANS, new double[][]{{-1.0, 0.0}, {0.0, 0.0}});
    }

    /**
     * Test density calculation.
     */
    @Test
    public void testDensity() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), MEANS, COVARIANCES);
            double[] testValues = new double[]{1.5, 2.5};
            double density = dist.density(testValues);
            double exponentTerm = dist.getExponentTerm(testValues);
            double expectedDensity = FastMath.pow(2 * FastMath.PI, -DIM / 2) * FastMath.pow(dist.getCovarianceMatrixDeterminant(), -0.5) * exponentTerm;
            assertEquals(expectedDensity, density, 1e-15);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test density calculation with invalid number of dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidDimension() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), MEANS, COVARIANCES);
        double[] testValues = new double[]{1.5, 2.5, 3.0};
        dist.density(testValues);
    }

    /**
     * Test getStandardDeviations method.
     */
    @Test
    public void testGetStandardDeviations() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), MEANS, COVARIANCES);
            double[] result = dist.getStandardDeviations();
            double[] expectedStdDeviations = new double[]{FastMath.sqrt(COVARIANCES[0][0]), FastMath.sqrt(COVARIANCES[1][1])};
            assertArrayEquals(expectedStdDeviations, result);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test getStandardDeviations method with invalid number of dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStandardDeviationsInvalidDimension() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), MEANS, COVARIANCES);
        dist.getStandardDeviations(1);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), MEANS, COVARIANCES);
            double[] sample = dist.sample();
            assertArrayEquals(DIM, sample.length);
            for (int i = 0; i < DIM; i++) {
                assertEquals(Precision.SAFE_EPSILON, sample[i], MEANS[i]);
                assertEquals(Precision.SAFE_EPSILON, sample[i], dist.sample()[i]);
            }
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test sample method with invalid number of dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSampleInvalidDimension() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), MEANS, COVARIANCES);
        dist.sample(2);
    }

    /**
     * Test sample method with large number of samples.
     */
    @Test
    public void testSampleLargeNumberOfSamples() {
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), MEANS, COVARIANCES);
            int numberOfSamples = 10000;
            double[][] samples = new double[numberOfSamples][DIM];
            for (int i = 0; i < numberOfSamples; i++) {
                samples[i] = dist.sample();
            }
            RealArray2DSampleGenerator generator = new RealArray2DSampleGenerator(samples, numberOfSamples);
            RealMatrix covariance = dist.getCovariances();
            assertArrayEquals(covariance.getData(), generator.getSampleCovariance().getData());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

}