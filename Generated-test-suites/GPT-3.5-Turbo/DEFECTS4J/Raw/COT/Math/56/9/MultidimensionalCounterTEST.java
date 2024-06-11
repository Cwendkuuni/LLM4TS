import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;

public class MultidimensionalCounterTest {

  @Test
  public void testIteratorHasNext() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Iterator iterator = counter.iterator();
    assertTrue(iterator.hasNext());

    MultidimensionalCounter emptyCounter = new MultidimensionalCounter();
    Iterator emptyIterator = emptyCounter.iterator();
    assertFalse(emptyIterator.hasNext());
  }

  @Test
  public void testIteratorNext() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Iterator iterator = counter.iterator();
    
    assertEquals(Integer.valueOf(0), iterator.next());
    assertEquals(Integer.valueOf(1), iterator.next());
    assertEquals(Integer.valueOf(2), iterator.next());
    assertEquals(Integer.valueOf(3), iterator.next());
    assertEquals(Integer.valueOf(4), iterator.next());
    assertEquals(Integer.valueOf(5), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIteratorRemove() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    Iterator iterator = counter.iterator();
    iterator.remove();
  }

  @Test
  public void testGetDimension() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    assertEquals(2, counter.getDimension());

    MultidimensionalCounter emptyCounter = new MultidimensionalCounter();
    assertEquals(0, emptyCounter.getDimension());
  }

  @Test
  public void testGetCounts() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    assertArrayEquals(new int[]{0, 0}, counter.getCounts(0));
    assertArrayEquals(new int[]{0, 1}, counter.getCounts(1));
    assertArrayEquals(new int[]{0, 2}, counter.getCounts(2));
    assertArrayEquals(new int[]{1, 0}, counter.getCounts(3));
    assertArrayEquals(new int[]{1, 1}, counter.getCounts(4));
    assertArrayEquals(new int[]{1, 2}, counter.getCounts(5));
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountsOutOfRange() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    counter.getCounts(6);
  }

  @Test
  public void testGetCount() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    assertEquals(0, counter.getCount(0, 0));
    assertEquals(1, counter.getCount(0, 1));
    assertEquals(2, counter.getCount(0, 2));
    assertEquals(3, counter.getCount(1, 0));
    assertEquals(4, counter.getCount(1, 1));
    assertEquals(5, counter.getCount(1, 2));
  }

  @Test(expected = DimensionMismatchException.class)
  public void testGetCountDimensionMismatch() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    counter.getCount(0);
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountOutOfRange() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    counter.getCount(2, 0);
  }

  @Test
  public void testGetSize() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    assertEquals(6, counter.getSize());

    MultidimensionalCounter emptyCounter = new MultidimensionalCounter();
    assertEquals(0, emptyCounter.getSize());
  }

  @Test
  public void testGetSizes() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    assertArrayEquals(new int[]{2, 3}, counter.getSizes());
  }

  @Test
  public void testToString() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
    assertEquals("[0][0]", counter.toString());
  }

}