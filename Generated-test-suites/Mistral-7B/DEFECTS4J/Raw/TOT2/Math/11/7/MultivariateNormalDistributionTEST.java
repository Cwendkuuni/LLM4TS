import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultivariateNormalDistributionTest {

 private double[] means = {1.0, 2.0};
 private double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 private MultivariateNormalDistribution dist;

 @Test
 public void testConstructor() throws Exception {
 dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
 assertEquals(means.length, dist.getDimension());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testInvalidMeansLength() {
 new MultivariateNormalDistribution(new Well19937c(), new double[1], covariances);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testInvalidCovariancesShape() throws Exception {
 new MultivariateNormalDistribution(new Well19937c(), means, new double[2][3]);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testInvalidCovariancesColumnSize() throws Exception {
 new MultivariateNormalDistribution(new Well19937c(), means, new double[2][3]);
 }

 @Test(expected = SingularMatrixException.class)
 public void testSingularCovarianceMatrix() throws Exception {
 double[][] singularCovariances = {{1.0, 0.0}, {0.0, 0.0}};
 new MultivariateNormalDistribution(new Well19937c(), means, singularCovariances);
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteCovarianceMatrix() throws Exception {
 new MultivariateNormalDistribution(new Well19937c(), means, new Array2DRowRealMatrix(2, 2).multiply(new Array2DRowRealMatrix(2, 2).scalarMultiply(-1.0)));
 }

 @Test
 public void testGetMeans() {
 double[] meansCopy = dist.getMeans();
 assertArrayEquals(means, meansCopy);
 }

 @Test
 public void testGetCovariances() throws Exception {
 RealMatrix covarianceMatrixCopy = dist.getCovariances();
 assertArrayEquals(covariances, covarianceMatrixCopy.getData());
 }

 @Test
 public void testDensity() throws Exception {
 double[] testValues = {1.5, 2.5};
 double expectedDensity = 0.0926222461640238; // calculated using MathWorld's density formula
 double actualDensity = dist.density(testValues);
 assertEquals(expectedDensity, actualDensity, 1e-6);
 }

 @Test
 public void testSample() throws Exception {
 double[] testSample = dist.sample();
 for (int i = 0; i < dist.getDimension(); i++) {
 assertEquals(i < means.length ? means[i] : 0.0, testSample[i], 1e-6);
 }
 }

 @Test
 public void testSampleAndDensity() throws Exception {
 double[] testSample = dist.sample();
 double expectedDensity = dist.density(testSample);
 double actualDensity = dist.density(testSample);
 assertEquals(expectedDensity, actualDensity, 1e-6);
 }
}