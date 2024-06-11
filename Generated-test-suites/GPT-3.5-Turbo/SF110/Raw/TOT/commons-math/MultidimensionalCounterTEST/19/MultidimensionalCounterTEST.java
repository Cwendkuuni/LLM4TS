import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {

   @Test
   public void testConstructor() {
      try {
         MultidimensionalCounter mc = new MultidimensionalCounter(5, 2, 3);
         assertEquals(3, mc.getDimension());
         assertEquals(30, mc.getSize());
         int[] expectedSizes = {5, 2, 3};
         assertArrayEquals(expectedSizes, mc.getSizes());
      } catch (NotStrictlyPositiveException e) {
         fail("Unexpected NotStrictlyPositiveException");
      }
   }

   @Test(expected = NotStrictlyPositiveException.class)
   public void testConstructor_NotStrictlyPositiveException() throws NotStrictlyPositiveException {
      MultidimensionalCounter mc = new MultidimensionalCounter(-1, 2, 3);
   }

   @Test
   public void testIterator() {
      MultidimensionalCounter mc = new MultidimensionalCounter(2, 2, 3);
      Iterator<Integer> iterator = mc.iterator();
      assertTrue(iterator.hasNext());
      assertEquals(Integer.valueOf(0), iterator.next());
      assertEquals(Integer.valueOf(1), iterator.next());
      assertEquals(Integer.valueOf(2), iterator.next());
      assertEquals(Integer.valueOf(3), iterator.next());
      assertEquals(Integer.valueOf(4), iterator.next());
      assertEquals(Integer.valueOf(5), iterator.next());
      assertEquals(Integer.valueOf(6), iterator.next());
      assertEquals(Integer.valueOf(7), iterator.next());
      assertEquals(Integer.valueOf(8), iterator.next());
      assertEquals(Integer.valueOf(9), iterator.next());
      assertFalse(iterator.hasNext());
   }

   @Test
   public void testGetCounts() {
      MultidimensionalCounter mc = new MultidimensionalCounter(2, 2, 3);
      try {
         int[] counts1 = mc.getCounts(0);
         int[] expectedCounts1 = {0, 0, 0};
         assertArrayEquals(expectedCounts1, counts1);

         int[] counts2 = mc.getCounts(3);
         int[] expectedCounts2 = {1, 0, 0};
         assertArrayEquals(expectedCounts2, counts2);

         int[] counts3 = mc.getCounts(8);
         int[] expectedCounts3 = {0, 1, 2};
         assertArrayEquals(expectedCounts3, counts3);
      } catch (OutOfRangeException e) {
         fail("Unexpected OutOfRangeException");
      }
   }

   @Test(expected = OutOfRangeException.class)
   public void testGetCounts_OutOfRangeException() throws OutOfRangeException {
      MultidimensionalCounter mc = new MultidimensionalCounter(2, 2, 3);
      int[] counts = mc.getCounts(9);
   }

   @Test
   public void testGetCount() {
      MultidimensionalCounter mc = new MultidimensionalCounter(2, 2, 3);
      try {
         int count1 = mc.getCount(0, 0, 0);
         assertEquals(0, count1);

         int count2 = mc.getCount(0, 1, 0);
         assertEquals(4, count2);

         int count3 = mc.getCount(1, 0, 2);
         assertEquals(8, count3);

         int count4 = mc.getCount(1, 1, 1);
         assertEquals(9, count4);
      } catch (OutOfRangeException | DimensionMismatchException e) {
         fail("Unexpected exception");
      }
   }

   @Test(expected = DimensionMismatchException.class)
   public void testGetCount_DimensionMismatchException() throws DimensionMismatchException, OutOfRangeException {
      MultidimensionalCounter mc = new MultidimensionalCounter(2, 2, 3);
      int count = mc.getCount(0, 1);
   }

   @Test(expected = OutOfRangeException.class)
   public void testGetCount_OutOfRangeException() throws DimensionMismatchException, OutOfRangeException {
      MultidimensionalCounter mc = new MultidimensionalCounter(2, 2, 3);
      int count = mc.getCount(1, 2, 1);
   }

   @Test
   public void testToString() {
      MultidimensionalCounter mc = new MultidimensionalCounter(2, 2, 3);
      String expectedString = "[0][0][0]";
      assertEquals(expectedString, mc.toString());
   }

}