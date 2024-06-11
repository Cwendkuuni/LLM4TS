package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DBasic;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(rng, means, covariances);

        assertEquals(means.length, mvnd.getDimension());
        assertEquals(means, mvnd.getMeans());

        RealMatrix covarianceMatrix = mvnd.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        assertArrayEquals(expectedCovarianceMatrix.getData(), covarianceMatrix.getData());

        RealMatrix inverse = mvnd.getCovarianceMatrixInverse();
        RealMatrix expectedInverse = covarianceMatrix.getInverse();
        assertArrayEquals(expectedInverse.getData(), inverse.getData());

        assertEquals(1.0, mvnd.getCovarianceMatrixDeterminant(), 1e-10);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(rng, means, covariances);

        double[] stdDeviations = mvnd.getStandardDeviations();
        double[] expectedStdDeviations = new double[]{1.0, 0.70710678118654755};
        assertArrayEquals(expectedStdDeviations, stdDeviations);
    }

    @Test
    public void testDensity() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(rng, means, covariances);

        double[] values = new double[]{0.0, 0.5};
        double density = mvnd.density(values);

        double expectedDensity = Math.exp(-0.5 * (0.25));
        assertEquals(expectedDensity, density, 1e-8);
    }

    @Test
    public void testSample() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(rng, means, covariances);

        double[] sample = mvnd.sample();

        RealVector expectedMean = new Array2DRowRealMatrix(1, mvnd.getDimension())
                .getColumnVector(0);
        RealVector sampleVector = new Array2DRowRealMatrix(1, mvnd.getDimension())
                .getColumnVector(0);

        for (int i = 0; i < mvnd.getDimension(); i++) {
            expectedMean.setEntry(i, means[i]);
            sampleVector.setEntry(i, sample[i]);
        }

        RealVector difference = expectedMean.subtract(sampleVector);
        assertEquals(0.0, difference.getMaxEntry(), 1e-8);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(rng, means, covariances);

        double[] values = new double[]{1.0, 1.0};
        double exponentTerm = mvnd.getExponentTerm(values);

        double expectedExponentTerm = -0.25;
        assertEquals(expectedExponentTerm, exponentTerm, 1e-8);
    }
}