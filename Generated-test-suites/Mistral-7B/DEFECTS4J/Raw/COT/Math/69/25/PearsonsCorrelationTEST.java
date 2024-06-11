import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PearsonsCorrelationTest {

 @Test
 public void testEmptyConstructor() {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
 assertEquals(null, pearsonCorrelation.getCorrelationMatrix());
 assertEquals(0, pearsonCorrelation.nObs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDataArray() {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(new double[][]{});
 }

 @Test
 public void testValidDataArray() {
 double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 double[][] expected = {{1.0, 0.8333333333333333, 0.8}, {0.8333333333333333, 1.0, 0.8}, {0.8, 0.8, 1.0}};
 assertArrayEquals(expected, correlationMatrix.getData());
 }

 @Test
 public void testValidRealMatrix() {
 RealMatrix matrix = new BlockRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 double[][] expected = {{1.0, 0.8333333333333333, 0.8}, {0.8333333333333333, 1.0, 0.8}, {0.8, 0.8, 1.0}};
 assertArrayEquals(expected, correlationMatrix.getData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRealMatrix() {
 RealMatrix matrix = new BlockRealMatrix(new double[][]{{1, 2}, {3}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 }

 @Test
 public void testValidCovariance() {
 RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1, 1, 1}, {1, 2, 1}, {1, 1, 2}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(new Covariance(covarianceMatrix));
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 double[][] expected = {{1.0, 0.8333333333333333, 0.8}, {0.8333333333333333, 1.0, 0.8}, {0.8, 0.8, 1.0}};
 assertArrayEquals(expected, correlationMatrix.getData());
 }

 @Test
 public void testValidRealMatrixAndNumberOfObservations() {
 RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1, 1, 1}, {1, 2, 1}, {1, 1, 2}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 6);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 double[][] expected = {{1.0, 0.8333333333333333, 0.8}, {0.8333333333333333, 1.0, 0.8}, {0.8, 0.8, 1.0}};
 assertArrayEquals(expected, correlationMatrix.getData());
 }

 @Test
 public void testGetCorrelationStandardErrors() throws MathException {
 RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1, 0.8, 0.8}, {0.8, 1, 0.8}, {0.8, 0.8, 1}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(correlationMatrix);
 RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
 double[][] expected = {{0.4472135955011251, 0.4472135955011251, 0.4472135955011251},
                         {0.4472135955011251, 0.4472135955011251, 0.4472135955011251},
                         {0.4472135955011251, 0.4472135955011251, 0.4472135955011251}};
 assertArrayEquals(expected, correlationStandardErrors.getData(), 0.00001);
 }

 @Test
 public void testGetCorrelationPValues() throws MathException {
 RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1, 0.8, 0.8}, {0.8, 1, 0.8}, {0.8, 0.8, 1}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(correlationMatrix);
 RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
 double[][] expected = {{0.3531233218128583, 0.3531233218128583, 0.3531233218128583},
                         {0.3531233218128583, 0.3531233218128583, 0.3531233218128583},
                         {0.3531233218128583, 0.3531233218128583, 0.3531233218128583}};
 assertArrayEquals(expected, correlationPValues.getData(), 0.00001);
 }

 @Test
 public void testCorrelation() {
 double[] xArray = {1, 2, 3};
 double[] yArray = {1, 2, 3};
 double correlation = PearsonsCorrelation.correlation(xArray, yArray);
 assertEquals(1.0, correlation, 0.00001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCorrelationDataArrayX() {
 double[] xArray = {};
 double[] yArray = {1, 2, 3};
 PearsonsCorrelation.correlation(xArray, yArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCorrelationDataArrayY() {
 double[] xArray = {1, 2, 3};
 double[] yArray = {};
 PearsonsCorrelation.correlation(xArray, yArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCorrelationDataArrayLengthMismatch() {
 double[] xArray = {1, 2};
 double[] yArray = {1, 2, 3};
 PearsonsCorrelation.correlation(xArray, yArray);
 }
}