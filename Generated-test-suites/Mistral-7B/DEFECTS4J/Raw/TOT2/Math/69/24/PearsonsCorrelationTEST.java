import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

 @Test
 public void testEmptyConstructor() {
 PearsonsCorrelation pc = new PearsonsCorrelation();
 assertNull(pc.getCorrelationMatrix());
 assertEquals(0, pc.nObs);
 }

 @Test
 public void testDataConstructor() {
 double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
 PearsonsCorrelation pc = new PearsonsCorrelation(data);
 RealMatrix expected = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1, 0.8, 0.6}, {0.8, 1, 0.8}, {0.6, 0.8, 1}});
 RealMatrix actual = pc.getCorrelationMatrix();
 assertEquals(expected, actual);
 assertEquals(3, pc.nObs);
 }

 @Test
 public void testMatrixConstructor() {
 RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
 PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
 RealMatrix expected = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1, 0.8, 0.6}, {0.8, 1, 0.8}, {0.6, 0.8, 1}});
 RealMatrix actual = pc.getCorrelationMatrix();
 assertEquals(expected, actual);
 assertEquals(3, pc.nObs);
 }

 @Test
 public void testCovarianceConstructor() {
 RealMatrix covarianceMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}});
 PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
 RealMatrix expected = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1, 0.8, 0.6}, {0.8, 1, 0.8}, {0.6, 0.8, 1}});
 RealMatrix actual = pc.getCorrelationMatrix();
 assertEquals(expected, actual);
 assertEquals(3, pc.nObs);
 }

 @Test(expected = MathRuntimeException.class)
 public void testInvalidMatrixConstructor() {
 RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
 PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
 fail("Exception not thrown");
 }

 @Test
 public void testCorrelationStandardErrors() {
 PearsonsCorrelation pc = new PearsonsCorrelation(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
 RealMatrix stdErrors = pc.getCorrelationStandardErrors();
 RealMatrix expected = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{0.759352334214619, 0.65254514254817, 0.67346884822752}, {0.65254514254817, 0.759352334214619, 0.711262198974313}, {0.67346884822752, 0.711262198974313, 0.759352334214619}});
 RealMatrix actual = stdErrors;
 assertEquals(expected, actual);
 }

 @Test
 public void testCorrelationPValues() throws MathException {
 PearsonsCorrelation pc = new PearsonsCorrelation(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
 RealMatrix pValues = pc.getCorrelationPValues();
 RealMatrix expected = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{0.000389392983215, 0.000299113227844, 0.000389392983215}, {0.000299113227844, 0.000389392983215, 0.000389392983215}, {0.000389392983215, 0.000389392983215, 0.000389392983215}});
 RealMatrix actual = pValues;
 assertEquals(expected, actual);
 }

 @Test
 public void testCorrelation() {
 double[] xArray = {1, 2, 3};
 double[] yArray = {4, 5, 6};
 double correlation = PearsonsCorrelation.correlation(xArray, yArray);
 assertEquals(0.8, correlation, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCorrelationData() {
 double[] xArray = {1};
 double[] yArray = {4, 5};
 assertThrows(IllegalArgumentException.class, () -> PearsonsCorrelation.correlation(xArray, yArray));
 }
}