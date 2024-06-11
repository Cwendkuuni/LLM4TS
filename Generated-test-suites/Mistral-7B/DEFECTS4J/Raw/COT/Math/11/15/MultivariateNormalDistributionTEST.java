package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.LinearAlgebra;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test constructor with valid input.
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testValidConstruction() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {1.5, 0.5},
            {0.5, 2.5}
        };

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans(), 0.0);
        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(means.length, covarianceMatrix.getRowDimension());
        assertEquals(means.length, covarianceMatrix.getColumnDimension());
        LinearAlgebra.assertEquals(covariances, covarianceMatrix.getData(), 0.0);
    }

    /**
     * Test constructor with invalid input (dimension mismatch).
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testInvalidConstruction() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {1.5, 0.5},
            {0.5}
        };

        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test density calculation for valid input.
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testDensity() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {1.5, 0.5},
            {0.5, 2.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        double[] testVector = new double[] {2.5, 3.5};
        double expectedResult = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(1.0, -1) * FastMath.pow(2.5, -1)
                * FastMath.pow(2.5, -1) * FastMath.exp(-((2.5 - 1.0) * 1.0 / 2) * ((2.5 - 1.0) * 1.0 / 2)
                        - ((3.5 - 2.0) * 2.5 / 2) * ((3.5 - 2.0) * 2.5 / 2));
        double result = dist.density(testVector);
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test density calculation for invalid input (dimension mismatch).
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {1.5, 0.5},
            {0.5, 2.5},
            {1.5, 0.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testVector = new double[] {2.5, 3.5, 4.5};
        dist.density(testVector);
    }

    /**
     * Test sample calculation for valid input.
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testSample() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {1.5, 0.5},
            {0.5, 2.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RandomGenerator rng = new Well19937c();
        double[] result = dist.sample();
        double[] expectedMeans = dist.getMeans();
        double[] expectedCovariances = dist.getCovariances().getData();

        for (int i = 0; i < result.length; i++) {
            // Check if the mean is reached as expected.
            if (i < result.length && i < expectedMeans.length) {
                assertEquals(expectedMeans[i], result[i], 0.001);
            }
            // Check if the distribution is normalized.
            if (i == result.length) {
                double[] sample = new double[dist.getDimension()];
                for (int j = 0; j < dist.getDimension(); j++) {
                    sample[j] = result[j];
                }
                double density = dist.density(sample);
                double epsilon = 1e-6;
                // Check if the absolute value of the density is close to 1.
                double normalizationCheck = 1.0 - Math.abs(density);
                assertTrue(normalizationCheck <= epsilon);
            }

            // Check if the covariance matrix is respected.
            int index1 = i;
            int index2 = i + 1;
            if (i < dist.getDimension() - 1) {
                double covariance = expectedCovariances[index1][index2];
                double covarianceResult = LinearAlgebra.dotProduct(dist.getCovariances().operate(new double[]{result[index1], result[index2]}),
                        new double[]{result[index1], result[index2]});
                assertEquals(covariance, covarianceResult, 0.001);
            }
        }
    }

    /**
     * Test sample calculation for invalid input (random generator).
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSampleInvalidRandomGenerator() throws Exception {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[] {1.0}, new double[][] {{1.5}});
        dist.sample((Object) null);
    }

    /**
     * Test sample calculation for invalid input (dimension mismatch).
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test(expected = DimensionMismatchException.class)
    public void testSampleInvalidInput() throws Exception {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[] {1.0}, new double[][] {{1.5}, {1.5, 2.5}});
        dist.sample();
    }

    /**
     * Test getStandardDeviations calculation for valid input.
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testGetStandardDeviations() throws Exception {
        double[] means = new double[] {1.0, 2.0};
        double[][] covariances = new double[][] {
            {1.5, 0.5},
            {0.5, 2.5}
        };
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] expectedStandardDeviations = new double[] {Math.sqrt(1.5), Math.sqrt(2.5)};
        double[] result = dist.getStandardDeviations();
        assertArrayEquals(expectedStandardDeviations, result, 0.0);
    }
}