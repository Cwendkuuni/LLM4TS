import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class OpenMapRealMatrixTest {

 private OpenMapRealMatrix matrix;

 @Before
 public void setUp() {
 matrix = new OpenMapRealMatrix(3, 3);
 }

 @Test
 public void testConstruction() {
 assertEquals(3, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 assertTrue(matrix instanceof OpenMapRealMatrix);
 }

 @Test
 public void testCopy() {
 OpenMapRealMatrix copy = matrix.copy();
 assertEquals(matrix, copy);
 }

 @Test
 public void testCreateMatrix() {
 OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
 assertEquals(2, newMatrix.getRowDimension());
 assertEquals(2, newMatrix.getColumnDimension());
 }

 @Test
 public void testGetColumnDimension() {
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test
 public void testAdd() {
 OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
 m.setEntry(0, 0, 2.0);
 m.setEntry(0, 1, 3.0);
 m.setEntry(1, 1, 4.0);
 matrix.add(m);
 assertEquals(5.0, matrix.getEntry(0, 0), 0.001);
 assertEquals(3.0, matrix.getEntry(0, 1), 0.001);
 assertEquals(4.0, matrix.getEntry(1, 1), 0.001);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAddDimensionMisMatch() {
 RealMatrix m = new BlockRealMatrix(1, 3);
 matrix.add(m);
 }

 @Test
 public void testSubtract() {
 OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
 m.setEntry(0, 0, 2.0);
 m.setEntry(0, 1, 3.0);
 m.setEntry(1, 1, 4.0);
 matrix.subtract(m);
 assertEquals(-5.0, matrix.getEntry(0, 0), 0.001);
 assertEquals(-3.0, matrix.getEntry(0, 1), 0.001);
 assertEquals(-4.0, matrix.getEntry(1, 1), 0.001);
 }

 @Test
 public void testMultiply() {
 OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
 m.setEntry(0, 0, 2.0);
 m.setEntry(0, 1, 3.0);
 m.setEntry(1, 1, 4.0);
 RealMatrix result = matrix.multiply(m);
 assertEquals(rows, result.getRowDimension());
 assertEquals(columns, result.getColumnDimension());
 assertArrayEquals(new double[] {11.0, 13.0, 13.0}, result.getRowVector(0), 0.001);
 assertArrayEquals(new double[] {7.0, 12.0, 13.0}, result.getRowVector(1), 0.001);
 assertArrayEquals(new double[] {7.0, 12.0, 13.0}, result.getColumnVector(0), 0.001);
 assertArrayEquals(new double[] {5.0, 11.0, 13.0}, result.getColumnVector(1), 0.001);
 assertArrayEquals(new double[] {5.0, 11.0, 13.0}, result.getColumnVector(2), 0.001);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultiplyDimensionMisMatch() {
 RealMatrix m = new BlockRealMatrix(1, 2);
 matrix.multiply(m);
 }

 @Test
 public void testGetEntry() {
 assertEquals(0.0, matrix.getEntry(0, 0), 0.001);
 assertEquals(0.0, matrix.getEntry(2, 2), 0.001);
 }

 @Test
 public void testSetEntry() {
 matrix.setEntry(0, 0, 1.0);
 assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test
 public void testAddToEntry() {
 matrix.addToEntry(0, 0, 1.0);
 assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
 matrix.addToEntry(0, 0, 2.0);
 assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test
 public void testMultiplyEntry() {
 matrix.multiplyEntry(0, 0, 2.0);
 assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test
 public void testComputeKey() {
 int key = matrix.computeKey(0, 0);
 assertEquals(0, key % columns);
 assertEquals(0, key / columns);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testNumberIsTooLargeException() {
 OpenMapRealMatrix largeMatrix = new OpenMapRealMatrix(Integer.MAX_VALUE, Integer.MAX_VALUE);
 }

 @Test
 public void testSetNegativeEntry() {
 matrix.setEntry(0, 0, -1.0);
 assertEquals(-1.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test
 public void testAddNegativeEntry() {
 matrix.addToEntry(0, 0, -1.0);
 assertEquals(-1.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test
 public void testMultiplyEntryNegative() {
 matrix.multiplyEntry(0, 0, -2.0);
 assertEquals(-2.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test
 public void testSetEntryOutOfBounds() {
 try {
 matrix.setEntry(3, 3, 0.0);
 fail("Expected an exception");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testAddEntryOutOfBounds() {
 try {
 matrix.addToEntry(3, 3, 0.0);
 fail("Expected an exception");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testMultiplyEntryOutOfBounds() {
 try {
 matrix.multiplyEntry(3, 3, 0.0);
 fail("Expected an exception");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

}