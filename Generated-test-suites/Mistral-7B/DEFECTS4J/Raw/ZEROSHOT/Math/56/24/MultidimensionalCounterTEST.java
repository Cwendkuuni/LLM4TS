import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 @Test
 public void testMultidimensionalCounterConstructor() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 assertEquals(3, counter.getDimension());
 assertArrayEquals(new int[]{3, 4, 5}, counter.getSizes());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSizes() {
 new MultidimensionalCounter(new int[]{-1, 4, 5});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterZeroSizes() {
 new MultidimensionalCounter(new int[]{0, 4, 5});
 }

 @Test
 public void testMultidimensionalCounterGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 int[] counts = counter.getCounts(0);
 assertEquals(3, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(0, counts[2]);
 }

 @Test
 public void testMultidimensionalCounterGetCountsOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 try {
 counter.getCounts(30);
 fail();
 } catch (OutOfRangeException e) {
 // expected
 }
 }

 @Test
 public void testMultidimensionalCounterGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 int count = counter.getCount(0, 0, 0);
 assertEquals(0, count);
 count = counter.getCount(2, 3, 4);
 assertEquals(15, count);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultidimensionalCounterGetCountDimensionMismatch() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.getCount(new int[]{1, 1, 2});
 }

 @Test(expected = OutOfRangeException.class)
 public void testMultidimensionalCounterGetCountOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 try {
 counter.getCount(3, 3, 3);
 fail();
 } catch (OutOfRangeException e) {
 // expected
 }
 }

 @Test
 public void testMultidimensionalCounterIterator() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 MultidimensionalCounter.Iterator it = counter.iterator();
 int index = 0;
 assertFalse(it.hasNext());
 it.next();
 index++;
 while (it.hasNext()) {
 int count = it.next();
 assertEquals(index, count);
 index++;
 }
 assertFalse(it.hasNext());
 }

 @Test
 public void testMultidimensionalCounterIteratorGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 MultidimensionalCounter.Iterator it = counter.iterator();
 it.next();
 int count = it.getCount();
 assertEquals(1, count);
 it.next();
 count = it.getCount();
 assertEquals(2, count);
 }

 @Test
 public void testMultidimensionalCounterIteratorGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 MultidimensionalCounter.Iterator it = counter.iterator();
 it.next();
 int[] counts = it.getCounts();
 assertArrayEquals(new int[]{1, 0, 0}, counts);
 it.next();
 counts = it.getCounts();
 assertArrayEquals(new int[]{1, 0, 1}, counts);
 }
}