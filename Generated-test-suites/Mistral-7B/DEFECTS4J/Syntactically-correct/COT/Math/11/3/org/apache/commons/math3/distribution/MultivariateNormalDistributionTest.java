package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

/**
 * Test cases for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionCreation() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(means.length, mvn.getDimension());
        assertEquals(means.length, mvn.getMeans().length);
        assertEquals(means, mvn.getMeans());

        RealMatrix covarianceMatrix = mvn.getCovariances();
        assertArrayEquals(covariances, covarianceMatrix.getData(), 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionCreationMeansAndCovariancesMismatch() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 3.0}};

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistributionCreationSingularMatrix() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistributionCreationNonPositiveDefiniteMatrix() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, -1.0}, {-1.0, 1.0}};

        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test
    public void testDensity() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(rng, means, covariances);

        double[] valuesTest = new double[]{1.5, 3.0};
        double expected = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(1.0 / Math.sqrt(2.0), -1) * FastMath.exp(-0.5 * ((FastMath.pow(valuesTest[0] - 1.0, 2.0) / 1.0) + FastMath.pow(valuesTest[1] - 2.0, 2.0) / 2.0));

        double density = mvn.density(valuesTest);
        assertEquals(expected, density, 1e-6);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(rng, means, covariances);
        double[] sample = mvn.sample();

        double[] meansCopy = mvn.getMeans().clone();
        double[] stdDeviations = mvn.getStandardDeviations();

        for (int i = 0; i < sample.length; i++) {
            assertEquals("Sample point [" + i + "] is out of expected range: expected [" + meansCopy[i] + "], got [" + sample[i] + "]", meansCopy[i], sample[i], 1e-6);
        }

        double[] eigValues = new double[covariances[0].length];
        EigenDecomposition eigenDecomposition = new EigenDecomposition(new Array2DRowRealMatrix(covariances));
        eigenDecomposition.getRealEigenvalues(eigValues);

        for (int i = 0; i < eigValues.length; i++) {
            double stdDeviation = stdDeviations[i];
            double expectedVariance = eigValues[i];

            double variance = 0;
            for (int j = 0; j < sample.length; j++) {
                variance += FastMath.pow(sample[j] - meansCopy[j], 2);
            }

            assertEquals("Variance of sample[" + i + "] is not equal to expected variance: expected [" + expectedVariance + "], got [" + variance + "]", expectedVariance, variance, 1e-6);
        }
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] valuesTest = new double[]{1.5, 3.0};
        double expected = FastMath.exp(-0.5 * ((FastMath.pow(valuesTest[0] - 1.0, 2.0) / 1.0) + FastMath.pow(valuesTest[1] - 2.0, 2.0) / 2.0));

        double exponentTerm = mvn.getExponentTerm(valuesTest);
        assertEquals(expected, exponentTerm, 1e-6);
    }

    @Test
    public void testGetExponentTermNegativeValues() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] valuesTest = new double[]{-1.5, -3.0};
        double expected = FastMath.exp(0.5 * ((FastMath.pow(valuesTest[0] - 1.0, 2.0) / 1.0) + FastMath.pow(valuesTest[1] - 2.0, 2.0) / 2.0));

        double exponentTerm = mvn.getExponentTerm(valuesTest);
        assertEquals(expected, exponentTerm, 1e-6);
    }

    @Test
    public void testGetExponentTermZeroValues() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] valuesTest = new double[]{0.0, 0.0};
        double expected = 1.0;

        double exponentTerm = mvn.getExponentTerm(valuesTest);
        assertEquals(expected, exponentTerm, 1e-6);
    }
}