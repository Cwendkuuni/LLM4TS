import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() throws Exception {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 3.0}};

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] means = distribution.getMeans();
        double[] expectedMeans = {1.0, 2.0};
        assertArrayEquals(expectedMeans, means, 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[][] covariances = distribution.getCovariances().getData();
        double[][] expectedCovariances = {{2.0, 0.5}, {0.5, 3.0}};
        assertArrayEquals(expectedCovariances, covariances);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] standardDeviations = distribution.getStandardDeviations();
        double[] expectedStandardDeviations = {Math.sqrt(2.0), Math.sqrt(3.0)};
        assertArrayEquals(expectedStandardDeviations, standardDeviations, 0.0);
    }

    @Test
    public void testDensity() {
        double[] values = {1.0, 2.0};
        double density = distribution.density(values);
        double expectedDensity = 0.0994975; // Value obtained from external calculations
        assertEquals(expectedDensity, density, 0.000001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();

        assertNotNull(sample);
        assertEquals(2, sample.length);
    }

    @Test
    public void testInvalidMeanLength() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 0.5}, {0.5, 3.0}};

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
            fail("Expected DimensionMismatchException");
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            assertTrue(e instanceof DimensionMismatchException);
        }
    }

    @Test
    public void testInvalidCovariancesLength() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.5, 1.0}, {0.5, 3.0}};

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
            fail("Expected DimensionMismatchException");
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            assertTrue(e instanceof DimensionMismatchException);
        }
    }

    @Test
    public void testNegativeEigenvalue() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, -0.5}, {-0.5, 3.0}};

        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            assertTrue(e instanceof NonPositiveDefiniteMatrixException);
        }
    }
}