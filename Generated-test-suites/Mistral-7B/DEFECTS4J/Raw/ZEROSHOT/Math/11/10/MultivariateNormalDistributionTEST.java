import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        // Test with identity covariance matrix
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(2, mvnd.getDimension());
        assertEquals(means, mvnd.getMeans());
        RealMatrix covarianceMatrix = mvnd.getCovariances();
        assertEquals(1.0, covarianceMatrix.getEntry(0, 0), 1e-5);
        assertEquals(1.0, covarianceMatrix.getEntry(1, 1), 1e-5);
        assertEquals(0.0, covarianceMatrix.getEntry(0, 1), 1e-5);
        assertEquals(0.0, covarianceMatrix.getEntry(1, 0), 1e-5);

        // Test mean and standard deviation
        double[] standardDeviations = mvnd.getStandardDeviations();
        assertEquals(1.0, standardDeviations[0], 1e-5);
        assertEquals(1.0, standardDeviations[1], 1e-5);

        // Test sampling
        double[] sample = mvnd.sample();
        assertEquals(2, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertTrue(sample[i] >= -3.0 && sample[i] <= 3.0);
        }

        // Test density
        double[] testValues = new double[]{1.0, 1.0};
        double density = mvnd.density(testValues);
        double expectedDensity = 0.1353352832306055;
        assertEquals(expectedDensity, density, 1e-5);

        // Test with non-identity covariance matrix
        means = new double[]{1.0, 2.0};
        covariances = new double[][]{{2.0, 0.5}, {0.5, 3.0}};
        mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        covarianceMatrix = mvnd.getCovariances();
        assertEquals(2.0, covarianceMatrix.getEntry(0, 0), 1e-5);
        assertEquals(3.0, covarianceMatrix.getEntry(1, 1), 1e-5);
        assertEquals(0.5, covarianceMatrix.getEntry(0, 1), 1e-5);
        assertEquals(0.5, covarianceMatrix.getEntry(1, 0), 1e-5);

        // Test with negative covariance matrix
        means = new double[]{1.0, 2.0};
        covariances = new double[][]{{-1.0, 0.5}, {0.5, -3.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        // Test with singular covariance matrix
        means = new double[]{1.0, 2.0};
        covariances = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            fail();
        } catch (SingularMatrixException e) {
            // expected
        }

        // Test with inconsistent arrays
        means = new double[]{1.0};
        covariances = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] centered = new double[]{1.0, 1.0};
        double[] preMultiplied = mvnd.covarianceMatrixInverse.operate(centered);
        double sum = 0.0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        double expectedExponentTerm = Math.exp(-0.5 * sum);
        double actualExponentTerm = mvnd.getExponentTerm(centered);
        assertEquals(expectedExponentTerm, actualExponentTerm, 1e-5);
    }
}