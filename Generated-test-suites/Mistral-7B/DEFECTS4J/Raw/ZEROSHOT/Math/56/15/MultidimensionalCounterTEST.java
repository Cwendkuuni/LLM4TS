import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructor() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 4, 3});
 assertEquals(3, counter.getDimension());
 assertArrayEquals(new int[] {2, 4, 3}, counter.getSizes());
 assertArrayEquals(new int[] {0, 0, 0}, counter.iterator().next().getCounts());
 assertEquals(24, counter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSize() {
 new MultidimensionalCounter(new int[] {-1, 4, 3});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroSize() {
 new MultidimensionalCounter(new int[] {2, 0, 3});
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 4, 3});
 assertEquals(0, counter.getCount());
 assertEquals(1, counter.getCount(0, 0, 0));
 assertEquals(1, counter.getCount(1, 0, 0));
 assertEquals(2, counter.getCount(0, 0, 1));
 assertEquals(3, counter.getCount(1, 0, 0));
 assertEquals(4, counter.getCount(0, 1, 0));
 assertEquals(12, counter.getCount(1, 3, 2));
 assertEquals(23, counter.getCount(1, 2, 2));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatch() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 4, 3});
 counter.getCount(4);
 }

 @Test(expected = OutOfRangeException.class)
 public void testOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 4, 3});
 counter.getCount(-1, 0, 0);
 counter.getCount(2, 4, 3);
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 4, 3});
 Iterator it = counter.iterator();
 int i = 0;
 while (it.hasNext()) {
 int[] counts = it.next().getCounts();
 assertEquals(dimension, counts.length);
 for (int j = 0; j < dimension; j++) {
 assertEquals(j, counts[j]);
 }
 i++;
 }
 assertEquals(24, i);
 }

 @Test
 public void testGetCountLast() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 4, 3});
 assertEquals(0, counter.getCount(0, 0, 0));
 assertEquals(1, counter.getCount(1, 0, 0));
 assertEquals(2, counter.getCount(0, 0, 1));
 assertEquals(3, counter.getCount(1, 0, 0));
 assertEquals(4, counter.getCount(0, 1, 0));
 assertEquals(12, counter.getCount(1, 3, 2));
 assertEquals(23, counter.getCount(1, 2, 2));
 assertEquals(24, counter.getCount(1, 2, 1));
 }
}