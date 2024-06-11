package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {
    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (NonPositiveDefiniteMatrixException | SingularMatrixException | DimensionMismatchException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] expected = {0, 0};
        assertArrayEquals(expected, distribution.getMeans(), 0);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        assertTrue(expected.subtract(distribution.getCovariances()).norm() < 1e-10);
    }

    @Test
    public void testDensity() {
        double[] values = {1, 1};
        double expected = (1 / (2 * FastMath.PI)) * FastMath.exp(-0.5 * 2);
        assertEquals(expected, distribution.density(values), 1e-10);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1, 1};
        assertArrayEquals(expected, distribution.getStandardDeviations(), 0);
    }

    @Test
    public void testSample() {
        // Since it is difficult to determine the expected value,
        // only validate the dimension
        double[] sample = distribution.sample();
        assertEquals(2, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1, 1};
        double[] centered = {1, 1};
        double[] preMultiplied = {1, 1};
        double sum = 2;
        double expected = FastMath.exp(-0.5 * sum);
        assertEquals(expected, distribution.getExponentTerm(values), 1e-10);
    }
}