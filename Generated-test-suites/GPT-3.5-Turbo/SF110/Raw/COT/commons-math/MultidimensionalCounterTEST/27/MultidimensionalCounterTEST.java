
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MultidimensionalCounter;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

   @Test
   public void testConstructor() {
      int[] size = {2, 3, 4};
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      
      assertEquals(3, counter.getDimension());
      assertArrayEquals(size, counter.getSizes());
   }
   
   @Test(expected = NotStrictlyPositiveException.class)
   public void testConstructor_withZeroSize() {
      int[] size = {0};
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
   }
   
   @Test
   public void testIterator() {
      int[] size = {2, 3};
      int expectedCount = size[0] * size[1] - 1;
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      MultidimensionalCounter.Iterator iterator = counter.iterator();
      
      for (int i = 0; i < expectedCount; i++) {
         assertTrue(iterator.hasNext());
         assertEquals(i, iterator.getCount());
         iterator.next();
      }
      
      assertFalse(iterator.hasNext());
   }
   
   @Test
   public void testIterator_getCounts() {
      int[] size = {2, 3};
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      MultidimensionalCounter.Iterator iterator = counter.iterator();
      
      int[] expectedCounts = {0, 0};
      
      for (int i = 0; i < size[0] * size[1]; i++) {
         assertArrayEquals(expectedCounts, iterator.getCounts());
         iterator.next();
         
         if (i % size[1] == size[1] - 1) {
            expectedCounts[0]++;
            expectedCounts[1] = 0;
         } else {
            expectedCounts[1]++;
         }
      }
   }
   
   @Test
   public void testGetCounts() throws OutOfRangeException {
      int[] size = {2, 3};
      int totalCounts = size[0] * size[1];
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      
      for (int i = 0; i < totalCounts; i++) {
         int[] expectedCounts = counter.getCounts(i);
         int resultCount = counter.getCount(expectedCounts);
         
         assertArrayEquals(expectedCounts, counter.getCounts(i));
         assertEquals(resultCount, i);
      }
   }
   
   @Test(expected = OutOfRangeException.class)
   public void testGetCounts_withOutOfRangeIndex() throws OutOfRangeException {
      int[] size = {2, 3};
      int totalCounts = size[0] * size[1];
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      
      counter.getCounts(totalCounts);
   }
   
   @Test
   public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
      int[] size = {2, 3};
      int totalCounts = size[0] * size[1];
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      
      for (int i = 0; i < totalCounts; i++) {
         int[] counts = counter.getCounts(i);
         int expectedResult = i;
         
         assertEquals(expectedResult, counter.getCount(counts));
      }
   }
   
   @Test(expected = DimensionMismatchException.class)
   public void testGetCount_withDimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
      int[] size = {2, 3};
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      int[] counts = {1};
      
      counter.getCount(counts);
   }
   
   @Test(expected = OutOfRangeException.class)
   public void testGetCount_withOutOfRangeIndex() throws OutOfRangeException, DimensionMismatchException {
      int[] size = {2, 3};
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      int[] counts = {0, 0, 0};
      
      counter.getCount(counts);
   }
   
   @Test
   public void testGetSize() {
      int[] size = {2, 3, 4};
      int expectedSize = size[0] * size[1] * size[2];
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      
      assertEquals(expectedSize, counter.getSize());
   }
   
   @Test
   public void testToString() {
      int[] size = {2, 3};
      String expectedResult = "[0][0]";
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      
      assertEquals(expectedResult, counter.toString());
   }
   
   @Test
   public void testToString_withLargerSize() {
      int[] size = {2, 3, 4};
      String expectedResult = "[0][0][0]";
      
      MultidimensionalCounter counter = new MultidimensionalCounter(size);
      
      assertEquals(expectedResult, counter.toString());
   }
}
