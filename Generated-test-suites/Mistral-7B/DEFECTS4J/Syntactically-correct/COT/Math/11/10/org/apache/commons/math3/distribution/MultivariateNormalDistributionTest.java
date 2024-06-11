package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

 private final double[] means = {0.0, 1.0, 2.0};
 private final double[][] covariances = {
         {1.0, 0.5, 0.5},
         {0.5, 0.5, 0.5},
         {0.5, 0.5, 1.0}
 };
 private MultivariateNormalDistribution dist;
 private final RandomGenerator rng = new Well19937c();

 @Test
 public void testMultivariateNormalDistribution() throws Exception {
 dist = new MultivariateNormalDistribution(means, covariances);
 assertEquals(means.length, dist.getDimension());
 assertEquals(means.length, dist.getMeans().length);
 }

 @Test(expected = SingularMatrixException.class)
 public void testSingularMatrixException() throws Exception {
 dist = new MultivariateNormalDistribution(means, new double[][]{{1.0}});
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteMatrixException() throws Exception {
 dist = new MultivariateNormalDistribution(means, new double[][]{{-1.0, 0.5}, {0.5, -1.0}});
 }

 @Test
 public void testGetMeans() throws Exception {
 RealVector means = dist.getMeans();
 assertArrayEquals(means.toArray(), means, 0.001);
 }

 @Test
 public void testGetCovariances() throws Exception {
 RealMatrix covariances = dist.getCovariances();
 assertEquals(covariances.getRowDimension(), covariances.getColumnDimension(), covariances.getDimension());
 }

 @Test
 public void testDensity() throws Exception {
 double[] testValues = new double[]{0.0, 1.0, 2.0};
 double density = dist.density(testValues);
 double expectedDensity = FastMath.pow(2 * FastMath.PI, -dist.getDimension() / 2) * FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) * dist.getExponentTerm(testValues);
 assertEquals(expectedDensity, density, 0.001);
 }

 @Test
 public void testSample() throws Exception {
 double[] sample = dist.sample();
 assertArrayEquals(dist.getMeans().length, sample.length, 0.0);
 for (int i = 0; i < sample.length; i++) {
 assert(FastMath.isFinite(sample[i])) : "Sample value is NaN: " + sample[i];
 }
 }

 @Test
 public void testGetStandardDeviations() throws Exception {
 double[] std = dist.getStandardDeviations();
 assertArrayEquals(std.length, dist.getDimension(), 0.0);
 }

 @Test
 public void testExponentTerm() throws Exception {
 double[] testValues = new double[]{0.0, 1.0, 2.0};
 double exponentTerm = dist.getExponentTerm(testValues);
 RealMatrix covarianceMatrixInverse = dist.covarianceMatrixInverse;
 double[] centered = new double[testValues.length];
 System.arraycopy(testValues, 0, centered, 0, testValues.length);
 for (int i = 0; i < centered.length; i++) {
 centered[i] -= dist.means[i];
 }
 RealVector preMultiplied = covarianceMatrixInverse.operate(new Array2DRowRealMatrix(new double[][]{centered}).getColumnVector(0));
 double sum = 0.0;
 for (int i = 0; i < preMultiplied.getDimension(); i++) {
 sum += preMultiplied.getEntry(i) * centered[i];
 }
 double expectedExponentTerm = FastMath.exp(-0.5 * sum);
 assertEquals(expectedExponentTerm, exponentTerm, 0.001);
 }

 @Test
 public void testEigenvalueDecomposition() throws Exception {
 RealMatrix covMatDec = new EigenDecomposition(dist.covarianceMatrix);
 SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(dist.covarianceMatrix);
 RealMatrix covMatEigenvectors = covMatDec.getEigenvectors();
 RealMatrix covMatEigenvectorsSvd = singularValueDecomposition.getU();
 for (int i = 0; i < dist.getDimension(); i++) {
 for (int j = 0; j < dist.getDimension(); j++) {
 assertEquals(covMatEigenvectors.getEntry(i, j), covMatEigenvectorsSvd.getEntry(i, j), 0.001);
 }
 }
 }

 @Test
 public void testSampleWithLargeMatrix() throws Exception {
 // Create a large matrix for testing
 int matrixSize = 10;
 double[][] covariances = new double[matrixSize][matrixSize];
 for (int i = 0; i < matrixSize; i++) {
 for (int j = 0; j < matrixSize; j++) {
 covariances[i][j] = 0.1;
 }
 }
 try {
 MultivariateNormalDistribution largeDist = new MultivariateNormalDistribution(rng, means, covariances);
 double[] sample = largeDist.sample();
 } catch (Exception e) {
 fail("Sample method should not throw an exception with large covariance matrix");
 }
 }
}