import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructorZeroDimensions() {
 MultidimensionalCounter mdc = new MultidimensionalCounter();
 assertEquals(0, mdc.getDimension());
 assertArrayEquals(new int[0], mdc.getSizes());
 assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter((int[]) null));
 }

 @Test
 public void testConstructorOneDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(5);
 assertEquals(1, mdc.getDimension());
 assertArrayEquals(new int[]{5}, mdc.getSizes());
 assertEquals(5, mdc.getSize());
 }

 @Test
 public void testConstructorTwoDimensions() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4);
 assertEquals(2, mdc.getDimension());
 assertArrayEquals(new int[]{3, 4}, mdc.getSizes());
 assertEquals(3 * 4, mdc.getSize());
 }

 @Test
 public void testConstructorThreeDimensions() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3, 5);
 assertEquals(3, mdc.getDimension());
 assertArrayEquals(new int[]{2, 3, 5}, mdc.getSizes());
 assertEquals(2 * 3 * 5, mdc.getSize());
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3, 5);
 int[] expected = {5, 3, 0};
 int index = 10;
 int[] actual = mdc.getCounts(index);
 assertEquals(3, actual.length);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3, 5);
 mdc.getCounts(-1);
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3, 5);
 int[] expected = {0, 1, 2};
 int[] c = {0, 1, 2};
 int actual = mdc.getCount(c);
 assertEquals(3, actual);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3, 5);
 int[] c = {0, 1};
 mdc.getCount(c);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3, 5);
 int[] c = {2, 3, 6};
 mdc.getCount(c);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3, 5);
 String expected = "[2][3][5]";
 String actual = mdc.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testIterator() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(2, 3, 5);
 Iterator it = mdc.iterator();
 int i = 0;
 while (it.hasNext()) {
 int actual = it.next();
 int[] actualCount = it.getCounts();
 int[] expectedCount = {i / 5 % 3, i / 5 / 3 % 2, i / 5 / 3 / 2};
 assertEquals(3, actualCount.length);
 assertArrayEquals(expectedCount, actualCount);
 i++;
 }
 }
}