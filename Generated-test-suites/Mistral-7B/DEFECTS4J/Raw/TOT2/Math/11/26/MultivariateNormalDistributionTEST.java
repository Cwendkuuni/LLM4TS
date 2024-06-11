import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution_BasicFunctionality() {
        // Given
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        double density = mvnd.density(new double[]{1.5, 2.5});

        // Then
        assertEquals(0.1849697736974, density, 1e-8);

        RealVector sample = mvnd.sample();
        double[] sampleValues = sample.toArray();

        // Check mean values
        for (int i = 0; i < means.length; i++) {
            assertEquals(means[i], sampleValues[i], 1e-8);
        }

        // Check covariance matrix
        RealMatrix covariance = mvnd.getCovariances();
        Array2DRowRealMatrix covarianceMatrix = (Array2DRowRealMatrix) covariance;
        RealArray2DRowVector expectedCovariance = new RealArray2DRowVector(new double[][]{
                {1.0, 0.5},
                {0.5, 1.0}});
        assertEquals(expectedCovariance, covarianceMatrix);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistribution_InvalidMeansLength() {
        double[] means = {1.0, 2.0, 3.0}; // Means should have a length equal to the number of dimensions
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistribution_InvalidCovariancesShape() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5}}; // Covariances should have a shape equal to [number of dimensions, number of dimensions]
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistribution_InvalidSampleLength() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] sample = new double[mvnd.getDimension() - 1]; // Sample should have a length equal to the number of dimensions
        mvnd.sample(sample);
    }

    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistribution_SingularCovarianceMatrix() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{0.0, 0.5}, {0.5, 0.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistribution_NonPositiveDefiniteCovarianceMatrix() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, -0.5}, {-0.5, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }
}