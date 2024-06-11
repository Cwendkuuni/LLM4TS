package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution.Well19937cRandomGenerator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;
    private RandomGenerator randomGenerator;
    private double[][] covariances = {
            {1, 0.5}, {0.5, 1}
    };

    @Before
    public void setup() {
        randomGenerator = mock(Well19937cRandomGenerator.class);
        distribution = new MultivariateNormalDistribution(randomGenerator, new double[]{0, 0}, covariances);
    }

    @Test
    public void testCreateNormally() {
        assertEquals(2, distribution.getDimension());
        assertEquals(0, Precision.compareTo(distribution.getMeans()[0], 0, 1e-10));
        assertEquals(0, Precision.compareTo(distribution.getMeans()[1], 0, 1e-10));
        assertArrayEquals(covariances, distribution.getCovariances().getData(), 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testCreateWithInvalidMeansLength() {
        double[] means = {0};
        new MultivariateNormalDistribution(randomGenerator, means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testCreateWithInvalidCovariancesRowsNumber() {
        double[][] covariancesInvalidRowsNumber = {covariances[0], covariances[1], covariances[2]};
        new MultivariateNormalDistribution(randomGenerator, new double[]{0, 0}, covariancesInvalidRowsNumber);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testCreateWithInvalidCovariancesColumnsNumber() {
        double[][] covariancesInvalidColumnsNumber = {covariances[0], covariances[1]};
        new MultivariateNormalDistribution(randomGenerator, new double[]{0, 0}, covariancesInvalidColumnsNumber);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] standardDeviations = distribution.getStandardDeviations();
        assertEquals(1, standardDeviations[0]);
        assertEquals(0.5, standardDeviations[1]);
    }

    @Test
    public void testDensity() {
        double[] values = new double[]{1, 1};
        when(randomGenerator.nextGaussian()).thenReturn(0);
        double density = distribution.density(values);
        assertEquals(0.13533528323060508, density, 1e-10);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidValuesLength() {
        distribution.density(new double[]{0});
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertArrayEquals(new double[]{0, 0}, sample, 1e-10);
    }

    @Test
    public void testSampleMultipleTimes() {
        for (int i = 0; i < 10; i++) {
            double[] sample = distribution.sample();
            assertArrayEquals(new double[]{0, 0}, sample, 1e-10);
        }
    }

    @Test
    public void testSampleWithNonGaussianRandomGenerator() {
        RandomGenerator randomGenerator = mock(RealGenerator.class);
        when(randomGenerator.nextDouble()).thenReturn(0.5);
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(randomGenerator, new double[]{0, 0}, covariances);
        double[] sample = distribution.sample();
        assertArrayEquals(new double[]{1.23456789, 2.45678901}, sample, 1e-10);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[][] covariancesSingular = {
                {1, 0.5}, {0.5, 0}
        };
        new MultivariateNormalDistribution(randomGenerator, new double[]{0, 0}, covariancesSingular);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[][] covariancesNonPositiveDefinite = {
                {-1, 0.5}, {0.5, -1}
        };
        new MultivariateNormalDistribution(randomGenerator, new double[]{0, 0}, covariancesNonPositiveDefinite);
    }
}