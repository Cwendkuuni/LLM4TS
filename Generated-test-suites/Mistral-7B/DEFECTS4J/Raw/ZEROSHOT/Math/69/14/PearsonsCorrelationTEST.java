import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

 @Test
 public void testEmptyConstructor() {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
 assertNull(pearsonCorrelation.getCorrelationMatrix());
 assertEquals(0, pearsonCorrelation.getnObs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDataIsNotRectangular() {
 double[][] data = new double[][]{{1, 2, 3}, {3, 4, 5, 6}};
 new PearsonsCorrelation(data);
 }

 @Test
 public void testDataWithTwoColumns() {
 double[][] data = new double[][]{{1, 2}, {3, 4}, {5, 6}};
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertNotNull(correlationMatrix);
 assertEquals(3, correlationMatrix.getRowDimension());
 assertEquals(3, correlationMatrix.getColumnDimension());
 assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
 assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
 assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
 assertEquals(0.8, correlationMatrix.getEntry(1, 1), 0.001);
 }

 @Test
 public void testDataWithTwoColumnsAndZeroSize() {
 double[][] data = new double[][]{{}, {}};
 try {
 new PearsonsCorrelation(data);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testRealMatrixWithTwoColumns() {
 double[][] data = new double[][]{{1, 2}, {3, 4}, {5, 6}};
 RealMatrix matrix = new BlockRealMatrix(data);
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertNotNull(correlationMatrix);
 assertEquals(3, correlationMatrix.getRowDimension());
 assertEquals(3, correlationMatrix.getColumnDimension());
 assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
 assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
 assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
 assertEquals(0.8, correlationMatrix.getEntry(1, 1), 0.001);
 }

 @Test
 public void testRealMatrixWithTwoColumnsAndZeroSize() {
 RealMatrix matrix = new BlockRealMatrix(new double[][]{{}, {}});
 try {
 new PearsonsCorrelation(matrix);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testRealMatrixWithOneColumn() {
 RealMatrix matrix = new BlockRealMatrix(new double[][]{{1}});
 try {
 new PearsonsCorrelation(matrix);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testRealMatrixWithMoreThanTwoColumns() {
 RealMatrix matrix = new BlockRealMatrix(new double[][]{{1, 2, 3}, {3, 4, 5}, {5, 6, 7}});
 try {
 new PearsonsCorrelation(matrix);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCovarianceWithTwoColumns() {
 double[][] data = new double[][]{{1, 2}, {3, 4}, {5, 6}};
 Covariance covariance = new Covariance(data);
 RealMatrix correlationMatrix = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN()).getCorrelationMatrix();
 RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
 assertEquals(expectedCorrelationMatrix, correlationMatrix);
 }

 @Test
 public void testCovarianceWithMoreThanTwoColumns() {
 double[][] data = new double[][]{{1, 2, 3}, {3, 4, 5}, {5, 6, 7}};
 Covariance covariance = new Covariance(data);
 RealMatrix correlationMatrix = null;
 try {
 correlationMatrix = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN()).getCorrelationMatrix();
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(correlationMatrix);
 }

 @Test
 public void testCorrelationStandardErrors() throws MathException {
 double[][] data = new double[][]{{1, 2}, {3, 4}, {5, 6}};
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 RealMatrix standardErrors = pearsonCorrelation.getCorrelationStandardErrors();
 assertEquals(3, standardErrors.getRowDimension());
 assertEquals(3, standardErrors.getColumnDimension());
 assertEquals(0.48284063, standardErrors.getEntry(0, 0), 0.001);
 assertEquals(0.35355339, standardErrors.getEntry(0, 1), 0.001);
 assertEquals(0.35355339, standardErrors.getEntry(1, 0), 0.001);
 assertEquals(0.32821855, standardErrors.getEntry(1, 1), 0.001);
 }

 @Test
 public void testCorrelationPValues() throws MathException {
 double[][] data = new double[][]{{1, 2}, {3, 4}, {5, 6}};
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 RealMatrix pValues = pearsonCorrelation.getCorrelationPValues();
 assertEquals(3, pValues.getRowDimension());
 assertEquals(3, pValues.getColumnDimension());
 assertEquals(0.1, pValues.getEntry(0, 0), 0.01);
 assertEquals(0.1, pValues.getEntry(0, 1), 0.01);
 assertEquals(0.1, pValues.getEntry(1, 0), 0.01);
 assertEquals(0.017, pValues.getEntry(1, 1), 0.01);
 }

 @Test
 public void testCorrelationAndCorrelationStandardErrorsWithEmptyMatrix() {
 RealMatrix matrix = new BlockRealMatrix(new double[][]{{}, {}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 assertNull(pearsonCorrelation.getCorrelationMatrix());
 RealMatrix standardErrors = pearsonCorrelation.getCorrelationStandardErrors();
 assertNull(standardErrors);
 }

 @Test
 public void testCorrelationAndCorrelationStandardErrorsWithOneColumnMatrix() {
 RealMatrix matrix = new BlockRealMatrix(new double[][]{{1}});
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 assertNull(pearsonCorrelation.getCorrelationMatrix());
 RealMatrix standardErrors = pearsonCorrelation.getCorrelationStandardErrors();
 assertNull(standardErrors);
 }

 @Test
 public void testCorrelationAndCorrelationStandardErrorsWithTwoColumnsMatrix() {
 double[][] data = new double[][]{{1, 2}, {3, 4}, {5, 6}};
 RealMatrix matrix = new BlockRealMatrix(data);
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 assertNotNull(correlationMatrix);
 assertEquals(3, correlationMatrix.getRowDimension());
 assertEquals(3, correlationMatrix.getColumnDimension());
 assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
 assertEquals(0.5, correlationMatrix.getEntry(0, 1), 0.001);
 assertEquals(0.5, correlationMatrix.getEntry(1, 0), 0.001);
 assertEquals(0.8, correlationMatrix.getEntry(1, 1), 0.001);
 RealMatrix standardErrors = pearsonCorrelation.getCorrelationStandardErrors();
 RealMatrix expectedStandardErrors = new BlockRealMatrix(new double[][]{{0.48284063, 0.35355339}, {0.35355339, 0.32821855}});
 assertEquals(expectedStandardErrors, standardErrors);
 }
}