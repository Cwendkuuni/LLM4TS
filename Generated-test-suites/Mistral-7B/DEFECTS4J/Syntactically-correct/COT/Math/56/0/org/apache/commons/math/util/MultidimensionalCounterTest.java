import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter multidimensionalCounter;

 @Test
 public void testConstructor() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 assertEquals(3, multidimensionalCounter.getDimension());
 assertEquals(24, multidimensionalCounter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeSizes() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{-1, 3, 4});
 }

 @Test
 public void testIteratorHasNext() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 Iterator it = multidimensionalCounter.iterator();
 it.next();
 assertTrue(it.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 Iterator it = multidimensionalCounter.iterator();
 int count = it.getCount();
 assertEquals(0, count);
 it.next();
 count = it.getCount();
 assertEquals(1, count);
 }

 @Test
 public void testIteratorGetCounts() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 Iterator it = multidimensionalCounter.iterator();
 int[] counts = it.getCounts();
 assertEquals(3, counts.length);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(0, counts[2]);
 it.next();
 counts = it.getCounts();
 assertEquals(1, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(0, counts[2]);
 }

 @Test
 public void testIteratorGetCountLastDimension() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 Iterator it = multidimensionalCounter.iterator();
 it.next();
 it.next();
 int count = it.getCount(2);
 assertEquals(0, count);
 }

 @Test
 public void testIteratorGetCountFirstDimension() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 Iterator it = multidimensionalCounter.iterator();
 int count = it.getCount(0);
 assertEquals(0, count);
 it.next();
 count = it.getCount(0);
 assertEquals(1, count);
 }

 @Test
 public void testGetCountsOutOfRange() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 int[] indices = new int[3];
 indices[2] = 5;
 try {
 multidimensionalCounter.getCounts(indices);
 fail();
 } catch (OutOfRangeException e) {
 // expected
 }
 }

 @Test
 public void testGetCountOutOfRange() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 int index = 25;
 try {
 multidimensionalCounter.getCount(index);
 fail();
 } catch (OutOfRangeException e) {
 // expected
 }
 }

 @Test
 public void testGetCountsDimensionMismatch() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 int[] indices = new int[4];
 try {
 multidimensionalCounter.getCounts(indices);
 fail();
 } catch (DimensionMismatchException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 4});
 Iterator it = multidimensionalCounter.iterator();
 assertEquals("[][][]", multidimensionalCounter.toString());
 it.next();
 assertEquals("[1][][]", multidimensionalCounter.toString());
 it.next();
 it.next();
 assertEquals("[1][1][]", multidimensionalCounter.toString());
 it.next();
 it.next();
 it.next();
 assertEquals("[1][1][1]", multidimensionalCounter.toString());
 }
}