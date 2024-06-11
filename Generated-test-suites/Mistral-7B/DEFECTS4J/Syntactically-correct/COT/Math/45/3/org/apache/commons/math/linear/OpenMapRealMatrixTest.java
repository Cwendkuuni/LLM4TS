import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

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
 try {
 createMatrix(-1, 2);
 } catch (IllegalArgumentException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testCopyConstructor() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 OpenMapRealMatrix copy = new OpenMapRealMatrix(matrix);
 assertEquals(matrix, copy);
 }

 @Test
 public void testAddMethod() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 2);
 OpenMapRealMatrix matrix2 = createMatrix(2, 2);
 matrix1.setEntry(0, 0, 1.0);
 matrix1.setEntry(1, 0, 2.0);
 matrix2.setEntry(0, 0, 3.0);
 matrix2.setEntry(0, 1, 4.0);
 OpenMapRealMatrix result = matrix1.add(matrix2);
 assertEquals(2, result.getRowDimension());
 assertEquals(2, result.getColumnDimension());
 assertEquals(4.0, result.getEntry(0, 0), 1e-12);
 assertEquals(3.0, result.getEntry(0, 1), 1e-12);
 assertEquals(2.0, result.getEntry(1, 0), 1e-12);
 assertEquals(0.0, result.getEntry(1, 1), 1e-12);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMethodWithIncompatibleMatrix() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 2);
 OpenMapRealMatrix matrix2 = createMatrix(2, 3);
 matrix1.add(matrix2);
 fail("Unexpected exception not thrown");
 }

 @Test
 public void testSubtractMethod() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 2);
 OpenMapRealMatrix matrix2 = createMatrix(2, 2);
 matrix1.setEntry(0, 0, 1.0);
 matrix1.setEntry(1, 0, 2.0);
 matrix2.setEntry(0, 0, 3.0);
 matrix2.setEntry(0, 1, 4.0);
 OpenMapRealMatrix result = matrix1.subtract(matrix2);
 assertEquals(2, result.getRowDimension());
 assertEquals(2, result.getColumnDimension());
 assertEquals(-2.0, result.getEntry(0, 0), 1e-12);
 assertEquals(-1.0, result.getEntry(0, 1), 1e-12);
 assertEquals(0.0, result.getEntry(1, 0), 1e-12);
 assertEquals(0.0, result.getEntry(1, 1), 1e-12);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubtractMethodWithIncompatibleMatrix() {
 OpenMapRealMatrix matrix1 = createMatrix(2, 2);
 OpenMapRealMatrix matrix2 = createMatrix(2, 3);
 matrix1.subtract(matrix2);
 fail("Unexpected exception not thrown");
 }

 @Test
 public void testMultiplyMethodWithVector() {
 OpenMapRealMatrix matrix = createMatrix(3, 2);
 RealVector vector = createVector(2);
 vector.setEntry(0, 1.0);
 vector.setEntry(1, 2.0);
 OpenMapRealMatrix result = matrix.multiply(vector);
 assertEquals(3, result.getRowDimension());
 assertEquals(2, result.getColumnDimension());
 assertEquals(5.0, result.getEntry(0, 0), 1e-12);
 assertEquals(6.0, result.getEntry(0, 1), 1e-12);
 assertEquals(7.0, result.getEntry(1, 0), 1e-12);
 assertEquals(8.0, result.getEntry(1, 1), 1e-12);
 assertEquals(4.0, result.getEntry(2, 0), 1e-12);
 assertEquals(5.0, result.getEntry(2, 1), 1e-12);
 }

 @Test(expected = MatrixDimensionMismatchException.class)
 public void testMultiplyMethodWithIncompatibleMatrix() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 OpenMapRealMatrix incompatibleMatrix = createMatrix(2, 3);
 try {
 matrix.multiply(incompatibleMatrix);
 } catch (MatrixDimensionMismatchException e) {
 // expected
 }
 }

 @Test
 public void testMultiplyMethodWithMatrix() {
 OpenMapRealMatrix matrix1 = createMatrix(3, 2);
 OpenMapRealMatrix matrix2 = createMatrix(2, 3);
 OpenMapRealMatrix result = matrix1.multiply(matrix2);
 assertEquals(3, result.getRowDimension());
 assertEquals(3, result.getColumnDimension());
 assertEquals(16.0, result.getEntry(0, 0), 1e-12);
 assertEquals(11.0, result.getEntry(0, 1), 1e-12);
 assertEquals(12.0, result.getEntry(1, 0), 1e-12);
 assertEquals(14.0, result.getEntry(1, 1), 1e-12);
 assertEquals(13.0, result.getEntry(1, 2), 1e-12);
 assertEquals(10.0, result.getEntry(2, 0), 1e-12);
 assertEquals(7.0, result.getEntry(2, 1), 1e-12);
 assertEquals(8.0, result.getEntry(2, 2), 1e-12);
 }

 @Test
 public void testGetEntryMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
 assertEquals(2.0, matrix.getEntry(1, 1), 1e-12);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetEntryMethodWithInvalidIndex() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.getEntry(2, 2);
 fail("Unexpected exception not thrown");
 }

 @Test
 public void testSetEntryMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 matrix.setEntry(0, 1, 3.0);
 assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
 assertEquals(2.0, matrix.getEntry(1, 1), 1e-12);
 assertEquals(3.0, matrix.getEntry(0, 1), 1e-12);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetEntryMethodWithInvalidIndex() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 try {
 matrix.setEntry(2, 2, 1.0);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testAddToEntryMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 matrix.addToEntry(0, 0, 3.0);
 assertEquals(4.0, matrix.getEntry(0, 0), 1e-12);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddToEntryMethodWithInvalidIndex() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 try {
 matrix.addToEntry(2, 2, 1.0);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testMultiplyEntryMethod() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 matrix.multiplyEntry(0, 0, 3.0);
 assertEquals(3.0, matrix.getEntry(0, 0), 1e-12);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testMultiplyEntryMethodWithInvalidIndex() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 try {
 matrix.multiplyEntry(2, 2, 3.0);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSetEntryWithLargeValue() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 matrix.setEntry(0, 0, Double.MAX_VALUE + 1.0);
 fail("Unexpected exception not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testSetEntryWithNullMatrix() {
 OpenMapRealMatrix matrix = null;
 try {
 matrix.setEntry(0, 0, 1.0);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSetEntryWithNullValue() {
 OpenMapRealMatrix matrix = createMatrix(2, 2);
 try {
 matrix.setEntry(0, 0, null);
 } catch (NullPointerException e) {
 // expected
 }
 }

}