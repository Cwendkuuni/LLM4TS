import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

  private OpenMapRealMatrix matrix;

  @Before
  public void setUp() {
     matrix = new OpenMapRealMatrix(3, 3);
  }

  @Test
  public void testConstructor() {
    assertEquals(3, matrix.getRowDimension());
    assertEquals(3, matrix.getColumnDimension());
  }

  @Test
  public void testCopy() {
    OpenMapRealMatrix copy = matrix.copy();
    assertEquals(matrix.getRowDimension(), copy.getRowDimension());
    assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    assertEquals(matrix.getEntry(0, 0), copy.getEntry(0, 0), 0.0);
  }

  @Test
  public void testCreateMatrix() {
    OpenMapRealMatrix result = matrix.createMatrix(2, 2);
    assertEquals(2, result.getRowDimension());
    assertEquals(2, result.getColumnDimension());
    assertEquals(0.0, result.getEntry(0, 0), 0.0);
  }

  @Test
  public void testGetColumnDimension() {
    assertEquals(3, matrix.getColumnDimension());
  }

  @Test
  public void testAdd() {
    OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
    other.setEntry(0, 0, 1.0);
    other.setEntry(1, 1, 2.0);
    OpenMapRealMatrix result = matrix.add(other);
    assertEquals(1.0, result.getEntry(0, 0), 0.0);
    assertEquals(2.0, result.getEntry(1, 1), 0.0);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testAddDimensionMismatch() {
    OpenMapRealMatrix other = new OpenMapRealMatrix(3, 2);
    matrix.add(other);
  }

  @Test
  public void testSubtract() {
    OpenMapRealMatrix other = new OpenMapRealMatrix(3, 3);
    other.setEntry(0, 0, 1.0);
    other.setEntry(1, 1, 2.0);
    OpenMapRealMatrix result = matrix.subtract(other);
    assertEquals(-1.0, result.getEntry(0, 0), 0.0);
    assertEquals(-2.0, result.getEntry(1, 1), 0.0);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testSubtractDimensionMismatch() {
    OpenMapRealMatrix other = new OpenMapRealMatrix(3, 2);
    matrix.subtract(other);
  }

  @Test
  public void testMultiply() {
    RealMatrix other = new OpenMapRealMatrix(3, 3);
    other.setEntry(0, 0, 2.0);
    other.setEntry(1, 1, 3.0);
    RealMatrix result = matrix.multiply(other);
    assertEquals(0.0, result.getEntry(0, 0), 0.0);
    assertEquals(0.0, result.getEntry(1, 1), 0.0);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testMultiplyDimensionMismatch() {
    RealMatrix other = new OpenMapRealMatrix(3, 2);
    matrix.multiply(other);
  }

  @Test
  public void testGetEntry() {
    matrix.setEntry(0, 0, 1.0);
    assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetEntryInvalidRow() {
    matrix.getEntry(3, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetEntryInvalidColumn() {
    matrix.getEntry(0, 3);
  }

  @Test
  public void testSetEntry() {
    matrix.setEntry(0, 0, 1.0);
    assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetEntryInvalidRow() {
    matrix.setEntry(3, 0, 1.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetEntryInvalidColumn() {
    matrix.setEntry(0, 3, 1.0);
  }

  @Test
  public void testAddToEntry() {
    matrix.setEntry(0, 0, 1.0);
    matrix.addToEntry(0, 0, 2.0);
    assertEquals(3.0, matrix.getEntry(0, 0), 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddToEntryInvalidRow() {
    matrix.addToEntry(3, 0, 1.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddToEntryInvalidColumn() {
    matrix.addToEntry(0, 3, 1.0);
  }

  @Test
  public void testMultiplyEntry() {
    matrix.setEntry(0, 0, 3.0);
    matrix.multiplyEntry(0, 0, 2.0);
    assertEquals(6.0, matrix.getEntry(0, 0), 0.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testMultiplyEntryInvalidRow() {
    matrix.multiplyEntry(3, 0, 1.0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testMultiplyEntryInvalidColumn() {
    matrix.multiplyEntry(0, 3, 1.0);
  }

}