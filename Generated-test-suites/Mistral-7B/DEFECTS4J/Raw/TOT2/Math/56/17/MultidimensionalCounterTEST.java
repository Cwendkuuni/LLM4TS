import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class MultidimensionalCounterTest {

 // Basic test case for constructor
 @Test
 public void testMultidimensionalCounterConstruction() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, counter.getDimension());
 assertEquals(1, counter.size[0]);
 assertEquals(2, counter.size[1]);
 assertEquals(3, counter.size[2]);
 assertEquals(6, counter.getSize());
 }

 // Basic test case for getCounts method
 @Test
 public void testGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] expected = {0, 0, 0};
 assertEquals(expected, counter.getCounts(0));
 expected = new int[]{2, 0, 0};
 assertEquals(expected, counter.getCounts(counter.getSize() - 2));
 expected = new int[]{1, 1, 1};
 assertEquals(expected, counter.getCounts(counter.getSize() - 1));
 }

 // Basic test case for getCount method
 @Test
 public void testGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertEquals(0, counter.getCount(0, 0, 0));
 assertEquals(1, counter.getCount(1, 0, 0));
 assertEquals(2, counter.getCount(1, 0, 1));
 assertEquals(3, counter.getCount(1, 0, 2));
 assertEquals(4, counter.getCount(1, 1, 0));
 assertEquals(5, counter.getCount(1, 1, 1));
 assertEquals(6, counter.getCount(1, 1, 2));
 }

 // Edge case test for getCounts method with negative index
 @Test(expected = OutOfRangeException.class)
 public void testNegativeIndexGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 counter.getCounts(-1);
 }

 // Edge case test for getCount method with negative index
 @Test(expected = OutOfRangeException.class)
 public void testNegativeIndexGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 counter.getCount(-1, 0, 0);
 }

 // Edge case test for getCount method with out of bounds index
 @Test(expected = OutOfRangeException.class)
 public void testOutOfBoundsIndexGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 assertThrows(OutOfRangeException.class, () -> counter.getCount(2, 2, 2));
 }

 // Edge case test for getCount method with sizes that don't match the constructor
 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] sizes = new int[]{2, 3, 4};
 assertThrows(DimensionMismatchException.class, () -> new MultidimensionalCounter(sizes));
 }

 // Edge case test for getCount method with negative sizes
 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSizeGetCount() {
 int[] sizes = new int[]{-1, 2, 3};
 assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(sizes));
 }

 // Combined test case for getCounts and getCount methods
 @Test
 public void testGetCountsAndGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
 int[] counts = counter.getCounts(counter.getSize() - 1);
 int expectedCount = counter.getCount(counts[0], counts[1], counts[2]);
 assertEquals(counter.getSize() - 1, expectedCount);
 }
}