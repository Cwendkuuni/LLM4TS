import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

 private OpenMapRealMatrix createMatrix(int rowDimension, int columnDimension) {
 return new OpenMapRealMatrix(rowDimension, columnDimension);
 }

 @Test
 public void testConstructorWithValidDimensions() {
 OpenMapRealMatrix matrix = createMatrix(2, 3);
 assertEquals(2, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeDimensions() {
 createMatrix(-1, 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithZeroRows() {
 createMatrix(0, 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithZeroColumns() {
 createMatrix(2, 0);
 }

 @Test
 public void testCopyConstructor() {
 OpenMapRealMatrix original = createMatrix(2, 2);
 original.setEntry(0, 0, 1.0);
 OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
 assertEquals(original, copy);
 }

 @Test
 public void testAddMethod() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 2);
 OpenMapRealMatrix matrix2 = createMatrix(2, 2);
 OpenMapRealMatrix sum = matrix1.add(matrix2);
 assertEquals(matrix1, sum);
 matrix1.setEntry(0, 0, 1.0);
 matrix1.setEntry(1, 1, 2.0);
 matrix2.setEntry(0, 0, 3.0);
 matrix2.setEntry(1, 1, 4.0);
 sum.setEntry(0, 0, 4.0);
 sum.setEntry(1, 1, 6.0);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAddDimensionMismatch() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 2);
 OpenMapRealMatrix matrix2 = createMatrix(3, 3);
 matrix1.add(matrix2);
 }

 @Test
 public void testSubtractMethod() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 2);
 OpenMapRealMatrix matrix2 = createMatrix(2, 2);
 OpenMapRealMatrix difference = matrix1.subtract(matrix2);
 assertEquals(matrix1, difference);
 matrix1.setEntry(0, 0, 1.0);
 matrix1.setEntry(1, 1, 2.0);
 matrix2.setEntry(0, 0, 3.0);
 matrix2.setEntry(1, 1, 4.0);
 difference.setEntry(0, 0, -2.0);
 difference.setEntry(1, 1, -2.0);
 }

 @Test
 public void testMultiplyMethod() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 3);
 OpenMapRealMatrix matrix2 = createMatrix(3, 2);
 RealMatrix product = matrix1.multiply(matrix2);
 OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
 expected.setEntry(0, 0, 1.0);
 expected.setEntry(0, 1, 2.0);
 expected.setEntry(0, 2, 3.0);
 expected.setEntry(1, 0, 4.0);
 expected.setEntry(1, 1, 5.0);
 expected.setEntry(1, 2, 6.0);
 assertEquals(expected, product);
 }

 @Test(expected = MatrixDimensionMismatchException.class)
 public void testMultiplyDimensionMismatch() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 3);
 OpenMapRealMatrix matrix2 = createMatrix(3, 4);
 matrix1.multiply(matrix2);
 }

 @Test
 public void testGetEntryMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
 assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetEntryWithInvalidIndex() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.getEntry(3, 3);
 }

 @Test
 public void testSetEntryMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
 assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetEntryWithInvalidIndex() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(3, 3, 3.0);
 }

 @Test
 public void testAddToEntryMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.addToEntry(0, 0, 1.0);
 matrix.addToEntry(1, 1, 2.0);
 assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
 assertEquals(3.0, matrix.getEntry(1, 1), 0.001);
 }

 @Test
 public void testMultiplyEntryMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.multiplyEntry(0, 0, 2.0);
 assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test
 public void testComputeKeyMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 assertEquals(0, matrix.computeKey(0, 0));
 assertEquals(1, matrix.computeKey(0, 1));
 assertEquals(2, matrix.computeKey(1, 0));
 assertEquals(3, matrix.computeKey(1, 1));
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSetEntryWithLargeValue() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 long maxValue = Long.MAX_VALUE;
 matrix.setEntry(0, 0, maxValue);
 }

}