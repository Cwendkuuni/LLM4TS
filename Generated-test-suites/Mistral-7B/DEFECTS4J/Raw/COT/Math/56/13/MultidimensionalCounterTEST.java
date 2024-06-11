import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Test
 public void testMultidimensionalCounterConstruction() {
 int[] sizes = {2, 4, 3};
 counter = new MultidimensionalCounter(sizes);
 Assert.assertEquals(2 * 4 * 3, counter.getSize());
 Assert.assertEquals(3, counter.getDimension());
 Assert.assertArrayEquals(sizes, counter.getSizes());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSizes() {
 int[] sizes = {-1, 4, 3};
 try {
 counter = new MultidimensionalCounter(sizes);
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 }

 @Test
 public void testMultidimensionalCounterGetCount() {
 int[] counts = new int[3];
 counts[0] = 1;
 counts[1] = 0;
 counts[2] = 2;
 counter = new MultidimensionalCounter(counts);

 Assert.assertEquals(0, counter.getCount());

 for (int i = 0; i < counter.getSize(); i++) {
 int count = counter.getCount();
 int[] expected = new int[counter.getDimension()];
 counter.getCounts(count);
 Assert.assertArrayEquals(expected, counts);
 }
 }

 @Test
 public void testMultidimensionalCounterGetCounts() {
 int[] expected = new int[3];
 int[] sizes = {2, 4, 3};
 counter = new MultidimensionalCounter(sizes);

 for (int i = 0; i < counter.getSize(); i++) {
 int[] counts = counter.getCounts(i);
 Assert.assertArrayEquals(expected, counts);
 ++expected[last];
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testMultidimensionalCounterGetCountOutOfRange() {
 int[] sizes = {2, 4, 3};
 counter = new MultidimensionalCounter(sizes);

 int index = counter.getSize() + 1;
 counter.getCount(index);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultidimensionalCounterGetCountDimensionMismatch() {
 int[] sizes = {2, 4};
 int[] dimensions = {2, 3};
 counter = new MultidimensionalCounter(sizes);

 int[] counts = new int[3];
 counts[0] = 1;
 counts[1] = 1;
 counts[2] = 1;

 int index = counter.getCount(counts);
 }

 @Test
 public void testMultidimensionalCounterIterator() {
 int[] sizes = {2, 4, 3};
 counter = new MultidimensionalCounter(sizes);

 Iterator<Integer> iterator = counter.iterator();
 int expected = 0;
 while (iterator.hasNext()) {
 int count = iterator.next();
 int[] counts = iterator.getCounts();
 Assert.assertEquals(expected++, count);
 Assert.assertArrayEquals(sizes, counts);
 }
 }

 @Test
 public void testMultidimensionalCounterIteratorGetCount() {
 int[] sizes = {2, 4, 3};
 counter = new MultidimensionalCounter(sizes);
 Iterator<Integer> iterator = counter.iterator();
 int expected = 0;
 while (iterator.hasNext()) {
 int count = iterator.next();
 int actual = iterator.getCount();
 Assert.assertEquals(expected, actual);
 ++expected;
 }
 }

 @Test
 public void testMultidimensionalCounterToString() {
 int[] sizes = {2, 4, 3};
 counter = new MultidimensionalCounter(sizes);
 String expected = "[0][0][0]";
 String actual = counter.toString();
 Assert.assertEquals(expected, actual);

 for (int i = 0; i < counter.getSize(); i++) {
 counter.next();
 expected = "[1][1][1]";
 actual = counter.toString();
 Assert.assertEquals(expected, actual);
 }
 }
}