import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.matrix.ArrayRealVector;
import org.apache.commons.math3.matrix.RealMatrix;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;


public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution multivariateNormal;

    @Before
    public void setUp() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            multivariateNormal = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    // Test density method for typical use case
    @Test
    public void testDensity_typical() {
        double[] values = {2, 2, 2};
        double expectedDensity = 0.06349363593424097;
        double actualDensity = multivariateNormal.density(values);
        assertEquals(expectedDensity, actualDensity, 1e-6);
    }

    // Test density method for edge case - values at the edges of the distribution
    @Test
    public void testDensity_edge() {
        double[] values = {0, 0, 0};
        double expectedDensity = 0.0029150244650281948;
        double actualDensity = multivariateNormal.density(values);
        assertEquals(expectedDensity, actualDensity, 1e-6);
    }

    // Test density method for error case - values of different length than means and covariances
    @Test(expected = DimensionMismatchException.class)
    public void testDensity_error() {
        double[] values = {1, 2};
        multivariateNormal.density(values);
    }

    // Test getMeans method
    @Test
    public void testGetMeans() {
        double[] expectedMeans = {1, 2, 3};
        double[] actualMeans = multivariateNormal.getMeans();
        assertArrayEquals(expectedMeans, actualMeans, 1e-6);
    }

    // Test getCovariances method
    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedCovariances);
        RealMatrix actualMatrix = multivariateNormal.getCovariances();
        assertTrue(expectedMatrix.equals(actualMatrix));
    }

    // Test getStandardDeviations method
    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {1, 1, 1};
        double[] actualStandardDeviations = multivariateNormal.getStandardDeviations();
        assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 1e-6);
    }

    // Test sample method for typical use case
    @Test
    public void testSample_typical() {
        double[] sample = multivariateNormal.sample();
        assertEquals(3, sample.length);
    }

    // Test sample method for edge case - sampling multiple times
    @Test
    public void testSample_edge() {
        int numSamples = 1000;
        double[][] samples = new double[numSamples][3];
        for (int i = 0; i < numSamples; i++) {
            samples[i] = multivariateNormal.sample();
        }
        double[] means = new double[3];
        double[][] covariances = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < numSamples; j++) {
                means[i] += samples[j][i];
            }
            means[i] /= numSamples;
            for (int j = 0; j < numSamples; j++) {
                covariances[i][i] += Math.pow(samples[j][i] - means[i], 2);
            }
            covariances[i][i] /= numSamples;
        }
        for (int i = 0; i < 3; i++) {
            assertEquals(multivariateNormal.getMeans()[i], means[i], 0.1);
            assertEquals(multivariateNormal.getCovariances().getEntry(i, i), covariances[i][i], 0.1);
        }
    }

}