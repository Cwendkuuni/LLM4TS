import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

   private MultidimensionalCounter counter;

   @Before
   public void setUp() {
      counter = new MultidimensionalCounter(2, 3);
   }

   @Test
   public void testGetDimension() {
      assertEquals(2, counter.getDimension());
   }

   @Test
   public void testGetCounts() {
      try {
         int[] counts = counter.getCounts(3);
         assertArrayEquals(new int[]{1, 0}, counts);
      } catch (OutOfRangeException e) {
         fail("Unexpected exception: " + e.getMessage());
      }
   }

   @Test
   public void testGetCounts_OutOfRangeException() {
      try {
         int[] counts = counter.getCounts(7);
         fail("Expected OutOfRangeException");
      } catch (OutOfRangeException e) {
         assertEquals("3 is out of range: [0, 5]", e.getMessage());
      }
   }

   @Test
   public void testGetCount() {
      try {
         int count = counter.getCount(1, 2);
         assertEquals(4, count);
      } catch (OutOfRangeException | DimensionMismatchException e) {
         fail("Unexpected exception: " + e.getMessage());
      }
   }

   @Test
   public void testGetCount_OutOfRangeException() {
      try {
         int count = counter.getCount(1, 5);
         fail("Expected OutOfRangeException");
      } catch (OutOfRangeException e) {
         assertEquals("5 is out of range: [0, 2]", e.getMessage());
      } catch (DimensionMismatchException e) {
         fail("Unexpected exception: " + e.getMessage());
      }
   }

   @Test
   public void testGetCount_DimensionMismatchException() {
      try {
         int count = counter.getCount(1);
         fail("Expected DimensionMismatchException");
      } catch (OutOfRangeException e) {
         fail("Unexpected exception: " + e.getMessage());
      } catch (DimensionMismatchException e) {
         assertEquals("1 is different from the number of dimensions: 2", e.getMessage());
      }
   }

   @Test
   public void testGetSize() {
      assertEquals(6, counter.getSize());
   }

   @Test
   public void testGetSizes() {
      int[] sizes = counter.getSizes();
      assertArrayEquals(new int[]{2, 3}, sizes);
   }

   @Test
   public void testIterator() {
      MultidimensionalCounter.Iterator iterator = counter.iterator();
      assertTrue(iterator.hasNext());
      assertEquals(0, iterator.next().intValue());
      assertEquals(1, iterator.getCount());
      assertArrayEquals(new int[]{0, 0}, iterator.getCounts());
      assertEquals(0, iterator.getCount(0));
      assertEquals(0, iterator.getCount(1));
      assertTrue(iterator.hasNext());
      assertEquals(1, iterator.next().intValue());
      assertEquals(2, iterator.getCount());
      assertArrayEquals(new int[]{0, 1}, iterator.getCounts());
      assertEquals(0, iterator.getCount(0));
      assertEquals(1, iterator.getCount(1));
      assertTrue(iterator.hasNext());
   }

   @Test
   public void testIterator_Remove() {
      MultidimensionalCounter.Iterator iterator = counter.iterator();
      try {
         iterator.remove();
         fail("Expected UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
         // Expected exception
      }
   }

   @Test
   public void testToString() {
      assertEquals("[0][0]", counter.toString());
   }
}