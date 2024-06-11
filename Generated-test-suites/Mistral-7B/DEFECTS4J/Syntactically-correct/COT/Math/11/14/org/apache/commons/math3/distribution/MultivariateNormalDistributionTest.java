package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for MultivariateNormalDistribution class.
 */
public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution dist;
    private double[] means = {1.0, 2.0};
    private double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @After
    public void tearDown() {
        dist = null;
    }

    /**
     * Test constructor MultivariateNormalDistribution.
     */
    @Test
    public void testConstructor() {
        assertNotNull(dist);
        assertEquals(means.length, dist.getDimension());
    }

    /**
     * Test getMeans method.
     */
    @Test
    public void testGetMeans() {
        RealVector meansVector = dist.getMeans();
        assertNotNull(meansVector);
        double[] meansArray = meansVector.toArray();
        assertNotNull(meansArray);
        assertArrayEquals(means, meansArray, 0.0);
    }

    /**
     * Test getCovariances method.
     */
    @Test
    public void testGetCovariances() {
        RealMatrix covMatrix = dist.getCovariances();
        assertNotNull(covMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(covariances);
        assertEquals(expected, covMatrix);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        double[] sample = dist.sample();
        assertNotNull(sample);
        assertEquals(dist.getDimension(), sample.length);
    }

    /**
     * Test density method for single value.
     */
    @Test
    public void testDensitySingleValue() {
        double value = 1.5;
        double density = dist.density(new double[]{value});
        assertTrue(density > 0);
    }

    /**
     * Test density method for multiple values.
     */
    @Test
    public void testDensityMultipleValues() {
        double[] values = {1.5, 2.5};
        double density = dist.density(values);
        assertTrue(density > 0);
    }

    /**
     * Test density method with invalid input.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() {
        dist.density(new double[]{1.0});
    }

    /**
     * Test getStandardDeviations method.
     */
    @Test
    public void testGetStandardDeviations() {
        double[] stdDeviations = dist.getStandardDeviations();
        assertNotNull(stdDeviations);
        assertEquals(dist.getDimension(), stdDeviations.length);
        for (int i = 0; i < stdDeviations.length; i++) {
            assertTrue(stdDeviations[i] > 0);
        }
    }

    /**
     * Test eigenvalue decomposition of covariance matrix.
     */
    @Test
    public void testEigenvalueDecomposition() {
        EigenDecomposition eigenDecomposition = new EigenDecomposition(dist.getCovariances());
        double[] eigenvalues = eigenDecomposition.getRealEigenvalues();
        for (int i = 0; i < eigenvalues.length; i++) {
            assertTrue(eigenvalues[i] > 0);
        }
    }
}