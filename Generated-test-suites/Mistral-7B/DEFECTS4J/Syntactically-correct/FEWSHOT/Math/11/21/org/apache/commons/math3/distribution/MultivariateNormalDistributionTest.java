package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.Random;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        int dim = 3;
        double[] means = new double[]{0.0, 0.0, 0.0};
        double[][] covariances = new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };

        RandomGenerator rng = new Random();
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }

        assertEquals(dim, mnd.getDimension());
        assertArrayEquals(means, mnd.getMeans());

        Array2DRowRealMatrix covarianceMatrix = mnd.getCovariances();
        assertEquals(dim, covarianceMatrix.getRowDimension());
        assertEquals(dim, covarianceMatrix.getColumnDimension());
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                assertEquals(covariances[i][j], covarianceMatrix.getEntry(i, j), 0.001);
            }
        }

        Random random = new Random();
        double[] samples = mnd.sample();
        assertEquals(dim, samples.length);
        for (int i = 0; i < dim; i++) {
            assertTrue(samples[i] > -100.0 && samples[i] < 100.0);
        }

        double[] means2 = mnd.getMeans();
        for (int i = 0; i < dim; i++) {
            assertEquals(means[i], means2[i], 0.001);
        }

        double[] standardDeviations = mnd.getStandardDeviations();
        for (int i = 0; i < dim; i++) {
            assertEquals(Math.sqrt(covariances[i][i]), standardDeviations[i], 0.001);
        }

        double density = mnd.density(new double[]{0.0, 0.0, 0.0});
        assertEquals(density, 1.0 / (Math.sqrt(2 * Math.PI) * Math.sqrt(3.0)), 0.001);

        mnd.dispose();
    }
}