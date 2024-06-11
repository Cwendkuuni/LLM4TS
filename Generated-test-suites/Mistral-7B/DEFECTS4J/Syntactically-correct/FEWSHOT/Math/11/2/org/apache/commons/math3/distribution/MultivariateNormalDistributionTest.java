package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionImplTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testDimensions() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {
            {1.0, 0.5},
            {0.5, 1.5}
        };
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        assertEquals("Incorrect number of dimensions.", 2, mvd.getDimension());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidMeansLength() {
        final double[] means = new double[] {1.0};
        final double[][] covariances = new double[][] {
            {1.0, 0.5},
            {0.5, 1.5}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesRows() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {
            {1.0, 0.5},
            {0.5}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesColumns() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {
            {1.0, 0.5},
            {0.5, 1.5},
            {0.5, 1.5}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularCovarianceMatrix() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {
            {0.0, 0.0},
            {0.0, 0.0}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteCovarianceMatrix() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {
            {-1.0, 0.5},
            {0.5, -1.0}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NullPointerException.class)
    public void testNullMeans() {
        final double[][] covariances = new double[][] {
            {1.0, 0.5},
            {0.5, 1.5}
        };
        new MultivariateNormalDistribution(null, covariances);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCovariances() {
        final double[] means = new double[] {1.0, 2.0};
        new MultivariateNormalDistribution(means, null);
    }

    @Test
    public void testMeans() {
        final double[] means = new double[] {1.0, 2.0};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means,
                new double[][] {{1.0, 0.5}, {0.5, 1.5}});
        assertArrayEquals("Incorrect means.", means, mvd.getMeans());
    }

    @Test
    public void testCovariances() {
        final double[] means = new double[] {1.0, 2.0};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means,
                new double[][] {{1.0, 0.5}, {0.5, 1.5}});
        final RealMatrix covMatrix = mvd.getCovariances();
        final double[][] covMatrixData = covMatrix.getData();
        assertArrayEquals("Incorrect covariance matrix.", new double[][] {{1.0, 0.5}, {0.5, 1.5}}, covMatrixData);
    }

    @Test
    public void testMeansAndCovariances() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals("Incorrect means.", means, mvd.getMeans());
        final RealMatrix covMatrix = mvd.getCovariances();
        final double[][] covMatrixData = covMatrix.getData();
        assertArrayEquals("Incorrect covariance matrix.", covariances, covMatrixData);
    }

    @Test
    public void testDensity() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        final double[] testData = new double[] {3.0, 3.0};
        try {
            mvd.density(testData);
        } catch (DimensionMismatchException dme) {
            // Throws expected exception
            return;
        }
        fail("Expected DimensionMismatchException, but got none.");
    }

    @Test
    public void testDensity1() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        final double[] testData = new double[] {1.5, 2.5};
        final double expected = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(1.0, -1.0) *
                FastMath.pow(1.5, -1.0) * FastMath.exp(-0.5 * ((1.5 - 1.0) * 1.0 / 1.0 + (2.5 - 2.0) * 0.5 / 1.5));
        assertEquals("Incorrect density.", expected, mvd.density(testData), 1e-13);
    }

    @Test
    public void testDensity2() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        final double[] testData = new double[] {-2.5, 3.5};
        try {
            mvd.density(testData);
        } catch (DimensionMismatchException dme) {
            // Throws expected exception
            return;
        }
        fail("Expected DimensionMismatchException, but got none.");
    }

    @Test
    public void testSample() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        final double[] sample = mvd.sample();
        assertNotSame("Samples should not be the same.", sample, means);
        final double[] testData = mvd.sample();
        assertArrayEquals("Incorrect sample.", sample, testData);
    }

    @Test
    public void testSampleSize() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        final double[] sample = mvd.sample();
        assertEquals("Incorrect sample size.", sample.length, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void testSampleSizeTooBig() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        mvd.sample(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSampleSizeNegative() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        mvd.sample(-1);
    }

    @Test
    public void testStandardDeviations() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        final double[] stdDev = mvd.getStandardDeviations();
        assertArrayEquals("Incorrect standard deviations.", new double[]{1.0, 0.7071067811865476}, stdDev, 1e-13);
    }

    @Test
    public void testGetSampleMatrix() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        final RealMatrix sampleMatrix = mvd.getSamplingMatrix();
        final double[][] sampleMatrixData = sampleMatrix.getData();
        final Array2DRowRealMatrix expected = new Array2DRowRealMatrix(
                new double[][] {{Math.sqrt(1.0), Math.sqrt(0.5)}, {-Math.sqrt(1.0), Math.sqrt(0.5)}});
        assertArrayEquals("Incorrect sampling matrix.", expected.getData(), sampleMatrixData);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSampleMatrixWhenNotInitialized() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        mvd.getSamplingMatrix();
        mvd.dispose(); // Dispose the instance to make sure it's not initialized
        mvd.getSamplingMatrix();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSampleMatrixTwice() {
        final double[] means = new double[] {1.0, 2.0};
        final double[][] covariances = new double[][] {{1.0, 0.5}, {0.5, 1.5}};
        final MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(means, covariances);
        final RealMatrix sampleMatrix = mvd.getSamplingMatrix();
        sampleMatrix; // Consume the initial result
        try {
            mvd.getSamplingMatrix();
        } catch (IllegalStateException ies) {
            // Throws expected exception
            return;
        }
        fail("Expected IllegalStateException, but got none.");
    }
}

class MultivariateNormalDistributionImplTest extends MultivariateNormalDistribution {
    public MultivariateNormalDistributionImplTest(double[] means, double[][] covariances) {
        super(new Well19937c(), means, covariances);
    }
}