import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

 private MultivariateNormalDistribution distribution;
 private RandomGenerator random;

 @Before
 public void setUp() {
 double[] means = {0.0, 1.0, 2.0};
 double[][] covariances = {
 {1.0, 0.5, 0.5},
 {0.5, 2.5, 0.5},
 {0.5, 0.5, 3.0}
 };
 try {
 distribution = new MultivariateNormalDistribution(means, covariances);
 random = distribution.getRandomGenerator();
 } catch (Exception e) {
 fail("Failed to initialize distribution.");
 }
 }

 @Test
 public void testGetMeans() {
 double[] means = distribution.getMeans();
 assertArrayEquals(means, new double[] {0.0, 1.0, 2.0});
 }

 @Test
 public void testGetCovariances() {
 RealMatrix covariances = distribution.getCovariances();
 Array2DRowRealMatrix expected = new Array2DRowRealMatrix(covariances.getData());
 Array2DRowRealMatrix actual = new Array2DRowRealMatrix(covariances.getRowDimension(), covariances.getColumnDimension());
 for (int i = 0; i < covariances.getRowDimension(); i++) {
 for (int j = 0; j < covariances.getColumnDimension(); j++) {
 actual.setEntry(i, j, covariances.getEntry(i, j));
 }
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testDensity() {
 double[] testValues = {0.5, 1.5, 2.5};
 double density = distribution.density(testValues);
 double expectedDensity = FastMath.pow(2 * FastMath.PI, -3.0 / 2) * FastMath.pow(distribution.covarianceMatrixDeterminant, -0.5) * distribution.getExponentTerm(testValues);
 assertEquals(expectedDensity, density, 1e-6);
 }

 @Test
 public void testSample() {
 double[] sample = distribution.sample();
 for (int i = 0; i < distribution.getDimension(); i++) {
 assertEquals(i < sample.length ? sample[i] : 0.0, distribution.getMeans()[i], 1e-6);
 }
 }

 @Test
 public void testExponentTerm() {
 double[] values = {1.0, 2.0, 3.0};
 double exponentTerm = distribution.getExponentTerm(values);
 double[] centeredValues = new double[values.length];
 for (int i = 0; i < values.length; i++) {
 centeredValues[i] = values[i] - distribution.getMeans()[i];
 }
 double[] preMultiplied = new double[covarianceMatrixInverse.getRowDimension()];
 covarianceMatrixInverse.operate(centeredValues, preMultiplied, 0);
 double sum = 0;
 for (int i = 0; i < preMultiplied.length; i++) {
 sum += preMultiplied[i] * centeredValues[i];
 }
 double expectedExponentTerm = FastMath.exp(-0.5 * sum);
 assertEquals(expectedExponentTerm, exponentTerm, 1e-6);
 }

}