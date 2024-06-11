import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 @Test
 public void testMultidimensionalCounterConstruction() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
 assertEquals(3, mdc.getDimension());
 int[] sizes = mdc.getSizes();
 assertEquals(3, sizes.length);
 assertEquals(2, sizes[0]);
 assertEquals(2, sizes[1]);
 assertEquals(8, mdc.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSize() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(-1, 2, 2);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterZeroSize() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(0, 2, 2);
 }

 @Test
 public void testMultidimensionalCounterGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
 int[] counts = mdc.getCounts(0);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(0, counts[2]);
 int count = mdc.getCount(0, 0, 0);
 assertEquals(0, count);
 count = mdc.getCount(2, 1, 1);
 assertEquals(4, count);
 }

 @Test
 public void testMultidimensionalCounterGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
 int[] counts = mdc.getCounts(8);
 assertEquals(2, counts[0]);
 assertEquals(1, counts[1]);
 assertEquals(1, counts[2]);
 }

 @Test(expected = OutOfRangeException.class)
 public void testMultidimensionalCounterOutOfRangeGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
 mdc.getCount(-1, 2, 1);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultidimensionalCounterDimensionMismatch() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
 mdc.getCount(4, 2, 2);
 }

 @Test
 public void testMultidimensionalCounterIterator() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
 Iterator it = mdc.iterator();
 int count = 0;
 while (it.hasNext()) {
 Integer next = it.next();
 assertEquals(count++, next.intValue());
 }
 it = mdc.iterator();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 assertFalse(it.hasNext());
 }

 @Test
 public void testMultidimensionalCounterIteratorGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
 Iterator it = mdc.iterator();
 it.next();
 it.next();
 int count = it.getCount();
 assertEquals(2, count);
 }

 @Test
 public void testMultidimensionalCounterToString() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
 assertEquals("[0][0][0]", mdc.toString());
 }
}