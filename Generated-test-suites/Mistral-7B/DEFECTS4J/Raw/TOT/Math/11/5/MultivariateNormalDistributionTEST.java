package org.apache.commons.math3.distribution;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest extends AbstractTest {
    private static final double EPSILON = 1e-13;
    private static final double DELTA = 0.01;

    private MultivariateNormalDistribution dist;
    private RealMatrix covMatrix;
    private double[] means;
    private double[][] covariances;

    @Before
    public void setUp() throws Exception {
        RandomGenerator rng = new Well19937c();
        means = new double[]{0.5, 1.2, 3.7};
        covariances = new double[][]{
            {1.5, 0.2, 0.1},
            {0.2, 2.3, 0.1},
            {0.1, 0.1, 3.4}
        };
        dist = new MultivariateNormalDistribution(rng, means, covariances);
        covMatrix = dist.getCovariances();
    }

    /**
     * Test construction from valid input.
     */
    @Test
    public void testValidConstruction() {
        assertEquals(means.length, dist.getDimension());
        assertEquals(means, dist.getMeans());
        assertTrue(covMatrix.isSymmetric());
        assertEquals(covMatrix, dist.getCovariances());
    }

    /**
     * Test mean value and covariance matrix.
     */
    @Test
    public void testMeanAndCovariance() {
        assertArrayEquals(means, dist.sample(1)[0], EPSILON);
        RealMatrix covSample = new Array2DRowRealMatrix(dist.sample(100).toArray());
        assertArrayEquals(covMatrix.getData(), covSample.getData(), EPSILON);
    }

    /**
     * Test mean and covariance values.
     */
    @Test
    public void testMeanAndCovarianceValues() {
        double[] meanSample = dist.sample(1, true)[0];
        assertArrayEquals(means, meanSample, EPSILON);
        RealMatrix covSample = new Array2DRowRealMatrix(dist.sample(100, true).toArray());
        RealMatrix invCovSample = covSample.copy().inverse();
        RealVector meanVector = RealVector.valueOf(means);
        RealVector meanSampleVector = RealVector.valueOf(meanSample);
        assertEquals(covarianceMatrixDeterminant, invCovSample.determinant(), EPSILON);
        assertEquals(meanVector, invCovSample.preMultiply(meanSampleVector), EPSILON);

        Variance variance = new Variance();
        StandardDeviation stdDev = new StandardDeviation();
        double[] sample = new double[3];
        for (int i = 0; i < 1000; i++) {
            dist.sample(sample);
            variance.increment(sample[0] - means[0]);
            variance.increment(sample[1] - means[1]);
            variance.increment(sample[2] - means[2]);
        }
        double[] var = variance.getValue();
        assertEquals(covMatrix.getData()[0], var[0], EPSILON);
        assertEquals(covMatrix.getData()[1], var[1], EPSILON);
        assertEquals(covMatrix.getData()[2], var[2], EPSILON);
        assertEquals(covMatrix.getData()[3], var[3], EPSILON);
        assertEquals(covMatrix.getData()[4], var[4], EPSILON);
        assertEquals(covMatrix.getData()[5], var[5], EPSILON);
        assertEquals(covMatrix.getData()[6], var[6], EPSILON);
        assertEquals(covMatrix.getData()[7], var[7], EPSILON);
        double[] stdDevSample = new double[3];
        stdDev.increment(stdDevSample[0] - Math.sqrt(var[0]));
        stdDev.increment(stdDevSample[1] - Math.sqrt(var[1]));
        stdDev.increment(stdDevSample[2] - Math.sqrt(var[2]));
        assertArrayEquals(stdDevSample, dist.sample(1, true)[0], EPSILON);
    }

    /**
     * Test density calculation.
     */
    @Test
    public void testDensity() {
        double[] test = new double[]{0.5, 1.2, 3.7};
        assertEquals(dist.density(test), 0.182108461434, EPSILON);
        test = new double[]{3.7, 1.2, 0.5};
        assertEquals(dist.density(test), 0.0077876137687, EPSILON);
        test = new double[]{0.5, 1.2, 5.4};
        assertEquals(dist.density(test), 3.68452243207e-07, EPSILON);
    }

    /**
     * Test density calculation with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInputDensity() {
        dist.density(new double[]{-1.0, -2.0, -3.0});
    }

    /**
     * Test density calculation with non-matching dimension.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testNonMatchingDimensionDensity() {
        dist.density(new double[]{0.5, 1.2, 3.7, 4.0});
    }

    /**
     * Test sample calculation.
     */
    @Test
    public void testSample() {
        double[] result = dist.sample();
        for (int i = 0; i < 3; i++) {
            assertEquals(means[i], result[i], EPSILON);
        }
        result = dist.sample();
        for (int i = 0; i < 3; i++) {
            assertTrue(result[i] > means[i] - DELTA);
            assertTrue(result[i] < means[i] + DELTA);
        }
    }

    /**
     * Test sample calculation with non-matching dimension.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testNonMatchingDimensionSample() {
        dist.sample(4);
    }

    /**
     * Test sample calculation with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInputSample() {
        dist.sample(null);
    }

    /**
     * Test eigenvalues and eigenvectors.
     */
    @Test
    public void testEigenvaluesAndEigenvectors() {
        RealMatrix eigenvectors = covMatrix.getEigenvectors();
        RealMatrix eigenvalues = covMatrix.getEigenvalues();
        for (int i = 0; i < 3; i++) {
            assertTrue(eigenvectors.getColumnVector(i).norm2() > 0);
            assertEquals(covMatrixDeterminant, eigenvalues.getEntry(i, i), EPSILON);
        }
    }
}