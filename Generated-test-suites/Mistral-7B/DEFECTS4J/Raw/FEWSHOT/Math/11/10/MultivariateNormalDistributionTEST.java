package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covarianceMatrix = mvnd.getCovariances();
        RealMatrix covarianceMatrixInverse = mvnd.getCovarianceMatrixInverse();
        double covarianceMatrixDeterminant = mvnd.getCovarianceMatrixDeterminant();
        assertArrayEquals(new double[]{1.0, 2.0}, mvnd.getMeans());
        assertEquals(2, covarianceMatrix.getRowDimension());
        assertEquals(2, covarianceMatrix.getColumnDimension());
        assertEquals(2, covarianceMatrixInverse.getRowDimension());
        assertEquals(2, covarianceMatrixInverse.getColumnDimension());
        assertEquals(2.0, covarianceMatrixDeterminant, 0.001);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] testValues = {1.5, 2.5};
        double expectedDensity = 0.105539738671421;
        assertEquals(expectedDensity, mvnd.density(testValues), 0.001);

        testValues = new double[]{-1.0, 0.5};
        expectedDensity = 0.0013533528682876;
        assertEquals(expectedDensity, mvnd.density(testValues), 0.001);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] sample = mvnd.sample();
        assertArrayEquals(new double[]{2.03840022543264, 0.36719169098495}, sample, 0.001);

        sample = mvnd.sample();
        assertArrayEquals(new double[]{-0.16570866082118, 1.8113646249144}, sample, 0.001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] standardDeviations = mvnd.getStandardDeviations();
        assertArrayEquals(new double[]{1.0, 0.70710678118655}, standardDeviations, 0.001);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] centered = {0.5, 0.5};
        double[] preMultiplied = mvnd.covarianceMatrixInverse.operate(centered);
        double exponentTerm = mvnd.getExponentTerm(centered);

        double expectedExponentTerm = 0.41162344632583;
        assertEquals(expectedExponentTerm, exponentTerm, 0.001);
    }
}