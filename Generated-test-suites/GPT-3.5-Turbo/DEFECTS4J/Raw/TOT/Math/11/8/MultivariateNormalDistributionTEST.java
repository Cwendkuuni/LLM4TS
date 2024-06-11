import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private static final double DELTA = Math.ulp(1d);

    private MultivariateNormalDistribution distribution;

    @Before
    public void setup() {
        double[] means = {1, 2};
        double[][] covariances = {{2, 1}, {1, 1}};
        RandomGenerator rng = new Well19937c();
        try {
            distribution = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetMeanVector() {
        double[] expected = {1, 2};
        assertArrayEquals(expected, distribution.getMeans(), DELTA);
    }

    @Test
    public void testGetCovarianceMatrix() {
        double[][] expected = {{2, 1}, {1, 1}};
        assertArrayEquals(expected, distribution.getCovariances().getData(), DELTA);
    }

    @Test
    public void testDensity() {
        double[] vals = {1, 2};
        double expected = 0.15915494309189535;
        assertEquals(expected, distribution.density(vals), DELTA);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1.4142135623730951, 1};
        assertArrayEquals(expected, distribution.getStandardDeviations(), DELTA);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        assertEquals(2, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1, 2};
        double expected = 0.3606992537169126;
        assertEquals(expected, distribution.getExponentTerm(values), DELTA);
    }

    @Test
    public void testInvalidDimensionMeans() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{2, 1}, {1, 1}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("lengths are inconsistent: 3 != 2", e.getMessage());
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testInvalidDimensionCovariances() {
        double[] means = {1, 2};
        double[][] covariances = {{2, 1, 0}, {1, 1}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("lengths are inconsistent: 2 != 3", e.getMessage());
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSingularMatrixException() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 1}, {1, 1}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            assertEquals("matrix is singular", e.getMessage());
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1, 2};
        double[][] covariances = {{-1, 1}, {1, 1}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("Eigenvalue -1.0 is negative", e.getMessage());
        } catch (DimensionMismatchException | SingularMatrixException e) {
            fail(e.getMessage());
        }
    }

}