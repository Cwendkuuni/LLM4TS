import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultivariateNormalDistributionTest {

 private static final double EPSILON = 1.0e-14;

 @Test
 public void testMultivariateNormalDistribution() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
 // Test with identity covariance matrix.
 double[] means = new double[]{0.0, 0.0};
 double[] covariances = new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 };
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 // Test means.
 assertEquals(means[0], dist.getMeans()[0], EPSILON);
 assertEquals(means[1], dist.getMeans()[1], EPSILON);
 // Test covariance matrix.
 RealMatrix mat = dist.getCovariances();
 RealMatrix expected = new Array2DRowRealMatrix(new RealArray2D.Double(2, 2, new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 }));
 assertEquals(expected, mat);
 // Test standard deviations.
 double[] std = dist.getStandardDeviations();
 assertEquals(1.0, std[0], EPSILON);
 assertEquals(1.0, std[1], EPSILON);
 // Test density.
 double[] values = new double[]{1.0, 1.0};
 double density = dist.density(values);
 double expectedDensity = 0.1253314135451262;
 assertEquals(expectedDensity, density, EPSILON);
 // Test sample.
 double[] sample = dist.sample();
 assertArrayEquals(values, sample, EPSILON);
 }

 @Test
 public void testMultivariateNormalDistributionSingular() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
 // Test with singular covariance matrix.
 double[] means = new double[]{0.0, 0.0};
 double[] covariances = new double[][]{
 {1.0, 0.0},
 {0.0, 0.0}
 };
 try {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 fail("Should have thrown SingularMatrixException");
 } catch (SingularMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testMultivariateNormalDistributionNonPositiveDefinite() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
 // Test with non-positive definite covariance matrix.
 double[] means = new double[]{0.0, 0.0};
 double[] covariances = new double[][]{
 {1.0, -1.0},
 {-1.0, 1.0}
 };
 try {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 fail("Should have thrown NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testMultivariateNormalDistributionDensity() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
 // Test density with different values.
 double[] means = new double[]{0.0, 0.0};
 double[] covariances = new double[][]{
 {1.0, 0.5},
 {0.5, 1.0}
 };
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 double[] values1 = new double[]{1.0, 1.0};
 double density1 = dist.density(values1);
 double expected1 = 0.2783836319664484;
 assertEquals(expected1, density1, EPSILON);
 double[] values2 = new double[]{-1.0, 1.0};
 double density2 = dist.density(values2);
 double expected2 = 0.1336590889724737;
 assertEquals(expected2, density2, EPSILON);
 double[] values3 = new double[]{1.5, 1.5};
 double density3 = dist.density(values3);
 double expected3 = 0.01177822642997225;
 assertFalse(Double.isFinite(density3));
 }

 @Test
 public void testMultivariateNormalDistributionSample() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
 // Test sample.
 double[] means = new double[]{0.0, 0.0};
 double[] covariances = new double[][]{
 {1.0, 0.5},
 {0.5, 1.0}
 };
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 double[] sample = dist.sample();
 double[] expected = new double[]{1.232298978126422, 0.8416150645838172};
 assertArrayEquals(expected, sample, EPSILON);
 }
}