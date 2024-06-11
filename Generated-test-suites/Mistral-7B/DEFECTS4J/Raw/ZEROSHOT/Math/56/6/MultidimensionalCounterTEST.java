import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructorWithValidSizes() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4, 2);
 assertEquals(3, mdc.getDimension());
 int[] expectedSizes = {3, 4, 2};
 assertArrayEquals(expectedSizes, mdc.getSizes());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeOrZeroSizes() {
 new MultidimensionalCounter(3, -1, 2);
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4, 2);
 int[] expectedCounts = {0, 0, 0};
 int index = 0;
 assertEquals(expectedCounts, mdc.getCounts(index++));
 expectedCounts = new int[]{0, 0, 1};
 assertEquals(expectedCounts, mdc.getCounts(index++));
 expectedCounts = new int[]{0, 1, 0};
 assertEquals(expectedCounts, mdc.getCounts(index++));
 expectedCounts = new int[]{0, 1, 1};
 assertEquals(expectedCounts, mdc.getCounts(index++));
 expectedCounts = new int[]{1, 0, 0};
 assertEquals(expectedCounts, mdc.getCounts(index++));
 expectedCounts = new int[]{1, 0, 1};
 assertEquals(expectedCounts, mdc.getCounts(index++));
 expectedCounts = new int[]{1, 1, 0};
 assertEquals(expectedCounts, mdc.getCounts(index++));
 expectedCounts = new int[]{1, 1, 1};
 assertEquals(expectedCounts, mdc.getCounts(index++));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongNumberOfDimensions() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4, 2);
 mdc.getCounts(new int[]{3, 4});
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsWithOutOfRangeValues() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4, 2);
 mdc.getCounts(new int[]{-1, 1, 1});
 mdc.getCounts(new int[]{3, 5, 1});
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4, 2);
 int expectedIndex = 0;
 int expectedCount = 0;
 assertEquals(expectedCount, mdc.getCount(new int[]{expectedIndex}));
 expectedCount = 1;
 mdc.next();
 assertEquals(expectedCount, mdc.getCount(new int[]{expectedIndex}));
 expectedIndex = 1;
 expectedCount = 0;
 assertEquals(expectedCount, mdc.getCount(new int[]{expectedIndex}));
 mdc.next();
 mdc.next();
 assertEquals(expectedCount, mdc.getCount(new int[]{expectedIndex}));
 expectedIndex = 2;
 expectedCount = 0;
 assertEquals(expectedCount, mdc.getCount(new int[]{expectedIndex}));
 mdc.next();
 mdc.next();
 mdc.next();
 mdc.next();
 assertEquals(expectedCount, mdc.getCount(new int[]{expectedIndex}));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongNumberOfDimensionsAgain() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4, 2);
 mdc.getCount(new int[2]);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsWithOutOfRangeValuesAgain() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4, 2);
 mdc.getCount(new int[]{-1, -1, -1});
 mdc.getCount(new int[]{3, 5, 5});
 }

 @Test
 public void testSize() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(3, 4, 2);
 int expectedSize = 3 * 4 * 2;
 assertEquals(expectedSize, mdc.getSize());
 }
}