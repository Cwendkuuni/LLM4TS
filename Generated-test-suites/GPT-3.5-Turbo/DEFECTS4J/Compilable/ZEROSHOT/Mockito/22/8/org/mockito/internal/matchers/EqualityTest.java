package org.mockito.internal.matchers;
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

   @Test
   public void testAreEqual_NullObjects_ReturnsTrue() {
      boolean result = Equality.areEqual(null, null);
      assertTrue(result);
   }
   
   @Test
   public void testAreEqual_NullAndNonNullObject_ReturnsFalse() {
      boolean result = Equality.areEqual(null, new Object());
      assertFalse(result);
   }
   
   @Test
   public void testAreEqual_Arrays_ReturnsTrue() {
      Object[] array1 = new Object[] {1, 2, 3};
      Object[] array2 = new Object[] {1, 2, 3};
      boolean result = Equality.areEqual(array1, array2);
      assertTrue(result);
   }
   
   @Test
   public void testAreEqual_Arrays_DifferentLength_ReturnsFalse() {
      Object[] array1 = new Object[] {1, 2, 3};
      Object[] array2 = new Object[] {1, 2, 3, 4};
      boolean result = Equality.areEqual(array1, array2);
      assertFalse(result);
   }
   
   @Test
   public void testAreEqual_Arrays_DifferentElements_ReturnsFalse() {
      Object[] array1 = new Object[] {1, 2, 3};
      Object[] array2 = new Object[] {1, 2, 4};
      boolean result = Equality.areEqual(array1, array2);
      assertFalse(result);
   }
   
   @Test
   public void testAreEqual_NonArrayObjects_ReturnsTrue() {
      Object obj1 = new Object();
      Object obj2 = new Object();
      boolean result = Equality.areEqual(obj1, obj2);
      assertTrue(result);
   }
   
   @Test
   public void testAreEqual_NonArrayObjects_DifferentObjects_ReturnsFalse() {
      Object obj1 = new Object();
      Object obj2 = new Object();
      boolean result = Equality.areEqual(obj1, obj2);
      assertFalse(result);
   }
   
   @Test
   public void testAreArrayLengthsEqual_ArraysWithEqualLength_ReturnsTrue() {
      Object[] array1 = new Object[] {1, 2, 3};
      Object[] array2 = new Object[] {4, 5, 6};
      boolean result = Equality.areArrayLengthsEqual(array1, array2);
      assertTrue(result);
   }
   
   @Test
   public void testAreArrayLengthsEqual_ArraysWithDifferentLengths_ReturnsFalse() {
      Object[] array1 = new Object[] {1, 2, 3};
      Object[] array2 = new Object[] {4, 5, 6, 7};
      boolean result = Equality.areArrayLengthsEqual(array1, array2);
      assertFalse(result);
   }
   
   @Test
   public void testAreArrayElementsEqual_ArraysWithEqualElements_ReturnsTrue() {
      Object[] array1 = new Object[] {1, 2, 3};
      Object[] array2 = new Object[] {1, 2, 3};
      boolean result = Equality.areArrayElementsEqual(array1, array2);
      assertTrue(result);
   }
   
   @Test
   public void testAreArrayElementsEqual_ArraysWithDifferentElements_ReturnsFalse() {
      Object[] array1 = new Object[] {1, 2, 3};
      Object[] array2 = new Object[] {1, 2, 4};
      boolean result = Equality.areArrayElementsEqual(array1, array2);
      assertFalse(result);
   }
   
   @Test
   public void testIsArray_Array_ReturnsTrue() {
      Object[] array = new Object[3];
      boolean result = Equality.isArray(array);
      assertTrue(result);
   }
   
   @Test
   public void testIsArray_NonArrayObject_ReturnsFalse() {
      Object obj = new Object();
      boolean result = Equality.isArray(obj);
      assertFalse(result);
   }
}