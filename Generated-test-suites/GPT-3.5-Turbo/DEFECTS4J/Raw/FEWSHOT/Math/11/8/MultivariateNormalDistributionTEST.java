
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private double[] means;
    private double[][] covariances;
    private MultivariateNormalDistribution distribution;
    private int dimension;
    private double[] sample;

    @Before
    public void setUp() {
        means = new double[]{1, 2, 3};
        covariances = new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}};
        distribution = new MultivariateNormalDistribution(means, covariances);
        dimension = means.length;
        sample = new double[dimension];
    }

    @Test
    public void testConstructor() {
        assertArrayEquals(means, distribution.getMeans(), 0);
        assertEquals(covariances.length, distribution.getCovariances().getRowDimension());
        assertEquals(covariances[0].length, distribution.getCovariances().getColumnDimension());
        assertArrayEquals(covariances, distribution.getCovariances().getData(), 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidDimension() {
        new MultivariateNormalDistribution(means, new double[][]{{1, 0}, {0, 2}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentMeans() {
        means = new double[]{1, 2};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentCovariances() {
        covariances = new double[][]{{1, 0}, {0, 2}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        covariances = new double[][]{{-1, 0}, {0, 2}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() {
        double[] values = new double[]{1, 2, 3};
        double expectedDensity = 0.013480755531649505;
        assertEquals(expectedDensity, distribution.density(values), 0);

        values = new double[]{4, 5, 6};
        expectedDensity = 0.001167540184195347;
        assertEquals(expectedDensity, distribution.density(values), 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidDimension() {
        double[] values = new double[]{1, 2};
        distribution.density(values);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = new double[]{1, Math.sqrt(2), Math.sqrt(3)};
        assertArrayEquals(expectedStandardDeviations, distribution.getStandardDeviations(), 1e-15);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        double[][] sampleData = new double[1000][dimension];

        for (int i = 0; i < 1000; i++) {
            sample = distribution.sample();
            sampleData[i] = sample;
        }

        RealMatrix sampleMatrix = distribution.getCovariances().getSubMatrix(0, dimension - 1, 0, dimension - 1);
        double[][] sampleCovariances = sampleMatrix.getData();

        for (int i = 0; i < dimension; i++) {
            double[] sampleColumn = new double[1000];
            for (int j = 0; j < 1000; j++) {
                sampleColumn[j] = sampleData[j][i];
            }

            double sampleMean = distribution.getMeans()[i];

            assertEquals(sampleMean, MathArrays.mean(sampleColumn), 0.1);
            assertEquals(sampleCovariances[i][i], MathArrays.variance(sampleColumn), 0.1);
        }
    }
}

