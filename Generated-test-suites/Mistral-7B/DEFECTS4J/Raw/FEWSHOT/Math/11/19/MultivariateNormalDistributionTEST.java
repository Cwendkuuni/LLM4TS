package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSpectiveFactory;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        assertEquals(2, mvn.getDimension());
        assertArrayEquals(means, mvn.getMeans());

        RealMatrix covarianceMatrix = mvn.getCovariances();
        Array2DRowRealMatrix covarianceMatrixView = (Array2DRowRealMatrix) covarianceMatrix;
        assertArrayEquals(covariances[0], covarianceMatrixView.getColumnVector(0));
        assertArrayEquals(covariances[1], covarianceMatrixView.getColumnVector(1));

        assertEquals(1.0 / Math.sqrt(2 * Math.PI), mvn.density(new double[]{0.0, 0.0}), 0.001);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        double[][] testData = {{1.5, 1.5}, {1.5, 2.5}, {2.5, 1.5}, {2.5, 2.5}};
        for (double[] data : testData) {
            double density = mvn.density(data);
            System.out.println("Data: " + Arrays.toString(data) + ", Density: " + density);
        }
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        for (int i = 0; i < 10; i++) {
            double[] sample = mvn.sample();
            System.out.println("Sample: " + Arrays.toString(sample));
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        double[] standardDeviations = mvn.getStandardDeviations();
        assertEquals(Math.sqrt(1.0), standardDeviations[0], 0.001);
        assertEquals(Math.sqrt(2.0), standardDeviations[1], 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidDataLength() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);

        mvn.density(new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};

        MultivariateNormalDistribution mvn = null;
        try {
            mvn = new MultivariateNormalDistribution(means, covariances);
        } catch (Exception e) {
            // Expected exception
        }
        assertNull(mvn);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{-1.0, 0.5}, {0.5, -1.0}};

        MultivariateNormalDistribution mvn = null;
        try {
            mvn = new MultivariateNormalDistribution(means, covariances);
        } catch (Exception e) {
            // Expected exception
        }
        assertNull(mvn);
    }

    @Test
    public void testEigenDecomposition() {
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(covariances);
        EigenDecomposition covMatDec = new EigenDecomposition(covarianceMatrix);

        double[] eigenvalues = covMatDec.getRealEigenvalues();
        RealMatrix eigenvectors = covMatDec.getEigenvectors();

        // Check eigenvalues
        assertEquals(1.5, eigenvalues[0], 0.001);
        assertEquals(3.5, eigenvalues[1], 0.001);

        // Check eigenvectors
        RealMatrix expectedEigenvectors = RealArray2DSpectiveFactory.singularValueDecomposition(covarianceMatrix).getV();
        assertEquals(expectedEigenvectors, eigenvectors);
    }

    @Test
    public void testSampleWithGivenSeed() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        RandomGenerator rng = new Well19937c(42);

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances, rng);

        for (int i = 0; i < 10; i++) {
            double[] sample = mvn.sample();
            System.out.println("Sample: " + Arrays.toString(sample));
        }
    }
}