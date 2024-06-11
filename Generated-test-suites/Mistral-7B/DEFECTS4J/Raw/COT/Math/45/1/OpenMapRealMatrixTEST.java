import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

 @Test
 public void testOpenMapRealMatrix_Constructor() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 assertEquals(2, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test
 public void testOpenMapRealMatrix_CopyConstructor() {
 OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
 assertEquals(original.getRowDimension(), copy.getRowDimension());
 assertEquals(original.getColumnDimension(), copy.getColumnDimension());
 }

 @Test
 public void testOpenMapRealMatrix_Addition() {
 OpenMapRealMatrix A = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix B = new OpenMapRealMatrix(2, 3);
 A.setEntry(0, 0, 1.0);
 A.setEntry(0, 1, 2.0);
 A.setEntry(1, 1, 3.0);
 B.setEntry(0, 0, 2.0);
 B.setEntry(1, 1, 4.0);
 OpenMapRealMatrix C = A.add(B);
 assertEquals(2.0, C.getEntry(0, 0), 0.001);
 assertEquals(3.0, C.getEntry(0, 1), 0.001);
 assertEquals(3.0, C.getEntry(1, 1), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddition_DimensionMismatchException() {
 OpenMapRealMatrix A = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix B = new OpenMapRealMatrix(3, 3);
 A.add(B);
 }

 @Test
 public void testOpenMapRealMatrix_Subtraction() {
 OpenMapRealMatrix A = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix B = new OpenMapRealMatrix(2, 3);
 A.setEntry(0, 0, 2.0);
 A.setEntry(0, 1, -1.0);
 A.setEntry(1, 1, 2.0);
 B.setEntry(0, 0, 1.0);
 B.setEntry(0, 1, 1.0);
 B.setEntry(1, 1, 1.0);
 OpenMapRealMatrix C = A.subtract(B);
 assertEquals(1.0, C.getEntry(0, 0), 0.001);
 assertEquals(0.0, C.getEntry(0, 1), 0.001);
 assertEquals(1.0, C.getEntry(1, 1), 0.001);
 }

 @Test
 public void testSubtraction_DimensionMismatchException() {
 OpenMapRealMatrix A = new OpenMapRealMatrix(2, 3);
 RealMatrix B = new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
 A.subtract(B);
 }

 @Test
 public void testOpenMapRealMatrix_Multiplication() {
 OpenMapRealMatrix A = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix B = new OpenMapRealMatrix(3, 2);
 A.setEntry(0, 0, 1.0);
 A.setEntry(0, 1, 2.0);
 A.setEntry(1, 1, 3.0);
 B.setEntry(0, 0, 1.0);
 B.setEntry(0, 1, 2.0);
 B.setEntry(1, 0, 2.0);
 B.setEntry(1, 1, 3.0);
 RealMatrix C = A.multiply(B);
 assertEquals(5.0, C.getEntry(0, 0), 0.001);
 assertEquals(8.0, C.getEntry(0, 1), 0.001);
 assertEquals(9.0, C.getEntry(1, 0), 0.001);
 assertEquals(11.0, C.getEntry(1, 1), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplication_DimensionMismatchException() {
 OpenMapRealMatrix A = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix B = new OpenMapRealMatrix(3, 3);
 A.multiply(B);
 }

 @Test
 public void testOpenMapRealMatrix_GetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
 assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetEntry_InvalidIndex() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.getEntry(2, 2);
 }

 @Test
 public void testOpenMapRealMatrix_SetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.setEntry(1, 1, 2.0);
 matrix.setEntry(1, 0, 3.0);
 assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testSetEntry_InvalidIndex() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.setEntry(2, 2, 3.0);
 }

 @Test
 public void testOpenMapRealMatrix_AddToEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.addToEntry(0, 0, 2.0);
 assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAddToEntry_InvalidIndex() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.addToEntry(2, 2, 3.0);
 }

 @Test
 public void testOpenMapRealMatrix_MultiplyEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.setEntry(0, 0, 1.0);
 matrix.multiplyEntry(0, 0, 2.0);
 assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testMultiplyEntry_InvalidIndex() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.multiplyEntry(2, 2, 2.0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testNumberIsTooLargeException() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 matrix.entries.put(Integer.MAX_VALUE, 1.0);
 }

 @Test
 public void testOpenMapRealMatrix_ComputeKey() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
 assertEquals(0, matrix.computeKey(0, 0));
 assertEquals(1, matrix.computeKey(0, 1));
 assertEquals(2, matrix.computeKey(1, 0));
 assertEquals(3, matrix.computeKey(1, 1));
 }

}