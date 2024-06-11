import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution mvd;

    @Before
    public void setup() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 1.0}, {1.0, 3.0}};
        mvd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testConstructor() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{2.0, 1.0}, {1.0, 3.0}};
        try {
            MultivariateNormalDistribution invalidMvd1 = new MultivariateNormalDistribution(means, covariances);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("Arrays length are inconsistent.", e.getMessage());
        }

        double[][] invalidCovariances = {{2.0, 1.0, 0.0}, {1.0, 3.0, 1.5}, {0.0, 1.5, 2.0}};
        try {
            MultivariateNormalDistribution invalidMvd2 = new MultivariateNormalDistribution(means, invalidCovariances);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("Arrays length are inconsistent.", e.getMessage());
        }

        double[][] invalidCovariances2 = {{2.0, 1.0}, {1.0, -3.0}};
        try {
            MultivariateNormalDistribution invalidMvd3 = new MultivariateNormalDistribution(means, invalidCovariances2);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("Covariance matrix is not positive definite.", e.getMessage());
        }
    }

    @Test
    public void testGetMeans() {
        double[] expectedMeans = {1.0, 2.0};
        assertArrayEquals(expectedMeans, mvd.getMeans(), 0.000001);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 3.0}});
        assertTrue(expectedCovariances.subtract(mvd.getCovariances()).isZero());
    }

    @Test
    public void testDensity() {
        double[] validValues = {1.5, 2.5};
        double expectedDensity = 0.0732921;
        assertEquals(expectedDensity, mvd.density(validValues), 0.000001);

        double[] invalidValues = {1.5, 2.5, 3.5};
        try {
            mvd.density(invalidValues);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            assertEquals("Arrays length are inconsistent.", e.getMessage());
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {1.41421356, 1.73205081};
        assertArrayEquals(expectedStandardDeviations, mvd.getStandardDeviations(), 0.000001);
    }

    @Test
    public void testSample() {
        double[] sample = mvd.sample();

        assertEquals(2, sample.length);
        assertTrue(sample[0] >= Double.NEGATIVE_INFINITY && sample[0] <= Double.POSITIVE_INFINITY);
        assertTrue(sample[1] >= Double.NEGATIVE_INFINITY && sample[1] <= Double.POSITIVE_INFINITY);
    }
}