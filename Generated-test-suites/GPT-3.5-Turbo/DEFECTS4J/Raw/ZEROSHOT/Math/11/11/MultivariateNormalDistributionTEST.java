import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] actual = distribution.getMeans();
        double[] expected = new double[]{1.0, 2.0, 3.0};

        assertArrayEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[] means = new double[]{1.0, 2.0, 3.0};
        double[][] covariances = new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        RealMatrix actual = distribution.getCovariances();
        RealMatrix expected = new Array2DRowRealMatrix(covariances);

        assertEquals(expected, actual);
    }

    @Test
    public void testDensity() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] values = new double[]{1.0, 2.0};

        double actual = distribution.density(values);
        double expected = 0.15915494309189535;

        assertEquals(expected, actual, 1e-10);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] actual = distribution.getStandardDeviations();
        double[] expected = new double[]{1.0, 1.0};

        assertArrayEquals(expected, actual, 1e-10);
    }

    @Test
    public void testSample() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] actual = distribution.sample();

        assertTrue(actual[0] >= -10 && actual[0] <= 10);
        assertTrue(actual[1] >= -10 && actual[1] <= 10);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        double[] values = new double[]{1.0, 2.0};

        double actual = distribution.getExponentTerm(values);
        double expected = 1.0;

        assertEquals(expected, actual, 1e-10);
    }
}