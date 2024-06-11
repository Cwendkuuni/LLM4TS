import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateRealDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test constructor with correct parameters.
     */
    @Test
    public void testConstructorWithCorrectParameters() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        Assert.assertArrayEquals(means, dist.getMeans());
        Assert.assertEquals(covariances.length, dist.getCovariances().getRowDimension());
        Assert.assertEquals(covariances[0].length, dist.getCovariances().getColumnDimension());
    }

    /**
     * Test constructor with incorrect parameters.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithIncorrectParameters() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0, 0.5}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test getMeans method.
     */
    @Test
    public void testGetMeans() {
        double[] means = new double[]{1.0, 2.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        Assert.assertArrayEquals(means, dist.getMeans());
    }

    /**
     * Test getCovariances method.
     */
    @Test
    public void testGetCovariances() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        RealMatrix cov = dist.getCovariances();
        Array2DRowRealMatrix covCopy = new Array2DRowRealMatrix(cov.getData());
        Assert.assertEquals(covCopy.getRowDimension(), covCopy.getColumnDimension());
        Assert.assertArrayEquals(cov.getData(), covariances);
    }

    /**
     * Test density method with correct values.
     */
    @Test
    public void testDensityWithCorrectValues() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = new double[]{1.5, 2.5};
        double expectedResult = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(dist.getCovariances().determinant(), -0.5) * dist.getExponentTerm(testValues);
        double actualResult = dist.density(testValues);
        Assert.assertEquals(expectedResult, actualResult, 0.001);
    }

    /**
     * Test density method with incorrect values.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithIncorrectValues() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] testValues = new double[]{1.0, 2.0, 3.0};
        dist.density(testValues);
    }

    /**
     * Test sample method with correct distribution.
     */
    @Test
    public void testSampleWithCorrectDistribution() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] expectedMeans = dist.getMeans();
        double[] actualSample = dist.sample();
        for (int i = 0; i < actualSample.length; i++) {
            Assert.assertEquals(expectedMeans[i], actualSample[i], 0.01);
        }
    }

    /**
     * Test sample method with wrong distribution.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSampleWithWrongDistribution() {
        double[] means = new double[]{1.0, 2.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        dist.sample(3);
    }
}