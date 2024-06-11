package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test for the constructor with mean vector and covariance matrix.
     */
    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = {0.0, 1.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            Assert.assertArrayEquals(means, dist.getMeans(), 0.001);
            RealMatrix covMat = dist.getCovariances();
            Assert.assertEquals(covariances[0].length, covMat.getRowDimension());
            Assert.assertEquals(covariances[0].length, covMat.getColumnDimension());
            Assert.assertArrayEquals(covariances, covMat.getData(), 0.001);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for the density method with a valid input.
     */
    @Test
    public void testDensity() {
        double[] means = {0.0, 1.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] testValues = {0.5, 1.5};
            double density = dist.density(testValues);
            double expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(dist.getCovariances().determinant(), -0.5) * dist.getExponentTerm(testValues);
            Assert.assertEquals(expectedDensity, density, 0.001);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for the density method with an invalid input.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() {
        double[] means = {0.0, 1.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] testValues = {0.5, 1.5, 2.5};
            dist.density(testValues);
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    /**
     * Test for the sample method.
     */
    @Test
    public void testSample() {
        double[] means = {0.0, 1.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] randomValues = dist.sample();
            for (int i = 0; i < randomValues.length; i++) {
                Assert.assertNotNaN(randomValues[i]);
            }
            double[] meansCopy = means.clone();
            for (int i = 0; i < randomValues.length; i++) {
                Assert.assertEquals(meansCopy[i], randomValues[i], 0.01);
            }
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for the getExponentTerm method.
     */
    @Test
    public void testGetExponentTerm() {
        double[] means = {0.0, 1.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] testValues = {0.5, 1.5};
            double exponentTerm = dist.getExponentTerm(testValues);
            double expectedExponentTerm = FastMath.exp(-0.5 * (dist.getCovariances().inverse().preMultiply(testValues).dotProduct(testValues)));
            Assert.assertEquals(expectedExponentTerm, exponentTerm, 0.001);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for the getStandardDeviations method.
     */
    @Test
    public void testGetStandardDeviations() {
        double[] means = {0.0, 1.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] standardDeviations = dist.getStandardDeviations();
            Assert.assertArrayEquals(new double[]{1.0, 0.5}, standardDeviations, 0.001);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for the getCovariances method.
     */
    @Test
    public void testGetCovariances() {
        double[] means = {0.0, 1.0};
        double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            RealMatrix covMatrix = dist.getCovariances();
            double[][] covMatrixData = covMatrix.getData();
            Assert.assertArrayEquals(covariances, covMatrixData, 0.001);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for the getMeans method.
     */
    @Test
    public void testGetMeans() {
        double[] means = {0.0, 1.0};
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, new double[means.length][means.length]);
            double[] meansCopy = means.clone();
            double[] distMeans = dist.getMeans();
            Assert.assertArrayEquals(meansCopy, distMeans, 0.001);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}