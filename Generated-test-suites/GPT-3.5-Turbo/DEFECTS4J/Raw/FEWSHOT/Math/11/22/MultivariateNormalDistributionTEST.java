
package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private static RandomGenerator rng;
    private static double[] means;
    private static double[][] covariances;
    private static MultivariateNormalDistribution distribution;

    @BeforeClass
    public static void init() throws Exception {
        rng = new Well19937c();
        means = new double[]{0, 0};
        covariances = new double[][]{{1, 0}, {0, 1}};
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
    }


    @Test
    public void testGetMeans() {
        double[] expectedMeans = new double[]{0, 0};
        assertArrayEquals(expectedMeans, distribution.getMeans(), 0);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(covariances);
        assertEquals(expectedCovariances, distribution.getCovariances());
    }

    @Test
    public void testDensity() throws DimensionMismatchException {
        double[] values = new double[]{1, 1};
        double expectedDensity = 0.058549831524319168;
        assertEquals(expectedDensity, distribution.density(values), 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatch() throws DimensionMismatchException {
        double[] values = new double[]{1};
        distribution.density(values);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStdDeviations = new double[]{1, 1};
        assertArrayEquals(expectedStdDeviations, distribution.getStandardDeviations(), 0);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(means.length, sample.length);
    }

    @Test
    public void testSampleMultipleTimes() {
        int sampleSize = 1000;
        double[][] samples = new double[sampleSize][];
        for (int i = 0; i < sampleSize; i++) {
            samples[i] = distribution.sample();
            assertNotNull(samples[i]);
            assertEquals(means.length, samples[i].length);
        }
    }

    @Test
    public void testSampleWithDifferentMeans() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] newMeans = new double[]{1, 2};
        distribution = new MultivariateNormalDistribution(rng, newMeans, covariances);

        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(newMeans.length, sample.length);
    }

    @Test
    public void testSampleWithDifferentCovariance() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[][] newCovariances = new double[][]{{2, 0}, {0, 2}};
        distribution = new MultivariateNormalDistribution(rng, means, newCovariances);

        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(means.length, sample.length);
    }

    @Test
    public void testSampleWithDifferentMeansAndCovariance() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] newMeans = new double[]{1, 2};
        double[][] newCovariances = new double[][]{{2, 0}, {0, 2}};
        distribution = new MultivariateNormalDistribution(rng, newMeans, newCovariances);

        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(newMeans.length, sample.length);
    }
}
