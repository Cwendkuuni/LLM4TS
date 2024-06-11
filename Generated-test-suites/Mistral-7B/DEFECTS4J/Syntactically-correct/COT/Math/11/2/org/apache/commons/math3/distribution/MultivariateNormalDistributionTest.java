package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test MultivariateNormalDistribution class
 */
public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-12;
    private static final RandomGenerator rng = new Well19937c();
    private MultivariateNormalDistribution distribution;
    private double[] means;
    private double[][] covariances;

    @Before
    public void setUp() {
        means = new double[]{1.0, 2.0};
        covariances = new double[][]{{1.5, 0.5}, {0.5, 2.5}};
        try {
            distribution = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (NonPositiveDefiniteMatrixException | SingularMatrixException | DimensionMismatchException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testConstructor() {
        assertEquals(means.length, distribution.getDimension());
        assertArrayEquals(means, distribution.getMeans());
        RealMatrix matrix = distribution.getCovariances();
        assertEquals(covariances.length, matrix.getRowDimension());
        assertEquals(covariances[0].length, matrix.getColumnDimension());
        for (int i = 0; i < covariances.length; i++) {
            assertArrayEquals(covariances[i], matrix.getRowVector(i).toArray());
        }
    }

    @Test
    public void testDensity() {
        double[] testValues = new double[]{1.1, 2.1};
        double densityValue = distribution.density(testValues);
        double expectedValue = FastMath.pow(2 * FastMath.PI, -means.length / 2)
                * FastMath.pow(distribution.covarianceMatrixDeterminant, -0.5)
                * distribution.getExponentTerm(testValues);
        assertEquals(Precision.abs(densityValue, expectedValue), expectedValue, EPSILON);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        for (int i = 0; i < means.length; i++) {
            assertEquals(means[i], sample[i], EPSILON);
        }
        for (int i = 0; i < means.length; i++) {
            for (int j = 0; j < means.length; j++) {
                double covariance = covariances[i][j];
                double correlation = covariances[i][j] / Math.sqrt(covariance * covariance + 1);
                double difference = FastMath.abs(sample[i] - sample[j]);
                double expectedCorrelation = FastMath.sqrt(covariance * covariance + 1) * correlation;
                assertEquals(expectedCorrelation, difference, 2 * EPSILON);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSampleWrongDimension() {
        distribution = new MultivariateNormalDistribution(rng, means, new double[1][means.length]);
        distribution.sample();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeCovariance() {
        covariances = new double[][]{{1.5, -0.5}, {-0.5, 2.5}};
        try {
            distribution = new MultivariateNormalDistribution(rng, means, covariances);
            fail("Should throw exception");
        } catch (NonPositiveDefiniteMatrixException e) {
            // pass
        }
    }
}