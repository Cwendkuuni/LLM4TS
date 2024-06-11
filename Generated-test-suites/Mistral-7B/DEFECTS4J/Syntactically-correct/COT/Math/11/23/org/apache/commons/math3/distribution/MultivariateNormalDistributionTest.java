package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private static final int DIM = 3;
    private static final double EPSILON = 1e-12;

    private MultivariateNormalDistribution dist;
    private double[] means;
    private double[][] covariances;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        means = new double[]{0.1, 0.2, 0.3};
        covariances = new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 0.5}, {0.5, 0.5, 3.0}};
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(DIM, dist.getDimension());
        Assert.assertArrayEquals(means, dist.getMeans());
        RealMatrix covMat = dist.getCovariances();
        Assert.assertEquals(DIM, covMat.getRowDimension());
        Assert.assertEquals(DIM, covMat.getColumnDimension());
        Assert.assertArrayEquals(covMat.getData(), covariances, EPSILON);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWrongDimensions() {
        double[] means2D = {0.1, 0.2};
        double[][] covariances2D = {{1.0, 0.5}, {0.5, 2.0}, {0.5, 0.5}};
        new MultivariateNormalDistribution(rng, means2D, covariances2D);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrix() {
        double[][] covariancesSingular = {{1.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(rng, means, covariancesSingular);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double[][] covariancesNonPositiveDefinite = {{1.0, 0.5}, {0.5, -1.0}};
        new MultivariateNormalDistribution(rng, means, covariancesNonPositiveDefinite);
    }

    @Test
    public void testGetMeans() {
        Assert.assertArrayEquals(means, dist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        RealMatrix covMat = dist.getCovariances();
        Assert.assertEquals(DIM, covMat.getRowDimension());
        Assert.assertEquals(DIM, covMat.getColumnDimension());
        Assert.assertArrayEquals(covMat.getData(), covariances, EPSILON);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWrongDimension() {
        double[] vals = new double[DIM + 1];
        dist.density(vals);
    }

    @Test
    public void testDensity() {
        double[] vals = new double[]{1.0, 2.0, 3.0};
        double density = dist.density(vals);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -DIM / 2) *
                FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) *
                dist.getExponentTerm(vals);
        Assert.assertEquals(expectedDensity, density, EPSILON);
    }

    @Test
    public void testSample() {
        double[] sample = dist.sample();
        Assert.assertArrayEquals(DIM, sample.length);
        for (int i = 0; i < DIM; i++) {
            Assert.assertTrue(Double.isFinite(sample[i]));
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] std = dist.getStandardDeviations();
        Assert.assertArrayEquals(DIM, std.length);
        for (int i = 0; i < DIM; i++) {
            Assert.assertTrue(std[i] > 0);
        }
    }

    @Test
    public void testInverseCovarianceMatrix() {
        RealMatrix inverse = dist.getCovarianceMatrixInverse();
        RealMatrix inverseExpected = covarianceMatrixInverse;
        Assert.assertEquals(DIM, inverse.getRowDimension());
        Assert.assertEquals(DIM, inverse.getColumnDimension());
        Assert.assertArrayEquals(inverse.getData(), inverseExpected.getData(), EPSILON);
    }

    @Test
    public void testCovarianceMatrixDeterminant() {
        double determinant = dist.getCovarianceMatrixDeterminant();
        double determinantExpected = dist.covarianceMatrixDeterminant;
        Assert.assertEquals(determinantExpected, determinant, EPSILON);
    }

    @Test
    public void testSamplingMatrix() {
        RealMatrix samplingMatrixExpected = new Array2DRowRealMatrix(DIM, DIM)
                .multiply(new SingularValueDecomposition(new Array2DRowRealMatrix(covMatEigenvectors)).getV());
        RealMatrix samplingMatrix = dist.getSamplingMatrix();
        Assert.assertEquals(DIM, samplingMatrix.getRowDimension());
        Assert.assertEquals(DIM, samplingMatrix.getColumnDimension());
        Assert.assertEquals(samplingMatrixExpected, samplingMatrix);
    }
}