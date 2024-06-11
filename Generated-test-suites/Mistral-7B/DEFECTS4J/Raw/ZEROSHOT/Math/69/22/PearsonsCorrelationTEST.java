package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PearsonsCorrelationTest {

 @Test
 public void testEmptyConstructor() {
 PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
 assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
 assertEquals(0, pearsonsCorrelation.getCorrelationMatrix().getRowDimension());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidData1() {
 double[][] data = new double[1][1];
 new PearsonsCorrelation(data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidData2() {
 double[][] data = new double[1][2];
 new PearsonsCorrelation(data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidData3() {
 double[][] data = new double[2][1];
 new PearsonsCorrelation(data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidData4() {
 double[][] data = new double[2][1];
 new PearsonsCorrelation(data);
 }

 @Test
 public void testValidData1() {
 double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}};
 RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8775825287, 0.8455959253},
 {0.8775825287, 1.0, 0.8775825287}, {0.8455959253, 0.8775825287, 1.0}});
 PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
 assertEquals(correlationMatrix, pearsonsCorrelation.getCorrelationMatrix());
 }

 @Test
 public void testValidData2() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
 RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8452380952, 0.8328342515},
 {0.8452380952, 1.0, 0.8452380952}, {0.8328342515, 0.8452380952, 1.0}});
 PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
 assertEquals(correlationMatrix, pearsonsCorrelation.getCorrelationMatrix());
 }

 @Test
 public void testGetCorrelationStandardErrors() throws Exception {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
 PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
 RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.4325443755, 0.4266348448, 0.4266348448},
 {0.4266348448, 0.4325443755, 0.4266348448}, {0.4266348448, 0.4266348448, 0.4325443755}});
 assertEquals(expected, correlationStandardErrors);
 }

 @Test(expected = MathException.class)
 public void testGetCorrelationPValuesThrowsException() throws Exception {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
 PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
 pearsonsCorrelation.getCorrelationPValues();
 }

 @Test
 public void testGetCorrelationPValues() throws Exception {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
 PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
 RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.0232885875, 0.0232885875, 0.0232885875},
 {0.0232885875, 0.0232885875, 0.0232885875}, {0.0232885875, 0.0232885875, 0.0232885875}});
 assertEquals(expected, correlationPValues);
 }

 @Test
 public void testComputationOfCorrelationMatrix() {
 double[][] data1 = new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
 double[][] data2 = new double[][]{{1, 3, 2}, {3, 2, 1}, {2, 1, 3}};
 RealMatrix correlationMatrix1 = new Array2DRowRealMatrix(new double[][]{{1.0, 0.6185427763, 0.6622625223},
 {0.6185427763, 1.0, 0.6185427763}, {0.6622625223, 0.6185427763, 1.0}});
 RealMatrix correlationMatrix2 = new Array2DRowRealMatrix(new double[][]{{0.6595504936, -0.6123225742, 0.7021197605},
 {-0.6123225742, 0.7816313116, -0.6841754388}, {0.7021197605, -0.6841754388, 0.6255511126}});
 PearsonsCorrelation pearsonsCorrelation1 = new PearsonsCorrelation();
 PearsonsCorrelation pearsonsCorrelation2 = new PearsonsCorrelation();
 pearsonsCorrelation1.computeCorrelationMatrix(data1);
 pearsonsCorrelation2.computeCorrelationMatrix(data2);
 assertEquals(correlationMatrix1, pearsonsCorrelation1.getCorrelationMatrix());
 assertEquals(correlationMatrix2, pearsonsCorrelation2.getCorrelationMatrix());
 }

 @Test
 public void testComputationOfCorrelationMatrixWithMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
 RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.6185427763, 0.6622625223},
 {0.6185427763, 1.0, 0.6185427763}, {0.6622625223, 0.6185427763, 1.0}});
 PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
 pearsonsCorrelation.computeCorrelationMatrix(matrix);
 assertEquals(correlationMatrix, pearsonsCorrelation.getCorrelationMatrix());
 }

 @Test
 public void testComputationOfCorrelation() {
 double[] xArray = new double[]{1, 2, 3};
 double[] yArray = new double[]{2, 3, 4};
 double correlation = 0.6185427763;
 assertEquals(correlation, PearsonsCorrelation.correlation(xArray, yArray), 0.00001);
 }

 @Test
 public void testCovarianceToCorrelation() {
 RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 10}});
 RealMatrix correlationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.4898979492, 0.7471953313},
 {0.4898979492, 1.0, 0.8159550561}, {0.7471953313, 0.8159550561, 1.0}});
 PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
 assertEquals(correlationMatrix, pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix));
 }
}