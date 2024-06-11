import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PearsonsCorrelationTest {

 @Test
 public void testEmptyMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new RealArray2D.Dense2D(0, 0));
 PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
 assertEquals(null, pearson.getCorrelationMatrix());
 assertEquals(0, pearson.getNObs());
 }

 @Test
 public void testIdentityMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new RealArray2D.Dense2D(2, 2).setIdentity(2));
 PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
 RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
 double[][] expected = {{1.0, 1.0}, {1.0, 1.0}};
 assertArrayEquals(expected, correlationMatrix.getData());
 assertEquals(2, pearson.getNObs());
 }

 @Test
 public void testCorrelationMatrix() {
 double[][] input = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {0.5, 0.5, 0.5}};
 RealMatrix matrix = new BlockRealMatrix(input);
 PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
 RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
 double[][] expected = {{1.0, 0.819152, 0.447214}, {0.819152, 1.0, 0.819152}, {0.447214, 0.819152, 1.0}};
 assertArrayEquals(expected, correlationMatrix.getData());
 assertEquals(3, pearson.getNObs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonSquareMatrix() {
 double[][] input = {{1.0, 2.0}, {3.0, 2.0}, {0.5, 0.5}};
 RealMatrix matrix = new BlockRealMatrix(input);
 PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
 }

 @Test
 public void testCorrelationStandardErrors() throws MathException {
 double[][] input = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {0.5, 0.5, 0.5}};
 RealMatrix matrix = new BlockRealMatrix(input);
 PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
 RealMatrix correlationStandardErrors = pearson.getCorrelationStandardErrors();
 double[][] expected = {{0.819152, 0.621271, 0.5}, {0.621271, 0.819152, 0.621271}, {0.5, 0.621271, 0.819152}};
 assertArrayEquals(expected, correlationStandardErrors.getData());
 }

 @Test(expected = MathException.class)
 public void testCorrelationPValues() throws MathException {
 RealMatrix matrix = new BlockRealMatrix(new double[0][0]);
 PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
 pearson.getCorrelationPValues();
 }
}