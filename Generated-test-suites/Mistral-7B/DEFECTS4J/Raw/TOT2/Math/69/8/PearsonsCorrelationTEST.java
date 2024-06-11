import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

 @Test
 public void testEmptyConstructor() {
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
 assertNull(pearsonCorrelation.getCorrelationMatrix());
 assertEquals(0, pearsonCorrelation.nObs);
 }

 @Test
 public void testEmptyArrayConstructor() {
 double[][] emptyData = new double[0][];
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(emptyData);
 assertNull(pearsonCorrelation.getCorrelationMatrix());
 assertEquals(0, pearsonCorrelation.nObs);
 }

 @Test
 public void testRectangularArrayConstructor() {
 double[][] data = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 RealArray2D expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][] {
             {1.0, 0.8333333333333334, 0.853553390598335},
             {0.8333333333333334, 1.0, 0.8333333333333334},
             {0.853553390598335, 0.8333333333333334, 1.0} });
 assertMatrixEquals(expectedCorrelationMatrix, correlationMatrix);
 assertEquals(3, pearsonCorrelation.nObs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonRectangularArrayConstructor() {
 double[][] nonRectangularData = { {1, 2}, {3, 4, 5}, {6, 7, 8, 9} };
 new PearsonsCorrelation(nonRectangularData);
 }

 @Test
 public void testRealMatrixConstructor() {
 RealMatrix matrix = new BlockRealMatrix(new double[][] { {1, 2}, {3, 4} });
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 RealArray2D expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][] {
             {1.0, 0.5},
             {0.5, 1.0} });
 assertMatrixEquals(expectedCorrelationMatrix, correlationMatrix);
 assertEquals(2, pearsonCorrelation.nObs);
 }

 @Test
 public void testCovarianceConstructor() {
 Covariance covariance = new Covariance(new double[][] { {1, 2}, {2, 3} });
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 RealArray2D expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][] {
             {1.0, 0.5},
             {0.5, 1.0} });
 assertMatrixEquals(expectedCorrelationMatrix, correlationMatrix);
 assertEquals(2, pearsonCorrelation.nObs);
 }

 @Test
 public void testRealMatrixConstructorWithInvalidNumberOfObservations() {
 RealMatrix invalidMatrix = new BlockRealMatrix(new double[][] { {1, 2}, {3, 4}, {5, 6}, {7, 8} });
 try {
 new PearsonsCorrelation(invalidMatrix, 3);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetCorrelationMatrix() {
 // Same test as testRealMatrixConstructor, but getCorrelationMatrix is used instead
 RealMatrix matrix = new BlockRealMatrix(new double[][] { {1, 2}, {3, 4} });
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
 RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
 RealArray2D expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][] {
             {1.0, 0.5},
             {0.5, 1.0} });
 assertMatrixEquals(expectedCorrelationMatrix, correlationMatrix);
 }

 @Test
 public void testGetCorrelationStandardErrors() {
 RealMatrix correlationMatrix = new BlockRealMatrix(new double[][] { {1.0, 0.5}, {0.5, 1.0} });
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(correlationMatrix);
 RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
 RealArray2D expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][] {
             {0.5, 0.5},
             {0.5, 0.5} });
 assertMatrixEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
 }

 @Test
 public void testGetCorrelationPValues() throws MathException {
 RealMatrix correlationMatrix = new BlockRealMatrix(new double[][] { {1.0, 0.5}, {0.5, 1.0} });
 PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(correlationMatrix);
 RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
 RealArray2D expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][] {
             {0.6666666666666666, 0.6666666666666666},
             {0.6666666666666666, 0.6666666666666666} });
 assertMatrixEquals(expectedCorrelationPValues, correlationPValues);
 }
}