package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

public class PearsonsCorrelationTest {

 private static final double EPSILON = 1e-6;

 @Test
 public void testEmptyConstructor() {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
 assertNotNull(pearsonCorrelation.correlationMatrix);
 assertEquals(0, pearsonCorrelation.nObs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDataArray() {
 double[][] data = new double[1][];
 new PearsonsCorrelation(data);
 }

 @Test
 public void testValidDataArray() {
 double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertEquals(3, correlationMatrix.getRowDimension());
 assertEquals(3, correlationMatrix.getColumnDimension());
 assertEquals(1.0, correlationMatrix.getEntry(0, 0), EPSILON);
 assertEquals(0.8, correlationMatrix.getEntry(0, 1), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(0, 2), EPSILON);
 assertEquals(0.8, correlationMatrix.getEntry(1, 0), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(1, 1), EPSILON);
 assertEquals(0.4, correlationMatrix.getEntry(1, 2), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(2, 0), EPSILON);
 assertEquals(0.4, correlationMatrix.getEntry(2, 1), EPSILON);
 assertEquals(0.2, correlationMatrix.getEntry(2, 2), EPSILON);
 }

 @Test
 public void testValidRealMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertEquals(3, correlationMatrix.getRowDimension());
 assertEquals(3, correlationMatrix.getColumnDimension());
 assertEquals(1.0, correlationMatrix.getEntry(0, 0), EPSILON);
 assertEquals(0.8, correlationMatrix.getEntry(0, 1), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(0, 2), EPSILON);
 assertEquals(0.8, correlationMatrix.getEntry(1, 0), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(1, 1), EPSILON);
 assertEquals(0.4, correlationMatrix.getEntry(1, 2), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(2, 0), EPSILON);
 assertEquals(0.4, correlationMatrix.getEntry(2, 1), EPSILON);
 assertEquals(0.2, correlationMatrix.getEntry(2, 2), EPSILON);
 }

 @Test
 public void testCovariance() {
 double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
 Covariance covariance = StatUtils.covariance(data);
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertEquals(3, correlationMatrix.getRowDimension());
 assertEquals(3, correlationMatrix.getColumnDimension());
 assertEquals(1.0, correlationMatrix.getEntry(0, 0), EPSILON);
 assertEquals(0.8, correlationMatrix.getEntry(0, 1), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(0, 2), EPSILON);
 assertEquals(0.8, correlationMatrix.getEntry(1, 0), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(1, 1), EPSILON);
 assertEquals(0.4, correlationMatrix.getEntry(1, 2), EPSILON);
 assertEquals(0.6, correlationMatrix.getEntry(2, 0), EPSILON);
 assertEquals(0.4, correlationMatrix.getEntry(2, 1), EPSILON);
 assertEquals(0.2, correlationMatrix.getEntry(2, 2), EPSILON);
 }

 @Test
 public void testCorrelationStandardErrors() throws MathException {
 double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
 assertEquals(3, correlationStandardErrors.getRowDimension());
 assertEquals(3, correlationStandardErrors.getColumnDimension());
 assertEquals(0.5, correlationStandardErrors.getEntry(0, 0), EPSILON);
 assertEquals(0.3, correlationStandardErrors.getEntry(0, 1), EPSILON);
 assertEquals(0.3, correlationStandardErrors.getEntry(0, 2), EPSILON);
 assertEquals(0.3, correlationStandardErrors.getEntry(1, 0), EPSILON);
 assertEquals(0.3, correlationStandardErrors.getEntry(1, 1), EPSILON);
 assertEquals(0.3, correlationStandardErrors.getEntry(1, 2), EPSILON);
 assertEquals(0.3, correlationStandardErrors.getEntry(2, 0), EPSILON);
 assertEquals(0.3, correlationStandardErrors.getEntry(2, 1), EPSILON);
 assertEquals(0.3, correlationStandardErrors.getEntry(2, 2), EPSILON);
 }

 @Test
 public void testCorrelationPValues() throws MathException {
 double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
 assertEquals(3, correlationPValues.getRowDimension());
 assertEquals(3, correlationPValues.getColumnDimension());
 assertEquals(1.0, correlationPValues.getEntry(0, 0), EPSILON);
 assertEquals(0.05, correlationPValues.getEntry(0, 1), EPSILON);
 assertEquals(0.05, correlationPValues.getEntry(0, 2), EPSILON);
 assertEquals(0.05, correlationPValues.getEntry(1, 0), EPSILON);
 assertEquals(0.1, correlationPValues.getEntry(1, 1), EPSILON);
 assertEquals(0.1, correlationPValues.getEntry(1, 2), EPSILON);
 assertEquals(0.1, correlationPValues.getEntry(2, 0), EPSILON);
 assertEquals(0.15, correlationPValues.getEntry(2, 1), EPSILON);
 assertEquals(0.15, correlationPValues.getEntry(2, 2), EPSILON);
 }

 @Test
 public void testCorrelationWithInvalidData() {
 double[] xArray = new double[1];
 double[] yArray = new double[2];
 try {
 PearsonsCorrelation.correlation(xArray, yArray);
 fail();
 } catch (IllegalArgumentException e) {
 // success
 }
 }
}