package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private double[] means;
    private double[][] covariances;
    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        means = new double[]{1, 2};
        covariances = new double[][]{{2, 1}, {1, 2}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testConstructor() {
        assertArrayEquals(means, distribution.getMeans(), 0);
        assertArrayEquals(covariances, distribution.getCovariances().getData(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorPreconditions1() {
        new MultivariateNormalDistribution(new Well19937c(), new double[]{}, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorPreconditions2() {
        new MultivariateNormalDistribution(new Well19937c(), means, new double[][]{{1, 0}, {0, 0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorPreconditions3() {
        new MultivariateNormalDistribution(new Well19937c(), means, new double[][]{{1, 2, 3}, {4, 5, 6}});
    }

    @Test
    public void testDensity() {
        double[] vals = {1, 2};
        double result = (Math.exp(-0.25) / (2 * Math.PI));
        assertEquals(result, distribution.density(vals), 1e-15);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityPreconditions1() {
        double[] vals = {1, 2, 3};
        distribution.density(vals);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityPreconditions2() {
        double[] vals = {1};
        distribution.density(vals);
    }

    @Test
    public void testSample() {
        RandomDataGenerator generator = new RandomDataGenerator();
        generator.reSeed(100);
        distribution.reseedRandomGenerator(100);
        double[] vals = distribution.sample();
        RealMatrix samplingMatrixInverse = new Array2DRowRealMatrix(means.length, means.length);
        RealMatrix tmpMatrix = distribution.getSamplingMatrix().transpose();

        for (int row = 0; row < means.length; row++) {
            double[] rowCol = tmpMatrix.getRowMatrix(row).getColumn(0);
            for (int col = 0; col < means.length; col++) {
                samplingMatrixInverse.setEntry(row, col, rowCol[col]);
            }
        }

        RealMatrix meansMatrix = new Array2DRowRealMatrix(new double[][]{{distribution.getMeans()[0]}, {distribution.getMeans()[1]}});

        RealMatrix result = tmpMatrix.multiply(meansMatrix).add(generator.nextVector(means.length));

        assertArrayEquals(result.getColumn(0), vals, 1e-15);

    }

    @Test
    public void testGetStandardDeviations() {
        double[] std = {Math.sqrt(2), Math.sqrt(2)};
        assertArrayEquals(std, distribution.getStandardDeviations(), 1e-15);
    }

    @Test
    public void testCovarianceMatrix() {
        RealMatrix covariancesMatrix = new Array2DRowRealMatrix(covariances);
        assertEquals(distribution.getCovariances(), covariancesMatrix);
    }

    @Test
    public void testMeans() {
        assertArrayEquals(distribution.getMeans(), means, 1e-15);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix covariancesMatrix = new Array2DRowRealMatrix(covariances);
        assertEquals(distribution.getCovariances(), covariancesMatrix);
    }

    @Test
    public void testGetSamplingMatrix() {
        RealMatrix samplingMatrixInverse = new Array2DRowRealMatrix(means.length, means.length);
        RealMatrix tmpMatrix = distribution.getSamplingMatrix().transpose();

        for (int row = 0; row < means.length; row++) {
            double[] rowCol = tmpMatrix.getRowMatrix(row).getColumn(0);
            for (int col = 0; col < means.length; col++) {
                samplingMatrixInverse.setEntry(row, col, rowCol[col]);
            }
        }

        RealMatrix samplingMatrix = tmpMatrix.multiply(samplingMatrixInverse);

        assertEquals(distribution.getSamplingMatrix(), samplingMatrix);
    }

    @Test
    public void testGetExponentTerm() {
        double[] vals = {1, 2};
        double[] centered = new double[vals.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = vals[i] - distribution.getMeans()[i];
        }
        RealMatrix centeredMatrix = new Array2DRowRealMatrix(new double[][]{{centered[0]}, {centered[1]}});

        RealMatrix centeredMatrixInverse = distribution.getCovariances().preMultiply(centeredMatrix);
        double sum = centeredMatrix.transpose().multiply(centeredMatrixInverse).getEntry(0, 0);
        double result = Math.exp(-0.5 * sum);

        assertEquals(result, distribution.getExponentTerm(vals), 1e-15);
    }
}