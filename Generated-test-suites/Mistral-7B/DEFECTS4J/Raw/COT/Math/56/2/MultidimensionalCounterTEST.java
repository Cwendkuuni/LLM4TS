import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Test
 public void testConstructor() {
 int[] size = {2, 4, 3};
 counter = new MultidimensionalCounter(size);
 assertEquals(3, counter.getDimension());
 assertEquals(2 * 4 * 3, counter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSize() {
 int[] size = {-1, 2, 3};
 counter = new MultidimensionalCounter(size);
 fail("Should not reach this point");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroSize() {
 int[] size = {0, 2, 3};
 counter = new MultidimensionalCounter(size);
 fail("Should not reach this point");
 }

 @Test
 public void testGetCounts() {
 int[] size = {2, 4, 3};
 counter = new MultidimensionalCounter(size);
 int[] index = {0, 0, 0};
 int[] expected = {0, 0, 0};
 assertArrayEquals(expected, counter.getCounts(counter.getCount(index)));
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountNegativeIndex() {
 int[] size = {2, 4, 3};
 counter = new MultidimensionalCounter(size);
 int[] index = {-1, 0, 0};
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 int[] size = {2, 4, 3};
 counter = new MultidimensionalCounter(size);
 int[] index = {2, 2, 2};
 }

 @Test
 public void testGetCountsDifferentSize() {
 int[] size1 = {2, 4, 3};
 int[] size2 = {3, 4, 3};
 MultidimensionalCounter counter1 = new MultidimensionalCounter(size1);
 MultidimensionalCounter counter2 = new MultidimensionalCounter(size2);
 int[] index = {0, 0, 0};
 int[] expected1 = {0, 0, 0};
 int[] expected2 = {0, 0, 0};
 assertArrayEquals(expected1, counter1.getCounts(counter1.getCount(index)));
 assertArrayEquals(expected2, counter2.getCounts(counter2.getCount(index)));
 assertNotSame(expected1, expected2);
 }

 @Test
 public void testGetCountsSameSizeButDifferentIndex() {
 int[] size = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(size);
 int[] index1 = {0, 0, 0};
 int[] index2 = {1, 0, 0};
 int[] expected1 = {0, 0, 0};
 int[] expected2 = {1, 0, 0};
 assertArrayEquals(expected1, counter.getCounts(counter.getCount(index1)));
 assertArrayEquals(expected2, counter.getCounts(counter.getCount(index2)));
 assertNotSame(expected1, expected2);
 }

 @Test
 public void testGetCountsDifferentDimensions() {
 int[] size1 = {2, 4};
 int[] size2 = {3, 3};
 MultidimensionalCounter counter1 = new MultidimensionalCounter(size1);
 MultidimensionalCounter counter2 = new MultidimensionalCounter(size2);
 int[] index = {0, 0};
 int[] expected1 = {0, 0};
 int[] expected2 = {0, 0};
 assertArrayEquals(expected1, counter1.getCounts(counter1.getCount(index)));
 assertArrayEquals(expected2, counter2.getCounts(counter2.getCount(index)));
 assertNotSame(expected1, expected2);
 assertNotEquals(counter1.getSize(), counter2.getSize());
 }

 @Test
 public void testIteratorHasNext() {
 int[] size = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = counter.iterator();
 it.hasNext(); // true
 it.next(); // increment counter and return the new index
 it.hasNext(); // true
 it.next(); // increment counter and return the new index
 it.hasNext(); // false
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorRemove() {
 int[] size = {2, 4, 3};
 MultidimensionalCounter counter = new MultidimensionalCounter(size);
 MultidimensionalCounter.Iterator it = counter.iterator();
 it.next(); // increment counter and return the new index
 it.remove();
 }

}