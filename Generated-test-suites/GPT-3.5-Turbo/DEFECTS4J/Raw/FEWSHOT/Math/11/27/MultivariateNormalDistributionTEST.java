
package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private static final double EPSILON = 1e-10;

    private double[] means;
    private double[][] covariances;
    private MultivariateNormalDistribution distribution;

    @Before
    public void setup() {
        means = new double[]{0.0, 0.0};
        covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() {
        new MultivariateNormalDistribution(new double[]{0.0, 0.0, 0.0}, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrix() {
        double[][] singularMatrix = new double[][]{{1.0, 2.0}, {2.0, 4.0}};
        new MultivariateNormalDistribution(means, singularMatrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double[][] nonPositiveDefiniteMatrix = new double[][]{{1.0, 0.0}, {0.0, -1.0}};
        new MultivariateNormalDistribution(means, nonPositiveDefiniteMatrix);
    }

    @Test
    public void testGetMeans() {
        double[] expectedMeans = new double[]{0.0, 0.0};
        double[] actualMeans = distribution.getMeans();
        assertNotNull(actualMeans);
        assertEquals(expectedMeans.length, actualMeans.length);
        for (int i = 0; i < expectedMeans.length; i++) {
            assertEquals(expectedMeans[i], actualMeans[i], EPSILON);
        }
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expectedCovariances = distribution.getCovariances();
        assertNotNull(expectedCovariances);
        assertEquals(2, expectedCovariances.getRowDimension());
        assertEquals(2, expectedCovariances.getColumnDimension());
        for (int i = 0; i < expectedCovariances.getRowDimension(); i++) {
            for (int j = 0; j < expectedCovariances.getColumnDimension(); j++) {
                assertEquals(covariances[i][j], expectedCovariances.getEntry(i, j), EPSILON);
            }
        }
    }

    @Test
    public void testDensity() {
        double[] vals = new double[]{0.5, 0.5};
        double expectedDensity = 0.15915494309189535;
        double actualDensity = distribution.density(vals);
        assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = new double[]{1.0, 1.0};
        double[] actualStandardDeviations = distribution.getStandardDeviations();
        assertNotNull(actualStandardDeviations);
        assertEquals(expectedStandardDeviations.length, actualStandardDeviations.length);
        for (int i = 0; i < expectedStandardDeviations.length; i++) {
            assertEquals(expectedStandardDeviations[i], actualStandardDeviations[i], EPSILON);
        }
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(means.length, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] vals = new double[]{0.5, 0.5};
        double[] centered = new double[]{0.5, 0.5};
        double[] preMultiplied = new double[]{0.5, 0.5};
        double expectedExponentTerm = FastMath.exp(-0.125);
        distribution.getMeans()[0] = 0.5;
        double actualExponentTerm = distribution.getExponentTerm(vals);
        assertEquals(expectedExponentTerm, actualExponentTerm, EPSILON);
    }
}
