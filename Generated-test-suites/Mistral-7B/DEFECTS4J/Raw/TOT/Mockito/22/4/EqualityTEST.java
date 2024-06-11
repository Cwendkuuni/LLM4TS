import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

 @Test
 public void testAreEqualObjects() {
 Object o1 = new Object();
 Object o2 = new Object();
 Object o3 = o1;

 // typical use case
 assertTrue(Equality.areEqual(o1, o1));
 assertFalse(Equality.areEqual(o1, o2));
 assertFalse(Equality.areEqual(o1, null));

 // edge case 1: null vs null
 assertTrue(Equality.areEqual(null, null));

 // edge case 2: array versus object
 Object array1 = new Object[] { 1, 2, 3 };
 assertFalse(Equality.areEqual(array1, o1));

 // error case: arrays of different lengths
 Object array2 = new Object[] { 1, 2, 3, 4 };
 assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual() {
 Object[] array1 = { 1, 2, 3 };
 Object[] array2 = { 1, 2, 3 };
 Object[] array3 = { 1, 2, 3, 4 };
 Object[] array4 = null;

 // typical use case
 assertTrue(Equality.areArraysEqual(array1, array1));
 assertFalse(Equality.areArraysEqual(array1, array2));

 // edge case 1: arrays of the same length and equal elements
 assertTrue(Equality.areArraysEqual(array1, array2));

 // edge case 2: arrays of different lengths
 assertFalse(Equality.areArraysEqual(array1, array3));

 // edge case 3: null array
 assertFalse(Equality.areArraysEqual(array1, array4));
 }

 @Test
 public void testAreArrayElementsEqual() {
 Object[] array1 = { 1, 2, 3 };
 Object[] array2 = { 1, 2, 3 };
 Object[] array3 = { 1, 2, 3, 4 };
 Object[] array4 = { 1, 2, "three", 3 };
 Object[] array5 = null;

 // typical use case
 for (int i = 0; i < array1.length; i++) {
 assertEquals(array1[i], array2[i]);
 }

 // edge case: arrays with different elements
 assertFalse(Equality.areArraysEqual(array1, array4));

 // error case: null array
 assertFalse(Equality.areArrayElementsEqual(array1, array5));
 }

 @Test
 public void testIsArray() {
 Object o1 = new Object();
 Object[] array1 = new Object[3];

 // typical use case
 assertTrue(Equality.isArray(array1));
 assertFalse(Equality.isArray(o1));
 }
}