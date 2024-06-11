import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class MultivariateNormalDistributionTest {

 @Test
 public void testMultivariateNormalDistribution() {
 final double[] means = {0.0, 0.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

 try {
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
 assertEquals(means.length, mvn.getDimension());
 assertEquals(means, mvn.getMeans());
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testDensity() {
 final double[] means = {0.0, 0.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn;
 try {
 mvn = new MultivariateNormalDistribution(means, covariances);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }

 double[] testValues = new double[]{0.0, 0.0};
 double expectedDensity = 1.0 / (2 * Math.PI * Math.sqrt(2)) * Math.exp(-0.5);
 assertEquals("Density should be correct for mean values.", expectedDensity, mvn.density(testValues), 1e-10);
 }

 @Test
 public void testSample() {
 final double[] means = {0.0, 0.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn;
 try {
 mvn = new MultivariateNormalDistribution(means, covariances);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }

 double[] sample = mvn.sample();
 assertArrayEquals("Sample should have the same length as the number of dimensions.", means.length, sample.length);
 }

 @Test
 public void testGetStandardDeviations() {
 final double[] means = {0.0, 0.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn;
 try {
 mvn = new MultivariateNormalDistribution(means, covariances);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }

 double[] stdDeviations = mvn.getStandardDeviations();
 assertArrayEquals("Standard Deviations should have the same length as the number of dimensions.", means.length, stdDeviations.length);
 }

 @Test
 public void testGetCovariances() {
 final double[] means = {0.0, 0.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn;
 RealMatrix covarianceMatrix;
 try {
 mvn = new MultivariateNormalDistribution(means, covariances);
 covarianceMatrix = mvn.getCovariances();
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }

 RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
 assertEquals("Covariance matrices should be equal.", expectedCovarianceMatrix, covarianceMatrix);
 }

 @Test
 public void testNegativeCovarianceMatrix() {
 final double[] means = {0.0, 0.0};
 final double[][] covariances = {{1.0, -0.5}, {-0.5, 1.0}};
 try {
 new MultivariateNormalDistribution(means, covariances);
 fail("A negative covariance matrix shouldn't be accepted.");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }
}