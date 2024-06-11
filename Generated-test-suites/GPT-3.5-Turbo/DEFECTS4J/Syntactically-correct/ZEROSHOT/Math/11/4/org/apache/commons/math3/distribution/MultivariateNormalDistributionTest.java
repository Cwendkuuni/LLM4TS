import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setUp() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] means = distribution.getMeans();
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, means, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix covMatrix = distribution.getCovariances();
        Assert.assertEquals(new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        }), covMatrix);
    }

    @Test
    public void testDensity() {
        double[] values = {1.0, 2.0, 3.0};
        double density = distribution.density(values);
        Assert.assertEquals(0.06349363593424097, density, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] stdDeviations = distribution.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, stdDeviations, 0.0001);
    }

    @Test
    public void testSample() {
        double[] sample = distribution.sample();
        Assert.assertNotNull(sample);
        Assert.assertEquals(3, sample.length);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {1.0, 2.0, 3.0};
        double exponentTerm = distribution.getExponentTerm(values);
        Assert.assertEquals(0.011109, exponentTerm, 0.0001);
    }
}