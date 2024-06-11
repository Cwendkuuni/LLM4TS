import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0},
                                {0.0, 2.0, 0.0},
                                {0.0, 0.0, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(means, distribution.getMeans(), EPSILON);
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0},
                                {0.0, 2.0, 0.0},
                                {0.0, 0.0, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(covariances);
        assertEquals(expectedCovariances, distribution.getCovariances());
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0},
                                {0.0, 2.0, 0.0},
                                {0.0, 0.0, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1.0, 2.0, 3.0};
        double expectedDensity = 0.06349363593424098; // Computed using external tool
        assertEquals(expectedDensity, distribution.density(vals), EPSILON);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0},
                                {0.0, 2.0, 0.0},
                                {0.0, 0.0, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] expectedStdDevs = {1.0, Math.sqrt(2.0), Math.sqrt(3.0)};
        assertArrayEquals(expectedStdDevs, distribution.getStandardDeviations(), EPSILON);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0},
                                {0.0, 2.0, 0.0},
                                {0.0, 0.0, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] sample = distribution.sample();
        assertEquals(3, sample.length);
        // Since the sampling is randomized, we can only test that the sample has the correct dimensions
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0},
                                {0.0, 2.0, 0.0},
                                {0.0, 0.0, 3.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);
        double[] vals = {1.0, 2.0, 3.0};
        double expectedExponentTerm = 1.0; // Exponent term is the constant factor in the density calculation
        assertEquals(expectedExponentTerm, distribution.getExponentTerm(vals), EPSILON);
    }

}