import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-7;

    private MultivariateNormalDistribution normalDistribution;

    @Before
    public void setup() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        try {
            normalDistribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException |
                NonPositiveDefiniteMatrixException e) {
            fail("Failed to create MultivariateNormalDistribution: " + e.getMessage());
        }
    }

    @Test
    public void testGetMeans() {
        assertArrayEquals(new double[]{1.0, 2.0}, normalDistribution.getMeans(), EPSILON);
    }

    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = {{1.0, 0.5}, {0.5, 2.0}};
        double[][] actualCovariances = normalDistribution.getCovariances().getData();

        for (int i = 0; i < expectedCovariances.length; i++) {
            assertArrayEquals(expectedCovariances[i], actualCovariances[i], EPSILON);
        }
    }

    @Test
    public void testDensity() {
        double[] values = {1.0, 2.0};
        double expectedDensity = (1 / (2 * Math.PI)) * Math.exp(-1.2125809172970674);
        double actualDensity;

        try {
            actualDensity = normalDistribution.density(values);
            assertEquals(expectedDensity, actualDensity, EPSILON);
        } catch (DimensionMismatchException e) {
            fail("Failed to compute density: " + e.getMessage());
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {1.0, Math.sqrt(2.0)};
        double[] actualStandardDeviations = normalDistribution.getStandardDeviations();

        assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, EPSILON);
    }

    @Test
    public void testSample() {
        double[] sample = normalDistribution.sample();

        assertEquals(2, sample.length);
    }

    @Test
    public void testConstructorWithInvalidMeans() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        try {
            normalDistribution = new MultivariateNormalDistribution(means, covariances);
            fail("Failed to throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("Dimension mismatch: expected 1, got 2", e.getMessage());
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail("Wrong exception thrown: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testConstructorWithInvalidCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5, 0.2}, {0.5, 2.0, 1.0}};

        try {
            normalDistribution = new MultivariateNormalDistribution(means, covariances);
            fail("Failed to throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("Dimension mismatch: expected 2, got 3", e.getMessage());
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            fail("Wrong exception thrown: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testConstructorWithSingularCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 2.0}};

        try {
            normalDistribution = new MultivariateNormalDistribution(means, covariances);
            fail("Failed to throw SingularMatrixException");
        } catch (SingularMatrixException e) {
            assertEquals("Singular matrix", e.getMessage());
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            fail("Wrong exception thrown: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testConstructorWithNonPositiveDefiniteCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, -2.0}};

        try {
            normalDistribution = new MultivariateNormalDistribution(means, covariances);
            fail("Failed to throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("Matrix has negative eigenvalue at index 1: -0.20710678118654752", e.getMessage());
        } catch (DimensionMismatchException | SingularMatrixException e) {
            fail("Wrong exception thrown: " + e.getClass().getSimpleName());
        }
    }

}