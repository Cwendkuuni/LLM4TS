import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Test
 public void testConstructor() {
 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 assertEquals(3, counter.getDimension());
 assertEquals(3 * 4 * 5, counter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSizeInConstructor() {
 counter = new MultidimensionalCounter(new int[]{-1, 4, 5});
 }

 @Test
 public void testIterator() {
 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 Iterator iterator = counter.iterator();
 int index = 0;
 while (iterator.hasNext()) {
 int actual = iterator.next();
 assertEquals(index, actual);
 index++;
 }
 }

 @Test
 public void testGetCount() {
 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 int[] expected = new int[]{1, 0, 0};
 int actual = counter.getCount(expected);
 assertEquals(expected[0], actual);
 assertEquals(expected[1], counter.getCount(1, 0));
 assertEquals(expected[2], counter.getCount(1, 0, 0));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountWithWrongDimension() {
 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.getCount(new int[]{1, 1, 1});
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.getCount(new int[]{3, 4, 5});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 Iterator iterator = counter.iterator();
 iterator.next();
 iterator.remove();
 }
}