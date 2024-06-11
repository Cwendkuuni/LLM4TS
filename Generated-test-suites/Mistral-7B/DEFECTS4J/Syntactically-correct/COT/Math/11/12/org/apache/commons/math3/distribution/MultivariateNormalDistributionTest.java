package org.apache.commons.math3.distribution;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealArray2DSpectralDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution dist;
    private static final double EPSILON = 1e-8;
    private static final double[] MEANS = {0.0, 1.0, 2.0};
    private static final double[][] COVARIANCES = {
            {1.0, 0.5, 0.5},
            {0.5, 2.5, 0.5},
            {0.5, 0.5, 3.0}
    };

    @Before
    public void setup() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        RandomGenerator rng = new Well19937c();
        dist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
    }

    @Test
    public void testConstructorWithMeansAndCovariances() {
        Assert.assertArrayEquals(MEANS, dist.getMeans(), 0.0);
        RealMatrix covMatrix = dist.getCovariances();
        Assert.assertEquals(3, covMatrix.getRowDimension());
        Assert.assertEquals(3, covMatrix.getColumnDimension());
        Assert.assertArrayEquals(COVARIANCES[0], covMatrix.getColumnVector(0).toArray(), EPSILON);
        Assert.assertArrayEquals(COVARIANCES[1], covMatrix.getColumnVector(1).toArray(), EPSILON);
        Assert.assertArrayEquals(COVARIANCES[2], covMatrix.getColumnVector(2).toArray(), EPSILON);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        new MultivariateNormalDistribution(new double[]{0.0, 1.0, 2.0}, COVARIANCES);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[][] covariancesSingular = {
                {1.0, 0.5, 0.5},
                {0.5, 0.0, 0.5},
                {0.5, 0.5, 0.0}
        };
        new MultivariateNormalDistribution(MEANS, covariancesSingular);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] covariancesNPD = {
                {1.0, -0.5, 0.5},
                {-0.5, 2.5, 0.5},
                {0.5, 0.5, -3.0}
        };
        new MultivariateNormalDistribution(MEANS, covariancesNPD);
    }

    @Test
    public void testGetMeans() {
        Assert.assertArrayEquals(MEANS, dist.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix covMatrix = dist.getCovariances();
        Assert.assertEquals(3, covMatrix.getRowDimension());
        Assert.assertEquals(3, covMatrix.getColumnDimension());
        Assert.assertArrayEquals(COVARIANCES[0], covMatrix.getColumnVector(0).toArray(), EPSILON);
        Assert.assertArrayEquals(COVARIANCES[1], covMatrix.getColumnVector(1).toArray(), EPSILON);
        Assert.assertArrayEquals(COVARIANCES[2], covMatrix.getColumnVector(2).toArray(), EPSILON);
    }

    @Test
    public void testDensity() {
        double[] testValues = {0.5, 1.5, 2.5};
        double density = dist.density(testValues);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -3.0 / 2) * FastMath.pow(dist.covarianceMatrixDeterminant, -0.5);
        expectedDensity *= getExponentTerm(testValues);
        Assert.assertEquals(expectedDensity, density, 1e-6);
    }

    private double getExponentTerm(double[] values) {
        final int dim = dist.getDimension();
        double[] centered = new double[dim];
        for (int i = 0; i < dim; i++) {
            centered[i] = values[i] - dist.getMeans()[i];
        }
        RealVector covarianceMatrixInverseVector = dist.covarianceMatrixInverse.operate(new Array2DRowRealMatrix(centered).transpose().toVector());
        double sum = 0;
        for (int i = 0; i < covarianceMatrixInverseVector.getDimension(); i++) {
            sum += covarianceMatrixInverseVector.getEntry(i) * centered[i];
        }
        return FastMath.exp(-0.5 * sum);
    }

    @Test
    public void testSample() {
        double[] sample = dist.sample();
        for (int i = 0; i < dist.getDimension(); i++) {
            Assert.assertNotSame(dist.getMeans()[i], sample[i], 0.0);
        }
    }

}