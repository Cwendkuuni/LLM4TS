import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultivariateNormalDistributionTest {

 @Test
 public void testCreateDistribution() throws Exception {
 double[] means = new double[]{1.0, 2.0};
 double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
 assertEquals(means.length, mvn.getDimension());
 assertEquals(means, mvn.getMeans());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testCreateDistributionDimensionMismatch() throws Exception {
 double[] means = new double[]{1.0, 2.0};
 double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0, 1.0}};
 new MultivariateNormalDistribution(means, covariances);
 }

 @Test(expected = SingularMatrixException.class)
 public void testCreateDistributionSingularMatrix() throws Exception {
 double[] means = new double[]{1.0, 2.0};
 double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
 new MultivariateNormalDistribution(means, covariances);
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testCreateDistributionNonPositiveDefiniteMatrix() throws Exception {
 double[] means = new double[]{1.0, 2.0};
 double[][] covariances = new double[][]{{-1.0, 0.5}, {0.5, -1.0}};
 new MultivariateNormalDistribution(means, covariances);
 }

 @Test
 public void testGetStandardDeviations() throws Exception {
 double[] means = new double[]{1.0, 2.0};
 double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
 double[] std = mvn.getStandardDeviations();
 assertArrayEquals(new double[]{FastMath.sqrt(1.0), FastMath.sqrt(0.5)}, std, 0.0);
 }

 @Test
 public void testDensity() throws Exception {
 double[] means = new double[]{1.0, 2.0};
 double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
 double[] testValues = new double[]{1.5, 2.5};
 double density = mvn.density(testValues);
 assertEquals(0.2398412375197655, density, 0.0001);
 }

 @Test
 public void testSample() throws Exception {
 double[] means = new double[]{1.0, 2.0};
 double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
 RandomGenerator rng = mvn.getRandomGenerator();
 double[] sample = mvn.sample();
 assertArrayEquals(new double[]{means[0] + samplingMatrix.getEntry(0, 0) * rng.nextGaussian(),
 means[1] + samplingMatrix.getEntry(1, 0) * rng.nextGaussian()}, sample, 0.0);
 }

 @Test
 public void testSampleMultipleTimes() throws Exception {
 double[] means = new double[]{1.0, 2.0};
 double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
 RandomGenerator rng = mvn.getRandomGenerator();
 int numSamples = 1000;
 double[] allSamples = new double[numSamples * 2];
 for (int i = 0; i < numSamples; i++) {
 allSamples[i * 2] = mvn.sample()[0];
 allSamples[i * 2 + 1] = mvn.sample()[1];
 }
 RealMatrix allSamplesMatrix = new Array2DRowRealMatrix(numSamples, 2);
 allSamplesMatrix.setColumnVector(0, RealVector.BEANUT_VECTOR, allSamples, 0, 2);
 double[] sampleMeans = new double[]{means[0], means[1]};
 RealVector sampleMeansVector = RealVector.BEANUT_VECTOR.createVector(sampleMeans);
 RealMatrix sampleCovarianceMatrix = mvn.getCovariances();
 RealMatrix sampleCovarianceMatrixInverse = mvn.getCovarianceMatrixInverse();
 RealMatrix sampleSamplingMatrix = mvn.getSamplingMatrix();
 EigenDecomposition eigenDecomposition = new EigenDecomposition(sampleCovarianceMatrix);
 RealMatrix sampleCovarianceMatrixEigenvectors = eigenDecomposition.getEigenvectors();
 RealMatrix sampleCovarianceMatrixEigenvectorsTranspose = sampleCovarianceMatrixEigenvectors.transpose();
 RealMatrix sampleCovarianceMatrixEigenvectorsSquareRoot = new Array2DRowRealMatrix(sampleCovarianceMatrix.getRowDimension(), sampleCovarianceMatrix.getColumnDimension());
 for (int row = 0; row < sampleCovarianceMatrix.getRowDimension(); row++) {
 double factor = FastMath.sqrt(eigenDecomposition.getRealEigenvalues()[row]);
 for (int col = 0; col < sampleCovarianceMatrix.getColumnDimension(); col++) {
 sampleCovarianceMatrixEigenvectorsSquareRoot.setEntry(row, col, factor);
 }
 }
 RealMatrix sampleSamplingMatrixTimesEigenvectorsSquareRoot = sampleSamplingMatrix.multiply(sampleCovarianceMatrixEigenvectorsSquareRoot);
 RealMatrix sampleMeanCentered = allSamplesMatrix.subtract(sampleMeansVector.toMatrix());
 RealMatrix sampleCenteredTimesInverse = sampleCovarianceMatrixInverse.multiply(sampleMeanCentered);
 RealMatrix product = sampleSamplingMatrixTimesEigenvectorsSquareRoot.multiply(sampleMeanCentered);
 RealMatrix exponent = new Array2DRowRealMatrix(numSamples, 1);
 for (int i = 0; i < numSamples; i++) {
 double[] sample = mvn.sample();
 exponent.setEntry(i, 0, -0.5 * product.getEntry(i, 0) * sample[0] + 0.5 * sampleCenteredTimesInverse.getEntry(i, 0) * sample[0] * sample[0]);
 exponent.setEntry(i, 1, -0.5 * product.getEntry(i, 1) * sample[1] + 0.5 * sampleCenteredTimesInverse.getEntry(i, 1) * sample[1] * sample[1]);
 }
 RealMatrix exponentMatrix = new Array2DRowRealMatrix(numSamples, 1).add(exponent);
 RealMatrix logMatrix = new Array2DRowRealMatrix(numSamples, 1).subtract(new Array2DRowRealMatrix(numSamples, 1).multiply(new Array2DRowRealMatrix(2, 1, new double[]{-0.5 * FastMath.LN_2 * 2, -0.5 * FastMath.LN_2})));
 RealMatrix result = new Array2DRowRealMatrix(numSamples, 1).subtract(new Array2DRowRealMatrix(numSamples, 1).multiply(logMatrix).add(new Array2DRowRealMatrix(numSamples, 1).exp(exponentMatrix)));
 double sum = 0;
 for (int i = 0; i < numSamples; i++) {
 sum += result.getEntry(i, 0);
 }
 assertEquals(1.0, sum, 0.01);
 }
}