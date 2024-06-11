import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultivariateNormalDistributionTest {

 @Test
 public void testMultivariateNormalDistribution() {
 final double[] means = {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 assertEquals(means.length, dist.getDimension());
 assertArrayEquals(means, dist.getMeans());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() {
 final double[] means = {1.0};
 final double[][] covariances = {{1.0}, {1.0, 2.0}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 }

 @Test(expected = SingularMatrixException.class)
 public void testSingularMatrixException() {
 final double[] means = {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 0.5}};
 MultivariateNormalDistribution dist = null;
 try {
 dist = new MultivariateNormalDistribution(new RandomGenerator(), means, covariances);
 } catch (SingularMatrixException e) {
 // expected
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteMatrixException() {
 final double[] means = {1.0, 2.0};
 final double[][] covariances = {{1.0, -0.5}, {-0.5, 2.0}};
 MultivariateNormalDistribution dist = null;
 try {
 dist = new MultivariateNormalDistribution(new RandomGenerator(), means, covariances);
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testDensity() {
 final double[] means = {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 double[] vals = new double[]{1.5, 3.5};
 double result = dist.density(vals);
 assertEquals(1.135622786124127E-6, result, 1e-7);
 }

 @Test
 public void testSample() {
 final double[] means = {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 double[] sample = dist.sample();
 assertArrayEquals(new double[]{2.147037265381366, 4.478512834899125}, sample);
 }

 @Test
 public void testGetStandardDeviations() {
 final double[] means = {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 double[] std = dist.getStandardDeviations();
 assertArrayEquals(new double[]{1.0, 1.4142135623730955}, std);
 }

 @Test
 public void testGetMeans() {
 final double[] means = {1.0, 2.0};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, null);
 double[] result = dist.getMeans();
 assertArrayEquals(means, result);
 }

 @Test
 public void testGetCovariances() {
 final double[] means = {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 RealMatrix result = dist.getCovariances();
 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 2.0}});
 assertArrayEquals(expected.getData(), result.getData());
 }
}