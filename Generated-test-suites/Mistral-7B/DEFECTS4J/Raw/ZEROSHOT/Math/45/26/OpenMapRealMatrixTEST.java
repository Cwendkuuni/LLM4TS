import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OpenMapRealMatrixTest {

 @Test
 public void testOpenMapRealMatrixConstruction() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 assertEquals(2, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test
 public void testCopyConstructor() {
 OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
 assertEquals(original.getRowDimension(), copy.getRowDimension());
 assertEquals(original.getColumnDimension(), copy.getColumnDimension());
 }

 @Test
 public void testAddition() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 matrix1.setEntry(0, 0, 2.0);
 matrix1.setEntry(0, 1, 3.0);
 matrix2.setEntry(0, 1, 1.0);
 matrix2.setEntry(1, 1, 2.0);

 OpenMapRealMatrix result = matrix1.add(matrix2);
 OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 expected.setEntry(0, 0, 2.0);
 expected.setEntry(0, 1, 4.0);
 expected.setEntry(1, 1, 2.0);

 assertEquals(expected, result);
 }

 @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
 public void testAdditionDimensionMismatch() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
 matrix1.setEntry(0, 0, 2.0);
 matrix1.setEntry(0, 1, 3.0);

 matrix1.add(matrix2);
 }

 @Test
 public void testSubtraction() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 matrix1.setEntry(0, 0, 2.0);
 matrix1.setEntry(0, 1, 3.0);
 matrix2.setEntry(0, 1, 1.0);
 matrix2.setEntry(1, 1, 2.0);

 OpenMapRealMatrix result = matrix1.subtract(matrix2);
 OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 expected.setEntry(0, 0, 2.0);
 expected.setEntry(0, 1, 2.0);
 expected.setEntry(1, 1, 0.0);

 assertEquals(expected, result);
 }

 @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
 public void testSubtractionDimensionMismatch() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
 matrix1.setEntry(0, 0, 2.0);
 matrix1.setEntry(0, 1, 3.0);

 matrix1.subtract(matrix2);
 }

 @Test
 public void testMultiplication() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 matrix1.setEntry(0, 0, 2.0);
 matrix1.setEntry(0, 1, 3.0);
 matrix2.setEntry(0, 0, 1.0);
 matrix2.setEntry(0, 1, 2.0);
 matrix2.setEntry(1, 1, 2.0);

 BlockRealMatrix result = matrix1.multiply(matrix2);
 double[][] expected = { { 2.0, 6.0 }, { 3.0, 7.0 } };

 assertEquals(expected.length, result.getRowDimension());
 assertEquals(expected[0].length, result.getColumnDimension());

 for (int i = 0; i < result.getRowDimension(); i++) {
 for (int j = 0; j < result.getColumnDimension(); j++) {
 assertEquals(expected[i][j], result.getEntry(i, j), 1e-10);
 }
 }
 }

 @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
 public void testMultiplicationDimensionMismatch() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
 matrix1.setEntry(0, 0, 2.0);
 matrix1.setEntry(0, 1, 3.0);

 matrix1.multiply(matrix2);
 }

 @Test
 public void testGetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 matrix.setEntry(0, 0, 2.0);

 assertEquals(2.0, matrix.getEntry(0, 0), 1e-10);
 }

 @Test
 public void testSetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());

 matrix.setEntry(0, 0, 2.0);
 assertEquals(2.0, matrix.getEntry(0, 0), 1e-10);
 }

 @Test
 public void testAddToEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 matrix.setEntry(0, 0, 2.0);

 matrix.addToEntry(0, 0, 3.0);
 assertEquals(5.0, matrix.getEntry(0, 0), 1e-10);
 }

 @Test
 public void testMultiplyEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
 matrix.setEntry(0, 0, 2.0);

 matrix.multiplyEntry(0, 0, 3.0);
 assertEquals(6.0, matrix.getEntry(0, 0), 1e-10);
 }

 @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
 public void testAddEntryTooLarge() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());

 matrix.setEntry(0, 0, Double.MAX_VALUE + 1.0);
 }
}