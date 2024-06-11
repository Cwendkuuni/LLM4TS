import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

 @Test
 public void testAreEqual_NullsAreEqual() {
 // null equals null
 assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqual_NullAndNonNull() {
 // null is not equal to non-null
 assertFalse(Equality.areEqual(null, new Object()[0]));
 }

 @Test
 public void testAreEqual_SameObjects() {
 // same object is equal to itself
 Object obj = new Object();
 assertTrue(Equality.areEqual(obj, obj));
 }

 @Test
 public void testAreEqual_DifferentObjects() {
 // different objects are not equal
 Object obj1 = new Object();
 Object obj2 = new Object();
 assertFalse(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreArraysEqual_SameArrays() {
 // same arrays are equal
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_DifferentArraysLength() {
 // arrays with different lengths are not equal
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3, 4};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_DifferentArraysContents() {
 // arrays with different contents are not equal
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_MixedTypes() {
 // arrays with different types are not equal
 Object[] array1 = new Object[]{1, "two", new Object()};
 Object[] array2 = new Object[]{"one", 2, new Object()};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test(expected = NullPointerException.class)
 public void testAreEqual_NullArray() {
 // NullPointerException when passing null array to areArraysEqual method
 assertFalse(Equality.areArraysEqual(null, new Object[]{1, 2, 3}));
 }

 @Test(expected = ArrayStoreException.class)
 public void testAreArraysEqual_MismatchedTypes() {
 // ArrayStoreException when comparing arrays with mismatched types
 Object[] array1 = new Object[]{1, "two"};
 int[] array2 = new int[]{1, 2};
 assertFalse(Equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreEqual_EmptyArrays() {
 // empty arrays are equal
 Object[] array1 = new Object[0];
 Object[] array2 = new Object[0];
 assertTrue(Equality.areEqual(array1, array2));
 }
}