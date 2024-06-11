import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

 private static final double EPSILON = 1e-12;

 @Test
 public void testMultivariateNormalDistribution() {
 // Test with correct parameters
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
 assertEquals(2, mnd.getDimension());
 assertEquals(means, mnd.getMeans());

 RealMatrix covarianceMatrix = mnd.getCovariances();
 assertEquals(2, covarianceMatrix.getRowDimension());
 assertEquals(2, covarianceMatrix.getColumnDimension());
 assertEquals(2.0, covarianceMatrix.getEntry(0, 0), EPSILON);
 assertEquals(0.5, covarianceMatrix.getEntry(0, 1), EPSILON);
 assertEquals(0.5, covarianceMatrix.getEntry(1, 0), EPSILON);
 assertEquals(1.0, covarianceMatrix.getEntry(1, 1), EPSILON);

 // Test density
 double[] testValues = {1.5, 2.5};
 assertEquals(-0.3214628533548856, mnd.logDensity(testValues), EPSILON);

 // Test sample
 double[] sample = mnd.sample();
 assertEquals(2, sample.length);
 assertArrayEquals(testValues, new double[] {1.5 + means[0], 2.5 + means[1]}, EPSILON);

 // Test standard deviations
 double[] stdDeviations = mnd.getStandardDeviations();
 assertEquals(1.0, stdDeviations[0], EPSILON);
 assertEquals(1.0, stdDeviations[1], EPSILON);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIncorrectMeansLength() {
 double[] means = {1.0};
 double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 try {
 MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
 fail();
 } catch (DimensionMismatchException e) {
 // expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIncorrectCovariancesRows() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.0}, {0.5, 1.0}};
 try {
 MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
 fail();
 } catch (DimensionMismatchException e) {
 // expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIncorrectCovariancesColumns() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.0, 0.5}, {0.5}};
 try {
 MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
 fail();
 } catch (DimensionMismatchException e) {
 // expected
 }
 }

 @Test(expected = SingularMatrixException.class)
 public void testSingularCovarianceMatrix() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
 try {
 MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
 fail();
 } catch (SingularMatrixException e) {
 // expected
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteCovarianceMatrix() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.0, 0.0}, {0.0, -1.0}};
 try {
 MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
 fail();
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }
}