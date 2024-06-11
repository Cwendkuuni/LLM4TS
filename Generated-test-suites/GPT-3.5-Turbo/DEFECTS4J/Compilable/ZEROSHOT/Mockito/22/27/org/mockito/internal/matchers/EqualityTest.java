package org.mockito.internal.matchers;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;


public class EqualityTest {

   @Test
   public void testAreEqual_WithNullValues_ShouldReturnTrue(){
      assertTrue(Equality.areEqual(null, null));
   }

   @Test
   public void testAreEqual_WithNullAndNonNullValues_ShouldReturnFalse(){
      assertFalse(Equality.areEqual(null, "hello"));
   }
   
   @Test
   public void testAreEqual_WithNonNullAndNullValues_ShouldReturnFalse(){
      assertFalse(Equality.areEqual("hello", null));
   }
   
   @Test
   public void testAreEqual_WithEqualNonArrayObjects_ShouldReturnTrue(){
      assertTrue(Equality.areEqual("hello", "hello"));
   }
   
   @Test
   public void testAreEqual_WithNonEqualNonArrayObjects_ShouldReturnFalse(){
      assertFalse(Equality.areEqual("hello", "world"));
   }
   
   @Test
   public void testAreEqual_WithEqualArrayObjects_ShouldReturnTrue(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 3};
      assertTrue(Equality.areEqual(array1, array2));
   }
   
   @Test
   public void testAreEqual_WithNonEqualArrayObjects_ShouldReturnFalse(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 4};
      assertFalse(Equality.areEqual(array1, array2));
   }
   
   @Test
   public void testAreEqual_WithArrayAndNonArrayObjects_ShouldReturnFalse(){
      int[] array = {1, 2, 3};
      assertFalse(Equality.areEqual(array, "hello"));
   }
   
   @Test
   public void testAreEqual_WithArrayAndNullValues_ShouldReturnFalse(){
      int[] array = {1, 2, 3};
      assertFalse(Equality.areEqual(array, null));
   }

   @Test
   public void testAreEqual_WithArrayAndNonArrayObjectsOfDifferentLength_ShouldReturnFalse(){
      int[] array = {1, 2, 3};
      int[] array2 = {1, 2};
      assertFalse(Equality.areEqual(array, array2));
   }

   @Test
   public void testAreEqual_WithArrayAndNonEqualArrayObjects_ShouldReturnFalse(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 4};
      assertFalse(Equality.areEqual(array1, array2));
   }
   
   @Test
   public void testAreArraysEqual_WithEqualArrayObjects_ShouldReturnTrue(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 3};
      assertTrue(Equality.areArraysEqual(array1, array2));
   }
   
   @Test
   public void testAreArraysEqual_WithNonEqualArrayObjects_ShouldReturnFalse(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 4};
      assertFalse(Equality.areArraysEqual(array1, array2));
   }
   
   @Test
   public void testAreArrayLengthsEqual_WithEqualArrayObjects_ShouldReturnTrue(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 3};
      assertTrue(Equality.areArrayLengthsEqual(array1, array2));
   }
   
   @Test
   public void testAreArrayLengthsEqual_WithNonEqualArrayObjects_ShouldReturnFalse(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 4};
      assertFalse(Equality.areArrayLengthsEqual(array1, array2));
   }
   
   @Test
   public void testAreArrayElementsEqual_WithEqualArrayObjects_ShouldReturnTrue(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 3};
      assertTrue(Equality.areArrayElementsEqual(array1, array2));
   }
   
   @Test
   public void testAreArrayElementsEqual_WithNonEqualArrayObjects_ShouldReturnFalse(){
      int[] array1 = {1, 2, 3};
      int[] array2 = {1, 2, 4};
      assertFalse(Equality.areArrayElementsEqual(array1, array2));
   }
  
   @Test
   public void testIsArray_WithArrayObject_ShouldReturnTrue(){
      int[] array = {1, 2, 3};
      assertTrue(Equality.isArray(array));
   }
   
   @Test
   public void testIsArray_WithNonArrayObjects_ShouldReturnFalse(){
      assertFalse(Equality.isArray("hello"));
   }

}