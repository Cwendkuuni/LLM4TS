import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

 private RandomGenerator rng;
 private MultivariateNormalDistribution dist;

 @Before
 public void setUp() {
 rng = new Well19937c();
 double[] means = new double[]{0.0, 0.0};
 double[][] covariances = new double[][]{
 new double[]{1.0, 0.5},
 new double[]{0.5, 1.0}
 };
 try {
 dist = new MultivariateNormalDistribution(rng, means, covariances);
 } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
 e.printStackTrace();
 Assert.fail();
 }
 }

 @Test
 public void testGetMeans() {
 double[] result = dist.getMeans();
 Assert.assertArrayEquals(means, result);
 }

 @Test
 public void testGetCovariances() {
 RealMatrix result = dist.getCovariances();
 RealMatrix expected = new Array2DRowRealMatrix(covariances);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testSample() {
 double[] result = dist.sample();
 Assert.assertArrayEquals(result.length, 2, "Sample array has incorrect length.");
 for (int i = 0; i < result.length; i++) {
 Assert.assertTrue(result[i] >= -10.0 && result[i] <= 10.0, "Sample value out of bounds.");
 }
 }

 @Test
 public void testDensity() {
 double[] values = new double[]{0.0, 0.0};
 double density = dist.density(values);
 double expected = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) * dist.getExponentTerm(values);
 Assert.assertEquals(expected, density, 1e-7);
 }

 @Test
 public void testGetStandardDeviations() {
 double[] result = dist.getStandardDeviations();
 double[] expected = new double[]{FastMath.sqrt(covariances[0][0]), FastMath.sqrt(covariances[1][1])};
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testSampleMatrix() {
 RealMatrix result = dist.sample(5, true);
 RealArray2DSample sample = new RealArray2DSample(result, 5, 2);
 RealMatrix expected = new Array2DRowRealMatrix(5, 2).mapMultiply(dist.samplingMatrix, dist.means);
 for (int i = 0; i < 5; i++) {
 RealVector row = sample.getColumnVector(i);
 Assert.assertEquals(row.getDimension(), 2);
 Assert.assertArrayEquals(means, row.toArray(), 1e-7);
 for (int j = 0; j < 2; j++) {
 double value = row.getEntry(j);
 Assert.assertTrue(value >= -10.0 && value <= 10.0, "Sample value out of bounds.");
 }
 }
 }

 @Test
 public void testGetExponentTerm() {
 double[] values = new double[]{1.0, 1.0};
 double result = dist.getExponentTerm(values);
 double expected = FastMath.pow(values[0] - means[0], 2) / (2 * covariances[0][0]) + FastMath.pow(values[1] - means[1], 2) / (2 * covariances[1][1]);
 Assert.assertEquals(expected, result, 1e-7);
 }
}