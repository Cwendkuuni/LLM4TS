import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for MultidimensionalCounter
 */
public class MultidimensionalCounterTest {

 // Test constructor
 @Test
 public void testConstructor() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 assertEquals(3, counter.getDimension());
 assertEquals(sizes, counter.getSizes());
 }

 // Test getCounts() with valid indices
 @Test
 public void testGetCounts() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 int[] expected = {0, 0, 0};
 int[] actual = counter.getCounts(0);
 assertArrayEquals(expected, actual);
 }

 // Test getCount() with valid multidimensional indices
 @Test
 public void testGetCount() throws OutOfRangeException {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 int[] indices = {0, 0, 0};
 int expected = 0;
 int actual = counter.getCount(indices);
 assertEquals(expected, actual);
 }

 // Test getCount() with invalid multidimensional indices
 @Test
 public void testGetCountInvalidIndices() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 int[] indices = {-1, 0, 0};
 assertThrows(OutOfRangeException.class, () -> counter.getCount(indices));
 }

 // Test iterator hasNext() with valid multidimensional indices
 @Test
 public void testIteratorHasNext() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 MultidimensionalCounter.Iterator iterator = counter.iterator();
 boolean expected = true;
 boolean actual = iterator.hasNext();
 assertEquals(expected, actual);
 }

 // Test iterator hasNext() with invalid multidimensional indices
 @Test
 public void testIteratorHasNextInvalidIndices() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 MultidimensionalCounter.Iterator iterator = counter.iterator();
 iterator.next();
 boolean expected = false;
 boolean actual = iterator.hasNext();
 assertEquals(expected, actual);
 }

 // Test iterator next() with valid multidimensional indices
 @Test
 public void testIteratorNext() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 MultidimensionalCounter.Iterator iterator = counter.iterator();
 int expected = 0;
 int actual = iterator.next();
 assertEquals(expected, actual);
 }

 // Test iterator next() with invalid multidimensional indices
 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorNextInvalidIndices() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 MultidimensionalCounter.Iterator iterator = counter.iterator();
 iterator.remove();
 iterator.next();
 }

 // Test getCounts() with invalid multidimensional indices
 @Test(expected = OutOfRangeException.class)
 public void testGetCountsInvalidIndices() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 int[] indices = {-1, 0, 0};
 counter.getCounts(indices.length, indices);
 }

 // Test getCount() with invalid unidimensional index
 @Test(expected = OutOfRangeException.class)
 public void testGetCountInvalidIndex() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 int index = sizes[0] * sizes[1] * sizes[2] + 1;
 counter.getCount(index);
 }

 // Test constructor with invalid sizes
 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorInvalidSizes() {
 int[] sizes = {-1, 2, 3};
 assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(sizes));
 }

 // Test getSize()
 @Test
 public void testGetSize() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 int expected = 2 * 4 * 3;
 int actual = counter.getSize();
 assertEquals(expected, actual);
 }

 // Test getSizes()
 @Test
 public void testGetSizes() {
 int[] sizes = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 int[] expected = {2, 4, 3};
 int[] actual = counter.getSizes();
 assertArrayEquals(expected, actual);
 }
}