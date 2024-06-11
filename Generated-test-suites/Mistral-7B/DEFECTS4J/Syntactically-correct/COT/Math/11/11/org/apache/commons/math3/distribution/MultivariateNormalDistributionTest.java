import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

 private MultivariateNormalDistribution distribution;
 private RandomGenerator random;

 @Before
 public void setUp() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 try {
 distribution = new MultivariateNormalDistribution(random, means, covariances);
 } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
 // handle exception
 }
 random = new Well19937c();
 }

 @Test
 public void testConstructorWithVectorAndMatrix() {
 // Test for constructor with vector and matrix
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 try {
 distribution = new MultivariateNormalDistribution(means, covariances);
 } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
 // handle exception
 }
 Assert.assertArrayEquals(means, distribution.getMeans());
 RealMatrix covarianceMatrix = distribution.getCovariances();
 Assert.assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
 Assert.assertEquals(covariances.length, covarianceMatrix.getRowDimension());
 }

 @Test
 public void testGetMeans() {
 // Test for getMeans() method
 double[] means = distribution.getMeans();
 Assert.assertArrayEquals(means, new double[]{1.0, 2.0});
 }

 @Test
 public void testGetCovariances() {
 // Test for getCovariances() method
 RealMatrix covarianceMatrix = distribution.getCovariances();
 Array2DRowRealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
 Assert.assertArrayEquals(expectedCovarianceMatrix.getData(), covarianceMatrix.getData());
 }

 @Test
 public void testDensity() throws DimensionMismatchException {
 // Test for density() method
 double[] values = {2.0, 3.0};
 double density = distribution.density(values);
 double expectedDensity = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(distribution.covarianceMatrixDeterminant, -0.5) * distribution.getExponentTerm(values);
 Assert.assertEquals(expectedDensity, density, 0.001);
 }

 @Test
 public void testSample() {
 // Test for sample() method
 double[] sample = distribution.sample();
 Assert.assertArrayEquals(sample.length, distribution.getDimension());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testSampleWithInvalidLength() {
 distribution.sample(2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDensityWithInvalidLength() {
 distribution.density(new double[]{1.0, 2.0, 3.0});
 }
}