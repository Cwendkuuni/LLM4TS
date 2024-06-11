import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OpenMapRealMatrixTest {

   OpenMapRealMatrix matrix;

   @Before
   public void setUp() {
      matrix = new OpenMapRealMatrix(3, 3);
   }

   @Test
   public void testAdd() {
      OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
      m.setEntry(0, 0, 1.0);
      m.setEntry(0, 1, 2.0);
      m.setEntry(0, 2, 3.0);
      m.setEntry(1, 0, 4.0);
      m.setEntry(1, 1, 5.0);
      m.setEntry(1, 2, 6.0);
      m.setEntry(2, 0, 7.0);
      m.setEntry(2, 1, 8.0);
      m.setEntry(2, 2, 9.0);

      OpenMapRealMatrix result = matrix.add(m);

      assertEquals(1.0, result.getEntry(0, 0), 0.0);
      assertEquals(2.0, result.getEntry(0, 1), 0.0);
      assertEquals(3.0, result.getEntry(0, 2), 0.0);
      assertEquals(4.0, result.getEntry(1, 0), 0.0);
      assertEquals(5.0, result.getEntry(1, 1), 0.0);
      assertEquals(6.0, result.getEntry(1, 2), 0.0);
      assertEquals(7.0, result.getEntry(2, 0), 0.0);
      assertEquals(8.0, result.getEntry(2, 1), 0.0);
      assertEquals(9.0, result.getEntry(2, 2), 0.0);
   }

   @Test
   public void testSubtract() {
      OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
      m.setEntry(0, 0, 1.0);
      m.setEntry(0, 1, 2.0);
      m.setEntry(0, 2, 3.0);
      m.setEntry(1, 0, 4.0);
      m.setEntry(1, 1, 5.0);
      m.setEntry(1, 2, 6.0);
      m.setEntry(2, 0, 7.0);
      m.setEntry(2, 1, 8.0);
      m.setEntry(2, 2, 9.0);

      OpenMapRealMatrix result = matrix.subtract(m);

      assertEquals(-1.0, result.getEntry(0, 0), 0.0);
      assertEquals(-2.0, result.getEntry(0, 1), 0.0);
      assertEquals(-3.0, result.getEntry(0, 2), 0.0);
      assertEquals(-4.0, result.getEntry(1, 0), 0.0);
      assertEquals(-5.0, result.getEntry(1, 1), 0.0);
      assertEquals(-6.0, result.getEntry(1, 2), 0.0);
      assertEquals(-7.0, result.getEntry(2, 0), 0.0);
      assertEquals(-8.0, result.getEntry(2, 1), 0.0);
      assertEquals(-9.0, result.getEntry(2, 2), 0.0);
   }

   @Test
   public void testMultiply() {
      OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
      m.setEntry(0, 0, 1.0);
      m.setEntry(0, 1, 2.0);
      m.setEntry(0, 2, 3.0);
      m.setEntry(1, 0, 4.0);
      m.setEntry(1, 1, 5.0);
      m.setEntry(1, 2, 6.0);
      m.setEntry(2, 0, 7.0);
      m.setEntry(2, 1, 8.0);
      m.setEntry(2, 2, 9.0);

      OpenMapRealMatrix result = matrix.multiply(m);

      assertEquals(0.0, result.getEntry(0, 0), 0.0);
      assertEquals(0.0, result.getEntry(0, 1), 0.0);
      assertEquals(0.0, result.getEntry(0, 2), 0.0);
      assertEquals(0.0, result.getEntry(1, 0), 0.0);
      assertEquals(0.0, result.getEntry(1, 1), 0.0);
      assertEquals(0.0, result.getEntry(1, 2), 0.0);
      assertEquals(0.0, result.getEntry(2, 0), 0.0);
      assertEquals(0.0, result.getEntry(2, 1), 0.0);
      assertEquals(0.0, result.getEntry(2, 2), 0.0);
   }

   @Test
   public void testGetEntry() {
      matrix.setEntry(0, 0, 1.0);

      double result = matrix.getEntry(0, 0);

      assertEquals(1.0, result, 0.0);
   }

   @Test
   public void testSetEntry() {
      matrix.setEntry(0, 0, 1.0);

      double result = matrix.getEntry(0, 0);

      assertEquals(1.0, result, 0.0);
   }

   @Test
   public void testAddToEntry() {
      matrix.setEntry(0, 0, 1.0);
      matrix.addToEntry(0, 0, 2.0);

      double result = matrix.getEntry(0, 0);

      assertEquals(3.0, result, 0.0);
   }

   @Test
   public void testMultiplyEntry() {
      matrix.setEntry(0, 0, 2.0);
      matrix.multiplyEntry(0, 0, 3.0);

      double result = matrix.getEntry(0, 0);

      assertEquals(6.0, result, 0.0);
   }

   @Test
   public void testGetRowDimension() {
      int result = matrix.getRowDimension();

      assertEquals(3, result);
   }

   @Test
   public void testGetColumnDimension() {
      int result = matrix.getColumnDimension();

      assertEquals(3, result);
   }

   @Test
   public void testCopy() {
      matrix.setEntry(0, 0, 1.0);
      OpenMapRealMatrix result = matrix.copy();

      assertEquals(1.0, result.getEntry(0, 0), 0.0);
   }

   @Test
   public void testCreateMatrix() {
      OpenMapRealMatrix result = matrix.createMatrix(3, 3);

      assertEquals(3, result.getRowDimension());
      assertEquals(3, result.getColumnDimension());
   }
}