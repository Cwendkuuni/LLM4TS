package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = new double[]{0.0, 1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 0.5}, {0.5, 0.5, 3.0}};

        try {
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            assertEquals(means.length, mnd.getDimension());
            assertEquals(means.length, mnd.getMeans().length);
            assertArrayEquals(means, mnd.getMeans());

            RealMatrix covMatrix = mnd.getCovariances();
            assertEquals(means.length, covMatrix.getRowDimension());
            assertEquals(means.length, covMatrix.getColumnDimension());
            assertEquals(covariances[0].length, covMatrix.getColumnDimension());
            assertEquals(covariances[0].length, covMatrix.getRowDimension());
            assertArrayEquals(covariances[0], covMatrix.getColumnVector(0).toArray());

            double[] stdDevs = mnd.getStandardDeviations();
            assertEquals(means.length, stdDevs.length);
            for (int i = 0; i < stdDevs.length; i++) {
                assertEquals(Math.sqrt(covariances[i][i]), stdDevs[i], 0.001);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testDensity() {
        double[] means = new double[]{0.0, 1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 0.5}, {0.5, 0.5, 3.0}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);

            double[] values = {0.0, 1.5, 2.5};
            double density = mnd.density(values);
            double pow = FastMath.pow(2 * FastMath.PI, -means.length / 2.0);
            double det = mnd.getCovarianceMatrixDeterminant();
            double exponentTerm = mnd.getExponentTerm(values);

            double expectedDensity = pow * det * exponentTerm;
            assertEquals(expectedDensity, density, 0.001);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testSample() {
        double[] means = new double[]{0.0, 1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 0.5}, {0.5, 0.5, 3.0}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);

            double[] sample = mnd.sample();
            assertNotNull(sample);
            assertEquals(means.length, sample.length);

            for (int i = 0; i < sample.length; i++) {
                assertEquals(Double.class, sample[i].getClass());
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown during test: " + e.getMessage());
        }
    }

    @Test
    public void testGetCovarianceMatrixDeterminant() {
        double[] means = new double[]{0.0, 1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 0.5}, {0.5, 0.5, 3.0}};
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mnd;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
            double det = mnd.getCovarianceMatrixDeterminant();

            double[][] covMatrix = mnd.getCovariances().getData();
            double detExpected = 1.0 / (FastMath.sqrt(2.0 * FastMath.PI) * FastMath.sqrt(FastMath.DETERMINANT3x3(covMatrix)));
            assertEquals(detExpected, det, 0.001);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown during test: " + e.getMessage());
        }
    }
}