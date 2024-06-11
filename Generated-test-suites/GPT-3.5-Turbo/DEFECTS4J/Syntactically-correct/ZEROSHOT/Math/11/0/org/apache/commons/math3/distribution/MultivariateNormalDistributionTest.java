import org.junit.Test;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.Well19937c;
import static org.junit.Assert.assertEquals;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {2.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 2.0}
        };
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] result = distribution.getMeans();

        assertEquals(means, result);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {2.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 2.0}
        };
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        RealMatrix result = distribution.getCovariances();

        assertEquals(covariances, result);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {2.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 2.0}
        };
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] values = {1.5, 2.5, 3.5};
        double result = distribution.density(values);

        assertEquals(0.0099796754, result, 1e-9);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {2.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 2.0}
        };
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] result = distribution.getStandardDeviations();

        double[] expected = {Math.sqrt(2.0), Math.sqrt(2.0), Math.sqrt(2.0)};
        assertEquals(expected, result);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {
                {1.0, 0.0},
                {0.0, 1.0}
        };
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] result = distribution.sample();

        // No assertion possible on the result as it is random
    }
}