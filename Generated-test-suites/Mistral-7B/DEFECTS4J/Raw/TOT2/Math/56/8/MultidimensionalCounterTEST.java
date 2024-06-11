import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 // Test constructor
 @Test
 public void testMultidimensionalCounterConstruction() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 assertEquals(3, multidimensionalCounter.getDimension());
 assertEquals(3 * 5 * 7, multidimensionalCounter.getSize());
 }

 // Test getCounts method with valid input
 @Test
 public void testGetCountsValidInput() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 int[] counts = multidimensionalCounter.getCounts(12);
 assertEquals(3, counts.length);
 assertEquals(2, counts[0]);
 assertEquals(1, counts[1]);
 assertEquals(0, counts[2]);
 }

 // Test getCount method with valid input
 @Test
 public void testGetCountValidInput() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 assertEquals(0, multidimensionalCounter.getCount(0, 0, 0));
 assertEquals(1, multidimensionalCounter.getCount(1, 0, 0));
 assertEquals(2, multidimensionalCounter.getCount(2, 0, 0));
 // Test getting count in larger dimensions
 assertEquals(3, multidimensionalCounter.getCount(0, 1, 0));
 assertEquals(4, multidimensionalCounter.getCount(0, 1, 1));
 // Test getting count in last dimension
 assertEquals(5, multidimensionalCounter.getCount(2, 2, 6));
 }

 // Test getCounts method with out of range input
 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 multidimensionalCounter.getCounts(-1);
 }

 // Test getCount method with out of range input
 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 multidimensionalCounter.getCount(-1, 0, 0);
 multidimensionalCounter.getCount(3, 5, 7);
 }

 // Test getCounts method with invalid number of dimensions
 @Test(expected = IllegalArgumentException.class)
 public void testGetCountsInvalidNumberOfDimensions() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 multidimensionalCounter.getCounts(new int[]{3, 5});
 }

 // Test iterator
 @Test
 public void testIterator() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 Iterator iterator = multidimensionalCounter.iterator();
 int count = 0;
 while (iterator.hasNext()) {
 int next = iterator.next();
 assertEquals(count, next);
 ++count;
 }
 }

 // Test iterator with reset
 @Test
 public void testIteratorReset() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 Iterator iterator = multidimensionalCounter.iterator();
 int count = 0;
 while (iterator.hasNext()) {
 int next = iterator.next();
 assertEquals(count, next);
 ++count;
 }
 iterator.reset();
 count = 0;
 while (iterator.hasNext()) {
 int next = iterator.next();
 assertEquals(count, next);
 ++count;
 }
 }

 // Test iterator with getCount and getCounts
 @Test
 public void testIteratorGetCountGetCounts() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 Iterator iterator = multidimensionalCounter.iterator();
 int count = 0;
 while (iterator.hasNext()) {
 int next = iterator.next();
 assertEquals(count, next);
 int[] counts = iterator.getCounts();
 assertEquals(3, counts.length);
 assertEquals(count % 5, counts[0]);
 assertEquals(count % 7, counts[1]);
 assertEquals(count % 3, counts[2]);
 ++count;
 }
 }

 // Test size
 @Test
 public void testSize() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 assertEquals(3 * 5 * 7, multidimensionalCounter.getSize());
 }

 // Test sizes
 @Test
 public void testSizes() {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 5, 7});
 int[] sizes = multidimensionalCounter.getSizes();
 assertEquals(3, sizes.length);
 assertEquals(3, sizes[0]);
 assertEquals(5, sizes[1]);
 assertEquals(7, sizes[2]);
 }
}