package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] expected = {1, 2, 3};
        assertArrayEquals(expected, distribution.getMeans(), 1e-9);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        assertEquals(expected, distribution.getCovariances());
    }

    @Test
    public void testDensity() {
        double[] vals = {1, 2, 3};
        double expected = 0.06349363593424098;
        assertEquals(expected, distribution.density(vals), 1e-9);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithIncorrectDimension() {
        double[] vals = {1, 2};
        distribution.density(vals);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1, 1, 1};
        assertArrayEquals(expected, distribution.getStandardDeviations(), 1e-9);
    }

    @Test
    public void testSample() {
        RandomDataGenerator random = new RandomDataGenerator();
        double[] expected = {1, 2, 3};
        double[] vals = distribution.sample();
        assertArrayEquals(expected, vals, 1e-9);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1, 2, 3};
        double expected = 1;
        assertEquals(expected, distribution.getExponentTerm(values), 1e-9);
    }

}