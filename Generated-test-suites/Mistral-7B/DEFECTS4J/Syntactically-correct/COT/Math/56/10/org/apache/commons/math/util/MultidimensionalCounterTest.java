import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Test
 public void testConstructor() {
 int[] sizes = {2, 4, 3};
 counter = new MultidimensionalCounter(sizes);
 assertEquals(3, counter.getDimension());
 assertArrayEquals(sizes, counter.getSizes());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSizes() {
 int[] sizes = {-1, 4, 3};
 try {
 new MultidimensionalCounter(sizes);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 }

 @Test
 public void testGetCounts() {
 int[] sizes = {2, 4, 3};
 int index = 0;
 int[] counts = counter.getCounts(index);
 assertEquals(3, counts.length);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(0, counts[2]);

 index = 3;
 counts = counter.getCounts(index);
 assertEquals(3, counts.length);
 assertEquals(1, counts[0]);
 assertEquals(3, counts[1]);
 assertEquals(2, counts[2]);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetOutOfRangeCounts() {
 int[] sizes = {2, 4, 3};
 int index = 10;
 try {
 counter.getCounts(index);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // expected
 }
 }

 @Test
 public void testGetCount() {
 int[] sizes = {2, 4, 3};
 int[] counts = {0, 0, 0};
 int index = 0;
 int counterIndex = 0;
 for (int i = 0; i < dimension; i++) {
 index += uniCounterOffset[i] * counts[i];
 }
 int expectedCount = counterIndex;
 int actualCount = counter.getCount(counts);
 assertEquals(expectedCount, actualCount);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountWrongSize() {
 int[] sizes = {2, 4, 3};
 int[] counts = {0, 0, 1};
 try {
 counter.getCount(counts);
 fail("Expected DimensionMismatchException");
 } catch (DimensionMismatchException e) {
 // expected
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetOutOfRangeCount() {
 int[] sizes = {2, 4, 3};
 int[] counts = {2, 0, 2};
 try {
 counter.getCount(counts);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // expected
 }
 }

 @Test
 public void testIterator() {
 int[] sizes = {2, 4, 3};
 counter = new MultidimensionalCounter(sizes);
 Iterator iterator = counter.iterator();
 int index = 0;
 while (iterator.hasNext()) {
 int[] counts = iterator.next().getCounts();
 assertEquals(3, counts.length);
 for (int i = 0; i < dimension; i++) {
 assertEquals(i, counts[i]);
 }
 index++;
 }
 assertEquals(totalSize, index);
 }

}