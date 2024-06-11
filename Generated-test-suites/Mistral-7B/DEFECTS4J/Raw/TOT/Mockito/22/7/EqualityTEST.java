import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

 @Test
 public void testAreEqual_SameObjects() {
 Object o1 = new Object();
 Object o2 = o1;
 assertTrue(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_NullObjects() {
 Object o1 = null;
 Object o2 = null;
 assertTrue(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_ObjectsWithSameContent() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqual_ObjectsWithDifferentClasses() {
 Object o1 = new Object();
 int o2 = 1;
 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_ObjectsWithDifferentLenghts() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqual_ObjectsWithDifferentElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAreArrayElementsEqual_IndexOutOfBounds() {
 Object[] array = new Object[1];
 Equality.areArrayElementsEqual(array, new Object[0]);
 }

 @Test
 public void testAreArraysEqual_EmptyArrays() {
 Object[] array1 = new Object[0];
 Object[] array2 = new Object[0];
 assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_ArraysWithSameContent() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertTrue(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_ArraysWithDifferentLenghts() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_ArraysWithDifferentClasses() {
 Object[] array1 = new Object[]{1, 2, 3};
 int[] array2 = new int[]{1, 2, 3};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_ArraysWithDifferentElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testIsArray_NullObject() {
 assertFalse(Equality.isArray(null));
 }

 @Test
 public void testIsArray_NonArrayObject() {
 Object o = new Object();
 assertFalse(Equality.isArray(o));
 }

 @Test
 public void testIsArray_ArrayObject() {
 Object[] array = new Object[0];
 assertTrue(Equality.isArray(array));
 }
}