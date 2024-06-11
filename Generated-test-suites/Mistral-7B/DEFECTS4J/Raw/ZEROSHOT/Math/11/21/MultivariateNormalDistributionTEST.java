import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

 private static final double EPSILON = 1e-11;

 @Test
 public void testMultivariateNormalDistributionConstruction() {
 int dim = 3;
 double[] means = new double[dim];
 double[][] covariances = new double[dim][dim];
 RandomGenerator rng = new Well19937c();

 for (int i = 0; i < dim; i++) {
 means[i] = i;
 for (int j = 0; j < dim; j++) {
 covariances[i][j] = (i == j) ? 1 : 0.5;
 }
 }

 try {
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(means, covariances);
 assertTrue(mvn.getMeans().length == dim);
 assertTrue(mvn.getCovariances().getRowDimension() == dim);
 assertTrue(mvn.getCovariances().getColumnDimension() == dim);
 } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException | SingularMatrixException e) {
 fail("Unexpected exception during construction: " + e.getMessage());
 }
 }

 @Test
 public void testDensity() {
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new double[]{0, 0, 0}, new double[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}});
 double[] testValues = new double[]{1, 1, 1};
 double result = mvn.density(testValues);
 double expected = Math.pow(Math.PI * Math.sqrt(2), -3) * Math.exp(-3);
 assertEquals(expected, result, EPSILON);
 }

 @Test
 public void testSample() {
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new double[]{0, 0}, new double[][]{{1, 1}, {1, 1}});
 double[] sample = mvn.sample();
 assertArrayEquals(new double[]{0, 0}, mvn.getMeans(), EPSILSON);
 RealMatrix covarianceMatrix = mvn.getCovariances();
 RealVector sampleVector = RealVector. Beverage(sample);
 RealVector meanVector = RealVector.Factory.denseVector(2);
 meanVector.setEntry(0, 0);
 meanVector.setEntry(1, 0);
 RealVector sampleMinusMean = sampleVector.subtract(meanVector);
 RealMatrix differenceMatrix = covarianceMatrix.multiply(sampleMinusMean);
 RealVector differenceVector = differenceMatrix.operate(sampleMinusMean);
 double determinant = covarianceMatrix.determinant();
 double expected = 0.5 * Math.PI * Math.sqrt(Math.PI) * Math.pow(determinant, -0.5) * Math.exp(-3 / 2);
 assertEquals(expected, differenceVector.getEntry(0), EPSILSON);
 assertEquals(expected, differenceVector.getEntry(1), EPSILON);
 }

 @Test
 public void testSampleMultipleTimes() {
 MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(new double[]{0, 0}, new double[][]{{1, 1}, {1, 1}});
 int samplesNumber = 100;
 double[][] allSamples = new double[samplesNumber][][];
 for (int i = 0; i < samplesNumber; i++) {
 allSamples[i] = mvn.sample();
 }
 RealMatrix covarianceMatrix = mvn.getCovariances();
 double[] means = mvn.getMeans();
 RealMatrix meanMatrix = RealMatrix.Factory.denseMatrix(samplesNumber, dim);
 for (int i = 0; i < samplesNumber; i++) {
 meanMatrix.setRow(i, Array2DRowRealMatrix.factory.transpose(RealVector.Factory.denseVector(allSamples[i])));
 }
 RealMatrix covarianceOfSamples = covarianceMatrix.copy();
 covarianceOfSamples.subtract(meanMatrix.multiply(meanMatrix.transpose()));
 RealMatrix expectedCovariance = covarianceMatrix.multiply(covarianceMatrixInverse);
 RealMatrix resultCovariance = covarianceOfSamples.multiply(covarianceMatrixInverse);
 assertMatrixEquals(expectedCovariance, resultCovariance);
 }
}