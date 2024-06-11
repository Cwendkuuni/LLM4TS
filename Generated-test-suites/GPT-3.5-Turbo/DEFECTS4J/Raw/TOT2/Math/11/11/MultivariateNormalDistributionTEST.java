import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;

public class MultivariateNormalDistributionTest {

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertArrayEquals(means, distribution.getMeans(), 0.0);
    }

    @Test
    public void testGetCovariances() {
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, covariances);
        assertArrayEquals(covariances, distribution.getCovariances().getData());
    }

    @Test
    public void testDensity() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] values = {1.0, 1.0};
        assertEquals(0.05854983152431917, distribution.density(values), 1e-9);
    }

    @Test
    public void testGetStandardDeviations() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] stdDeviations = distribution.getStandardDeviations();
        assertEquals(1.0, stdDeviations[0], 0.0);
        assertEquals(1.0, stdDeviations[1], 0.0);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] sample = distribution.sample();
        assertNotNull(sample);
        assertEquals(2, sample.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        distribution.density(new double[]{1.0});
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[][] covariances = {{1.0, 2.0}, {2.0, 4.0}};
        new MultivariateNormalDistribution(new double[]{0.0, 0.0}, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[][] covariances = {{1.0, 0.0}, {0.0, -1.0}};
        new MultivariateNormalDistribution(new double[]{0.0, 0.0}, covariances);
    }
}