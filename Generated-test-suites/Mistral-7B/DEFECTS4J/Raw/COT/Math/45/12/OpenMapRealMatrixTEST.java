import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OpenMapRealMatrixTest {

 private OpenMapRealMatrix createMatrix(int rowDimension, int columnDimension) {
 return new OpenMapRealMatrix(rowDimension, columnDimension);
 }

 @Test
 public void testOpenMapRealMatrixConstruction() {
 OpenMapRealMatrix matrix = createMatrix(2, 3);
 assertEquals(2, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test
 public void testOpenMapRealMatrixCopy() {
 OpenMapRealMatrix original = createMatrix(2, 3);
 OpenMapRealMatrix copy = original.copy();
 assertEquals(original, copy);
 }

 @Test
 public void testAddition() {
 OpenMapRealMatrix a = createMatrix(2, 2);
 OpenMapRealMatrix b = createMatrix(2, 2);
 a.setEntry(0, 0, 1.0);
 a.setEntry(1, 1, 1.0);
 b.setEntry(0, 0, 2.0);
 b.setEntry(1, 1, 2.0);
 b.setEntry(1, 0, 1.0);
 OpenMapRealMatrix result = a.add(b);
 OpenMapRealMatrix expected = createMatrix(2, 2);
 expected.setEntry(0, 0, 3.0);
 expected.setEntry(0, 1, 1.0);
 expected.setEntry(1, 0, 1.0);
 expected.setEntry(1, 1, 3.0);
 assertEquals(expected, result);
 }

 @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
 public void testAdditionDimensionMismatch() {
 OpenMapRealMatrix a = createMatrix(2, 2);
 OpenMapRealMatrix b = createMatrix(3, 2);
 a.add(b);
 }

 @Test
 public void testSubtraction() {
 OpenMapRealMatrix a = createMatrix(2, 2);
 OpenMapRealMatrix b = createMatrix(2, 2);
 a.setEntry(0, 0, 1.0);
 a.setEntry(1, 1, 1.0);
 b.setEntry(0, 0, -1.0);
 b.setEntry(1, 1, -1.0);
 b.setEntry(1, 0, 1.0);
 OpenMapRealMatrix result = a.subtract(b);
 OpenMapRealMatrix expected = createMatrix(2, 2);
 expected.setEntry(0, 0, 0.0);
 expected.setEntry(0, 1, -1.0);
 expected.setEntry(1, 0, 0.0);
 expected.setEntry(1, 1, 0.0);
 assertEquals(expected, result);
 }

 @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
 public void testSubtractionDimensionMismatch() {
 OpenMapRealMatrix a = createMatrix(2, 2);
 OpenMapRealMatrix b = createMatrix(3, 2);
 a.subtract(b);
 }

 @Test
 public void testMultiplication() {
 OpenMapRealMatrix a = createMatrix(2, 2);
 OpenMapRealMatrix b = createMatrix(2, 2);
 a.setEntry(0, 0, 1.0);
 a.setEntry(0, 1, 2.0);
 a.setEntry(1, 0, 3.0);
 a.setEntry(1, 1, 4.0);
 b.setEntry(0, 0, 1.0);
 b.setEntry(0, 1, 2.0);
 b.setEntry(1, 0, 3.0);
 b.setEntry(1, 1, 4.0);
 OpenMapRealMatrix result = a.multiply(b);
 OpenMapRealMatrix expected = createMatrix(2, 2);
 expected.setEntry(0, 0, 15.0);
 expected.setEntry(0, 1, 22.0);
 expected.setEntry(1, 0, 22.0);
 expected.setEntry(1, 1, 36.0);
 assertEquals(expected, result);
 }

 @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
 public void testMultiplicationDimensionMismatch() {
 OpenMapRealMatrix a = createMatrix(2, 2);
 OpenMapRealMatrix b = createMatrix(3, 2);
 a.multiply(b);
 }

 @Test
 public void testGetEntry() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 assertEquals(1.0, matrix.getEntry(0, 0), 0.01);
 assertEquals(2.0, matrix.getEntry(1, 1), 0.01);
 }

 @Test(expected = org.apache.commons.math3.exception.ArrayIndexOutOfBoundsException.class)
 public void testGetEntryOutOfBounds() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.getEntry(2, 2);
 }

 @Test
 public void testSetEntry() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 matrix.setEntry(1, 0, 3.0);
 assertEquals(3.0, matrix.getEntry(1, 0), 0.01);
 }

 @Test(expected = org.apache.commons.math3.exception.ArrayIndexOutOfBoundsException.class)
 public void testSetEntryOutOfBounds() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(2, 2, 3.0);
 }

 @Test
 public void testAddToEntry() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.addToEntry(0, 0, 2.0);
 assertEquals(3.0, matrix.getEntry(0, 0), 0.01);
 }

 @Test(expected = org.apache.commons.math3.exception.ArrayIndexOutOfBoundsException.class)
 public void testAddToEntryOutOfBounds() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.addToEntry(2, 2, 3.0);
 }

 @Test
 public void testMultiplyEntry() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.multiplyEntry(0, 0, 2.0);
 assertEquals(2.0, matrix.getEntry(0, 0), 0.01);
 }

 @Test(expected = org.apache.commons.math3.exception.ArrayIndexOutOfBoundsException.class)
 public void testMultiplyEntryOutOfBounds() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.multiplyEntry(2, 2, 2.0);
 }

 @Test
 public void testNumberIsTooLargeException() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(Integer.MAX_VALUE, Integer.MAX_VALUE);
 assertThrows(NumberIsTooLargeException.class, () -> matrix.createMatrix(Integer.MAX_VALUE + 1, 1));
 }

}