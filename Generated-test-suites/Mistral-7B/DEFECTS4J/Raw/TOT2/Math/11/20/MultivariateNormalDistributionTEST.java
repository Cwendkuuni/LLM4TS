import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributiontest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

 @Test
 public void testConstructorWithVectorAndMatrix() {
 MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0, 3.0}, new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 assertEquals(3, mvd.getDimension());
 assertArrayEquals(new double[]{1.0, 2.0, 3.0}, mvd.getMeans(), 0.001);

 RealMatrix covarianceMatrix = mvd.getCovariances();
 assertArrayEquals(new double[][]{
 new double[]{1.0, 0.5, 0.5},
 new double[]{0.5, 1.0, 0.5},
 new double[]{0.5, 0.5, 1.0}
 }, covarianceMatrix.getData(), 0.001);
 }

 @Test
 public void testDensity() {
 MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0, 3.0}, new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 double expectedDensity = 0.12762629;
 double actualDensity = mvd.density(new double[]{1.5, 2.5, 3.5});
 assertEquals(expectedDensity, actualDensity, 0.001);
 }

 @Test
 public void testSample() {
 MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0, 3.0}, new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 double[] sample = mvd.sample();
 assertArrayEquals(new double[]{3.1522684, 2.1762148, 3.211586}, sample, 0.001);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDensityWithInvalidDimension() {
 MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0, 3.0}, new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}});
 double[] invalidValues = new double[]{1.0, 2.0, 3.0, 4.0};
 mvd.density(invalidValues);
 }

 @Test(expected = SingularMatrixException.class)
 public void testSingularMatrixException() {
 double[][] covariances = new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 0.0}};
 try {
 new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0, 3.0}, covariances);
 fail("Should have thrown SingularMatrixException");
 } catch (SingularMatrixException e) {
 // Expected exception
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteMatrixException() {
 double[][] covariances = new double[][]{{1.0, -0.5, 0.5}, {-0.5, 1.0, 0.5}, {0.5, 0.5, -1.0}};
 try {
 new MultivariateNormalDistribution(new Well19937c(), new double[]{1.0, 2.0, 3.0}, covariances);
 fail("Should have thrown NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }
}