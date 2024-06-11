import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setup() throws DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 0.0}, {0.0, 1.0}};
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] expected = {1.0, 2.0};
        double[] actual = distribution.getMeans();

        Assert.assertArrayEquals(expected, actual, 0.000001);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 1.0}});
        RealMatrix actual = distribution.getCovariances();

        Assert.assertTrue(Arrays.deepEquals(expected.getData(), actual.getData()));
    }

    @Test
    public void testDensity() throws DimensionMismatchException {
        double[] values = {1.0, 2.0};
        double expected = 0.06349364;
        double actual = distribution.density(values);

        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {Math.sqrt(2.0), 1.0};
        double[] actual = distribution.getStandardDeviations();

        Assert.assertArrayEquals(expected, actual, 0.000001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertNotNull(sample);

        Assert.assertEquals(distribution.getDimension(), sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1.0, 2.0};
        double expected = 0.06349364;
        double actual = distribution.getExponentTerm(values);

        Assert.assertEquals(expected, actual, 0.000001);
    }
}