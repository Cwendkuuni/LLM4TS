import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] expectedMeans = {1, 2, 3};
        double[] actualMeans = distribution.getMeans();
        Assert.assertArrayEquals(expectedMeans, actualMeans, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix actualCovariances = distribution.getCovariances();
        Assert.assertArrayEquals(expectedCovariances, actualCovariances.getData());
    }

    @Test
    public void testDensity() {
        double[] vals = {1, 2, 3};
        double expectedDensity = (1 / (2 * Math.PI)) * (1 / Math.sqrt(1)) * Math.exp(-0.5 * 0);
        double actualDensity = distribution.density(vals);
        Assert.assertEquals(expectedDensity, actualDensity, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expectedStandardDeviations = {1, 1, 1};
        double[] actualStandardDeviations = distribution.getStandardDeviations();
        Assert.assertArrayEquals(expectedStandardDeviations, actualStandardDeviations, 0.0001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertEquals(3, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1, 2, 3};
        double[] means = distribution.getMeans();
        double[][] covariances = distribution.getCovariances().getData();
        RealMatrix covarianceMatrixInverse = distribution.covarianceMatrixInverse;
        double expectedExponentTerm = Math.exp(-0.5 * ((values[0] - means[0]) * (values[0] - means[0]) +
                (values[1] - means[1]) * (values[1] - means[1]) +
                (values[2] - means[2]) * (values[2] - means[2])));
        double actualExponentTerm = distribution.getExponentTerm(values);
        Assert.assertEquals(expectedExponentTerm, actualExponentTerm, 0.0001);
    }
}