import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

 // Test constructor with valid dimensions
 @Test
 public void testOpenMapRealMatrixConstruction() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
 assertEquals(3, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 }

 // Test getEntry method with valid indices
 @Test
 public void testGetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
 matrix.setEntry(1, 1, 5.0);
 assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
 }

 // Test getEntry method with invalid indices
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetEntryInvalidIndices() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
 matrix.getEntry(3, 3);
 }

 // Test setEntry method with valid indices and value
 @Test
 public void testSetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
 matrix.setEntry(1, 1, 5.0);
 assertEquals(5.0, matrix.getEntry(1, 1), 0.001);
 }

 // Test setEntry method with invalid indices
 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetEntryInvalidIndices() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
 matrix.setEntry(3, 3, 5.0);
 }

 // Test add method with same-sized matrices
 @Test
 public void testAddSameSize() {
 OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
 a.setEntry(0, 0, 1.0);
 a.setEntry(1, 1, 2.0);

 OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
 b.setEntry(0, 0, 1.0);
 b.setEntry(1, 1, 2.0);

 OpenMapRealMatrix result = a.add(b);
 OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
 expected.setEntry(0, 0, 2.0);
 expected.setEntry(0, 1, 0.0);
 expected.setEntry(1, 0, 0.0);
 expected.setEntry(1, 1, 4.0);

 assertEquals(expected, result);
 }

 // Test add method with different-sized matrices
 @Test(expected = IllegalArgumentException.class)
 public void testAddDifferentSize() {
 OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
 a.setEntry(0, 0, 1.0);
 a.setEntry(1, 1, 2.0);

 OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
 b.setEntry(0, 0, 1.0);
 b.setEntry(0, 1, 2.0);
 b.setEntry(0, 2, 3.0);

 OpenMapRealMatrix result = a.add(b);
 }

 // Test multiply method with same-sized matrices
 @Test
 public void testMultiplySameSize() {
 OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
 a.setEntry(0, 0, 1.0);
 a.setEntry(1, 1, 2.0);

 OpenMapRealMatrix b = new OpenMapRealMatrix(2, 2);
 b.setEntry(0, 0, 3.0);
 b.setEntry(1, 1, 4.0);

 OpenMapRealMatrix result = a.multiply(b);
 OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
 expected.setEntry(0, 0, 3.0 * 1.0 + 4.0 * 1.0);
 expected.setEntry(0, 1, 3.0 * 2.0 + 4.0 * 2.0);
 expected.setEntry(1, 0, 1.0 * 3.0 + 2.0 * 3.0);
 expected.setEntry(1, 1, 1.0 * 4.0 + 2.0 * 4.0);

 assertEquals(expected, result);
 }

 // Test multiply method with different-sized matrices
 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyDifferentSize() {
 OpenMapRealMatrix a = new OpenMapRealMatrix(2, 2);
 a.setEntry(0, 0, 1.0);
 a.setEntry(1, 1, 2.0);

 OpenMapRealMatrix b = new OpenMapRealMatrix(2, 3);
 b.setEntry(0, 0, 3.0);
 b.setEntry(0, 1, 4.0);
 b.setEntry(0, 2, 5.0);
 b.setEntry(1, 0, 6.0);
 b.setEntry(1, 1, 7.0);
 b.setEntry(1, 2, 8.0);

 OpenMapRealMatrix result = a.multiply(b);
 }
}