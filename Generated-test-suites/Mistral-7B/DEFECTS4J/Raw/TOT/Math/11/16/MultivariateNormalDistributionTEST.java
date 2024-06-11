package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link MultivariateNormalDistribution}
 */
public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution dist;
    private static final double EPSILON = 1e-12;
    private static final RandomGenerator rng = new Well19937c();

    @Before
    public void setUp() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{
                {1.0, 0.5},
                {0.5, 1.0}
        };
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of constructor with double[] means and double[][] covariances.
     */
    @Test
    public void testConstruction() {
        assertEquals(dist.getDimension(), 2);
        assertArrayEquals(dist.getMeans(), means, EPSILON);
        RealMatrix covMatrix = dist.getCovariances();
        assertArrayEquals(covMatrix.getData(), covariances, EPSILON);
    }

    /**
     * Test of getMeans() method.
     */
    @Test
    public void testGetMeans() {
        assertArrayEquals(dist.getMeans(), means, EPSILON);
    }

    /**
     * Test of getCovariances() method.
     */
    @Test
    public void testGetCovariances() {
        RealMatrix covMatrix = dist.getCovariances();
        assertArrayEquals(covMatrix.getData(), covariances, EPSILON);
    }

    /**
     * Test of density(double[]) method.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[]{1.0, 1.0};
        double expectedDensity = FastMath.exp(-0.5);
        double[] means = dist.getMeans();
        double[] covariances = dist.getCovariances().getData();
        Covariance cov = new Covariance();
        RealMatrix invCov = dist.getCovariances().getInverse();

        double actualDensity = dist.density(testValues);

        assertEquals(expectedDensity, actualDensity, EPSILON);

        double[] values = new double[]{means[0] + FastMath.sqrt(covariances[0][0]) * 2.5,
                means[1] + FastMath.sqrt(covariances[1][1]) * 2.5};
        double actualDensity2 = dist.density(values);

        // Expected density for a multivariate normal distribution
        // is the product of the densities of each component
        double productOfDensities = FastMath.pow(2 * FastMath.PI, -means.length / 2)
                * FastMath.pow(FastMath.E, -0.5 * (cov.cov(values, values) - FastMath.LOG_E));

        assertEquals(productOfDensities, actualDensity2, EPSILON);
    }

    /**
     * Test of sample() method.
     */
    @Test
    public void testSample() {
        double[] testValues = dist.sample();
        double[] expectedValues = new double[]{means[0] + dist.getStandardDeviations()[0] * FastMath.random(),
                means[1] + dist.getStandardDeviations()[1] * FastMath.random()};

        assertArrayEquals(testValues, expectedValues, EPSILON);
    }

    /**
     * Test of getStandardDeviations() method.
     */
    @Test
    public void testGetStandardDeviations() {
        double[] standardDeviations = dist.getStandardDeviations();
        double[] covariances = dist.getCovariances().getData();

        for (int i = 0; i < standardDeviations.length; i++) {
            assertEquals(FastMath.sqrt(covariances[i][i]), standardDeviations[i], EPSILON);
        }
    }
}