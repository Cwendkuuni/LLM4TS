import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MultidimensionalCounterTest {

 // Test constructor
 @Test
 public void testMultidimensionalCounterConstruction() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, counter.getDimension());
 int[] sizes = counter.getSizes();
 assertEquals(1, sizes[0]);
 assertEquals(2, sizes[1]);
 assertEquals(3, sizes[2]);
 }

 // Test Iterator hasNext and next methods
 @Test
 public void testIteratorHasNextAndNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 Iterator it = counter.iterator();
 assertEquals(false, it.hasNext());
 it.next();
 assertEquals(true, it.hasNext());
 it.next();
 assertEquals(false, it.hasNext());
 }

 // Test getCounts method
 @Test
 public void testGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] counts = counter.getCounts(0);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(0, counts[2]);
 counts = counter.getCounts(1);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(1, counts[2]);
 counts = counter.getCounts(2);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(2, counts[2]);
 }

 // Test getCount method
 @Test
 public void testGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(0, counter.getCount(0, 0, 0));
 assertEquals(1, counter.getCount(0, 0, 1));
 assertEquals(2, counter.getCount(0, 0, 2));
 assertEquals(3, counter.getCount(0, 1, 0));
 // ... add more test cases to cover all possible combinations
 }

 // Test getSize method
 @Test
 public void testGetSize() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(27, counter.getSize());
 }

 // Test exception handling for out of range index
 @Test(expected = OutOfRangeException.class)
 public void testOutOfRangeException() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 counter.getCount(-1, 0, 0);
 }

 // Test exception handling for incorrect number of dimensions
 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 counter.getCount(1, 2, 4);
 }
}