import org.apache.commons.math3.exception.*;
import org.junit.*;
import java.util.*;

public class MultidimensionalCounterTest {

  @Test
  public void testGetDimension() {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    int expected = 3;
    int result = counter.getDimension();
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testGetCountsWithValidIndex() throws OutOfRangeException {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    int[] expected = {2, 1, 0};
    int[] result = counter.getCounts(10);
    Assert.assertArrayEquals(expected, result);
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountsWithInvalidIndex() throws OutOfRangeException {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    counter.getCounts(20);
  }

  @Test
  public void testGetCountsWithValidDimensions() throws OutOfRangeException, DimensionMismatchException {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    int expected = 18;
    int result = counter.getCount(2, 1, 1);
    Assert.assertEquals(expected, result);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testGetCountsWithInvalidDimensions() throws OutOfRangeException, DimensionMismatchException {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    counter.getCount(2, 1);
  }

  @Test(expected = OutOfRangeException.class)
  public void testGetCountsWithInvalidIndices() throws OutOfRangeException, DimensionMismatchException {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    counter.getCount(5, 1, 1);
  }

  @Test
  public void testGetSize() {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    int expected = 30;
    int result = counter.getSize();
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testGetSizes() {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    int[] expected = {5, 3, 2};
    int[] result = counter.getSizes();
    Assert.assertArrayEquals(expected, result);
  }

  @Test
  public void testToString() {
    MultidimensionalCounter counter = new MultidimensionalCounter(5, 3, 2);
    String expected = "[0][1][0]";
    String result = counter.toString();
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testIterator() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
    Iterator<Integer> iterator = counter.iterator();
    List<Integer> counts = new ArrayList<>();
    while (iterator.hasNext()) {
      counts.add(iterator.next());
    }
    Integer[] expected = {1, 2, 3, 4};
    Integer[] result = counts.toArray(new Integer[0]);
    Assert.assertArrayEquals(expected, result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIteratorRemove() {
    MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
    Iterator<Integer> iterator = counter.iterator();
    iterator.remove();
  }
}