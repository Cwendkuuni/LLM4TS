package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealArray2DSampleGenerator;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

/**
 * Tests for {@link MultivariateNormalDistribution} class.
 */
public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-12;
    private static final double[] MEANS = new double[] {1.0, 2.0};
    private static final double[][] COVARIANCES = new double[][] {
            {1.0, 0.5},
            {0.5, 2.0}
    };
    private MultivariateNormalDistribution distribution;
    private double[] means;
    private RealMatrix covarianceMatrix;
    private double covarianceMatrixDeterminant;

    @Before
    public void setUp() throws Exception {
        RandomGenerator rng = new Well19937c();
        distribution = new MultivariateNormalDistribution(rng, MEANS, COVARIANCES);
        means = MEANS.clone();
        covarianceMatrix = new Array2DRowRealMatrix(COVARIANCES);
        covarianceMatrixDeterminant = covarianceMatrix.getDeterminant();
    }

    @Test
    public void testConstructorWithVectorAndMatrix() throws Exception {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(MEANS, COVARIANCES);
        assertEquals(means.length, dist.getDimension());
        assertEquals(MEANS, dist.getMeans());
        assertEquals(covarianceMatrix, dist.getCovariances());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithVectorAndWrongMatrix() throws Exception {
        double[][] covariances = new double[][] {
            {1.0, 0.5},
            {0.5, 3.0}
        };
        new MultivariateNormalDistribution(MEANS, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularMatrix() throws Exception {
        double[][] covariances = new double[][] {
            {0.0, 0.5},
            {0.5, 0.0}
        };
        new MultivariateNormalDistribution(MEANS, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeCovarianceMatrix() throws Exception {
        double[][] covariances = new double[][] {
            {-1.0, 0.5},
            {0.5, -2.0}
        };
        new MultivariateNormalDistribution(MEANS, covariances);
    }

    @Test
    public void testGetMeans() {
        assertArrayEquals(means, distribution.getMeans());
    }

    @Test
    public void testGetCovariances() {
        assertEquals(covarianceMatrix, distribution.getCovariances());
    }

    @Test
    public void testDensity() {
        double[] values = new double[] {1.5, 3.5};
        double density = distribution.density(values);
        double expected = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(covarianceMatrixDeterminant, -0.5) * getExponentTerm(values);
        assertEquals(expected, density, EPSILON);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        for (int i = 0; i < sample.length; i++) {
            assertTrue(sample[i] >= 0.0);
        }
        assertEquals(means.length, sample.length);
    }

    @Test
    public void testSampleMatrix() {
        int size = 5;
        RealMatrix sampleMatrix = new Array2DRowRealMatrix(size, getDimension());
        RealArray2DSampleGenerator generator = new RealArray2DSampleGenerator(distribution, size);
        for (int i = 0; i < size; i++) {
            sampleMatrix.setRow(i, generator.nextValue());
        }
        RealMatrix expectedSampleMatrix = new Array2DRowRealMatrix(size, getDimension());
        for (int i = 0; i < size; i++) {
            double[] sample = distribution.sample();
            for (int j = 0; j < getDimension(); j++) {
                expectedSampleMatrix.setEntry(i, j, sample[j]);
            }
        }
        assertEquals(expectedSampleMatrix, sampleMatrix);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = new double[] {1.5, 3.5};
        double exponentTerm = distribution.getExponentTerm(values);
        double expected = FastMath.exp(-0.5 * (covarianceMatrixInverse.operate(difference(values, means)).dotProduct(difference(values, means))));
        assertEquals(expected, exponentTerm, EPSILON);
    }

    private double[] difference(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }

}