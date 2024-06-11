package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionCreation() {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 2.5, 0.5},
                {0.5, 0.5, 3.5}
        };
        MultivariateNormalDistribution mvd = null;
        try {
            mvd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(means.length, mvd.getDimension());
        assertEquals(means, mvd.getMeans());
        assertEquals(covariances, mvd.getCovariances().getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionCreationWithInconsistentArraysLength() {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 2.5},
                {0.5, 0.5, 3.5}
        };
        new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionCreationWithInconsistentRowsAndColumns() {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.5, 0.5},
                {0.5, 0.5, 3.5}
        };
        new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistributionCreationWithNonPositiveDefiniteCovarianceMatrix() {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, -0.5, 0.5},
                {0.5, 2.5, 0.5},
                {0.5, 0.5, 3.5}
        };
        new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test
    public void testDensity() {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 2.5, 0.5},
                {0.5, 0.5, 3.5}
        };
        MultivariateNormalDistribution mvd = null;
        try {
            mvd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[] testValues = new double[]{1.5, 2.5, 3.5};
        double density = mvd.density(testValues);

        double expectedDensity = FastMath.pow(2 * FastMath.PI, -3.0) * FastMath.pow(mvd.covarianceMatrixDeterminant, -0.5) * getExponentTerm(testValues, mvd.means, mvd.covarianceMatrix);
        assertEquals(expectedDensity, density, 1e-15);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 2.5, 0.5},
                {0.5, 0.5, 3.5}
        };
        MultivariateNormalDistribution mvd = null;
        try {
            mvd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[] sample = mvd.sample();

        double[] meansValues = mvd.getMeans();
        RealMatrix covariancesMatrix = mvd.getCovariances();

        for (int i = 0; i < sample.length; i++) {
            assertEquals(meansValues[i], sample[i], 1e-15);
        }

        for (int i = 0; i < sample.length; i++) {
            for (int j = 0; j < sample.length; j++) {
                if (i != j) {
                    assertNotEquals(meansValues[i], sample[j], 1e-15);
                }
            }
        }

        RealMatrix covarianceMatrix = mvd.covarianceMatrix;
        for (int i = 0; i < sample.length; i++) {
            for (int j = 0; j < sample.length; j++) {
                assertEquals(covarianceMatrix.getEntry(i, i), FastMath.pow(covarianceMatrix.getEntry(i, i), 0.5), 1e-15);
            }
        }

        for (int i = 0; i < sample.length; i++) {
            for (int j = 0; j < sample.length; j++) {
                if (i != j) {
                    assertEquals(covarianceMatrix.getEntry(i, j), 0.0, 1e-15);
                }
            }
        }
    }

    private double getExponentTerm(double[] values, double[] means, RealMatrix covarianceMatrix) {
        double[] centered = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            centered[i] = values[i] - means[i];
        }
        double[] preMultiplied = covarianceMatrixInverse(covarianceMatrix).operate(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.exp(-0.5 * sum);
    }

    private RealMatrix covarianceMatrixInverse(RealMatrix matrix) {
        EigenDecomposition eigenDecomposition = matrix.getEigenvalueDecomposition();
        return eigenDecomposition.getSolver().getInverse();
    }
}