import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructor() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 assertEquals(3, counter.getDimension());
 assertArrayEquals(new int[]{3, 4, 5}, counter.getSizes());
 assertEquals(1440, counter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeSizes() {
 new MultidimensionalCounter(new int[]{-1, 4, 5});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorZeroSizes() {
 new MultidimensionalCounter(new int[]{0, 4, 5});
 }

 @Test
 public void testIterator() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 Iterator it = counter.iterator();
 int count = 0;
 int[] expected = new int[]{0, 0, 0};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
 count = it.next().intValue();
 expected = new int[]{0, 0, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{0, 0, 2};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{0, 1, 0};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
 it.next();
 it.next();
 it.next();
 expected = new int[]{1, 0, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{1, 0, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{1, 0, 2};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
 it.next();
 it.next();
 it.next();
 it.next();
 expected = new int[]{1, 1, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{1, 1, 1};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 expected = new int[]{2, 0, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{2, 0, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{2, 0, 2};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 expected = new int[]{2, 1, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{2, 1, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{2, 1, 2};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 expected = new int[]{2, 2, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{2, 2, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{2, 2, 2};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 expected = new int[]{3, 0, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{3, 0, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{3, 0, 2};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 it.next();
 expected = new int[]{3, 1, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{3, 1, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{3, 1, 2};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
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
 expected = new int[]{3, 2, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{3, 2, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{3, 2, 2};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());

 it = counter.iterator();
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
 expected = new int[]{3, 3, 0};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{3, 3, 1};
 assertArrayEquals(expected, it.getCounts());
 assertTrue(it.hasNext());

 it.next();
 expected = new int[]{3, 3, 2};
 assertArrayEquals(expected, it.getCounts());
 assertFalse(it.hasNext());
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 int[] c = new int[]{1, 2, 3};
 int index = 0;
 int expected = 0;
 assertEquals(expected, counter.getCount(c));

 index = 1;
 expected = 3;
 assertEquals(expected, counter.getCount(c));

 index = 2;
 expected = 12;
 assertEquals(expected, counter.getCount(c));

 index = 3;
 expected = 14;
 assertEquals(expected, counter.getCount(c));

 c = new int[]{1, 3, 3};
 index = 0;
 expected = 0;
 assertEquals(expected, counter.getCount(c));

 index = 1;
 expected = 3;
 assertEquals(expected, counter.getCount(c));

 index = 2;
 expected = 5;
 assertEquals(expected, counter.getCount(c));

 index = 3;
 expected = 15;
 assertEquals(expected, counter.getCount(c));

 c = new int[]{-1, 1, 2};
 assertNull(counter.getCount(c));

 c = new int[]{3, 4, 5};
 assertNull(counter.getCount(c));

 c = new int[]{3, 5, 4};
 assertArrayEquals(new int[]{3, 5}, counter.getCounts(counter.getCount(c)));
 assertEquals(125, counter.getCount(c));
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 int[] c = new int[]{-1, 1, 2};
 counter.getCount(c);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.getCount(new int[]{5});
 }

 @Test
 public void testToString() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 String expected = "[0][0][0]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[0][0][1]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[0][0][2]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[0][1][0]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[0][1][1]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[0][1][2]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[1][0][0]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[1][0][1]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[1][0][2]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[1][1][0]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[1][1][1]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[1][1][2]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][0][0]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][0][1]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][0][2]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][1][0]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][1][1]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][1][2]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][2][0]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][2][1]";
 assertEquals(expected, counter.toString());

 counter = new MultidimensionalCounter(new int[]{3, 4, 5});
 counter.next();
 expected = "[2][2][2]";
 assertEquals(expected, counter.toString());
 }

}