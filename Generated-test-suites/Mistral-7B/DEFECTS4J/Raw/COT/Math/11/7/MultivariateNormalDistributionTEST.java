package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 *
 * @author John Doe
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructorWithMeansAndCovariances() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, distribution.getDimension());
        assertEquals(means, distribution.getMeans());
        assertEquals(covariances, distribution.getCovariances().getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentArrays() throws Exception {
        final double[] means = {1.0};
        final double[][] covariances = {{}, {1.0, 2.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInconsistentRowsColumns() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0}, {1.0, 2.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteCovarianceMatrix() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{-1.0, 0.5}, {0.5, -1.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorWithSingularCovarianceMatrix() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 1.0}, {1.0, 1.0}};

        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testDensity() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        final double[] testValues = {1.5, 2.5};
        double density = distribution.density(testValues);
        double expected = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(distribution.covarianceMatrixDeterminant, -0.5) * distribution.getExponentTerm(testValues);
        assertEquals(expected, density, 1e-8);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithIncorrectValuesLength() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        final double[] testValues = {1.0};
        distribution.density(testValues);
    }

    @Test
    public void testSample() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        final double[] sample = distribution.sample();
        final int dimension = distribution.getDimension();
        assertEquals(dimension, sample.length);
        for (int i = 0; i < dimension; i++) {
            assertTrue(sample[i] > -1000 && sample[i] < 1000);
        }
    }

    @Test
    public void testSampleMultipleTimes() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        final int size = 1000;
        final double[][] samples = new double[size][distribution.getDimension()];
        for (int i = 0; i < size; i++) {
            samples[i] = distribution.sample();
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < distribution.getDimension(); j++) {
                assertTrue(samples[i][j] > -1000 && samples[i][j] < 1000);
            }
        }
    }

    @Test
    public void testGetStandardDeviations() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        final double[] standardDeviations = distribution.getStandardDeviations();
        assertEquals(distribution.getDimension(), standardDeviations.length);
        for (int i = 0; i < distribution.getDimension(); i++) {
            assertTrue(standardDeviations[i] > -1000 && standardDeviations[i] < 1000);
        }
    }

    @Test
    public void testGetMeans() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        final double[] testMeans = distribution.getMeans();
        assertEquals(means.length, testMeans.length);
        for (int i = 0; i < means.length; i++) {
            assertEquals(means[i], testMeans[i], 1e-8);
        }
    }

    @Test
    public void testGetCovariances() throws Exception {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        final RealMatrix testCovariances = distribution.getCovariances();
        final int dimension = distribution.getDimension();
        assertEquals(dimension, testCovariances.getRowDimension());
        assertEquals(dimension, testCovariances.getColumnDimension());
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                assertEquals(covariances[i][j], testCovariances.getEntry(i, j), 1e-8);
            }
        }
    }

}