import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
  private MultidimensionalCounter counter;

  @Before
  public void setUp() {
    counter = new MultidimensionalCounter(2, 3, 4);
  }

  @Test
  public void testGetDimension() {
    assertEquals(3, counter.getDimension());
  }

  @Test
  public void testGetSize() {
    assertEquals(2 * 3 * 4, counter.getSize());
  }
  
  @Test
  public void testGetSizes() {
    assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
  }

  @Test
  public void testGetCounts() {
    assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
    assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
    assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
    assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
    assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
    assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountsInvalidIndexLow() {
    counter.getCounts(-1);
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountsInvalidIndexHigh() {
    counter.getCounts(24);
  }

  @Test
  public void testGetCount() {
    assertEquals(0, counter.getCount(0, 0, 0));
    assertEquals(1, counter.getCount(0, 0, 1));
    assertEquals(2, counter.getCount(0, 0, 2));
    assertEquals(3, counter.getCount(0, 1, 0));
    assertEquals(12, counter.getCount(1, 0, 0));
    assertEquals(23, counter.getCount(1, 3, 2));
  }

  @Test
  public void testIterator() {
    MultidimensionalCounter.Iterator iterator = counter.iterator();

    assertEquals(true, iterator.hasNext());
    assertEquals(0, (int) iterator.next());
    assertEquals(1, (int) iterator.next());
    assertEquals(2, (int) iterator.next());
    assertEquals(3, (int) iterator.next());
    assertEquals(4, (int) iterator.next());
    assertEquals(5, (int) iterator.next());
    assertEquals(6, (int) iterator.next());
    assertEquals(7, (int) iterator.next());
    assertEquals(8, (int) iterator.next());
    assertEquals(9, (int) iterator.next());
    assertEquals(10, (int) iterator.next());
    assertEquals(11, (int) iterator.next());
    assertEquals(12, (int) iterator.next());
    assertEquals(13, (int) iterator.next());
    assertEquals(14, (int) iterator.next());
    assertEquals(15, (int) iterator.next());
    assertEquals(16, (int) iterator.next());
    assertEquals(17, (int) iterator.next());
    assertEquals(18, (int) iterator.next());
    assertEquals(19, (int) iterator.next());
    assertEquals(20, (int) iterator.next());
    assertEquals(21, (int) iterator.next());
    assertEquals(22, (int) iterator.next());
    assertEquals(23, (int) iterator.next());
    assertEquals(false, iterator.hasNext());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIteratorRemove() {
    MultidimensionalCounter.Iterator iterator = counter.iterator();
    iterator.remove();
  }

  @Test(expected = DimensionMismatchException.class)
  public void testGetCountDimensionMismatch() {
    counter.getCount(0, 0);
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountInvalidDimension0() {
    counter.getCount(-1, 0, 0);
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountInvalidDimension1() {
    counter.getCount(0, 0, 4);
  }

  @Test
  public void testToString() {
    assertEquals("[0][0][0]", counter.toString());
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorInvalidSize() {
    new MultidimensionalCounter(2, -3, 4);
  }
}

