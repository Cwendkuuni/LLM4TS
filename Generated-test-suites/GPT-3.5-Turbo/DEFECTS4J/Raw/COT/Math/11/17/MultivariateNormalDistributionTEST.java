import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private static MultivariateNormalDistribution mvnd;

    @BeforeClass
    public static void setup() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] expected = {0.0, 0.0};
        double[] actual = mvnd.getMeans();
        Assert.assertArrayEquals(expected, actual, 0.0001);
    }

    @Test
    public void testGetCovariances() {
        double[][] expected = {{1.0, 0.0}, {0.0, 1.0}};
        double[][] actual = mvnd.getCovariances().getData();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testDensity() {
        double[] vals = {0.0, 0.0};
        double expected = 0.1591549430918953;
        double actual = mvnd.density(vals);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1.0, 1.0};
        double[] actual = mvnd.getStandardDeviations();
        Assert.assertArrayEquals(expected, actual, 0.0001);
    }

    @Test
    public void testSample() {
        double[] sample = mvnd.sample();
        Assert.assertNotNull(sample);
    }

    @Test
    public void testGetExponentTerm() {
        double[] values = {0.0, 0.0};
        double expected = 1.0;
        double actual = mvnd.getExponentTerm(values);
        Assert.assertEquals(expected, actual, 0.0001);
    }
}