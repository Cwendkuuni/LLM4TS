import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreEqual;

public class EqualityTest {

 @Test
 public void testAreEqual_ObjectsAreEqualToEachOther() {
 AreEqual areEqual = new Equality();
 Object obj1 = new Object();
 Object obj2 = obj1;

 assertTrue(areEqual.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqual_ObjectsAreNotEqualToNull() {
 AreEqual areEqual = new Equality();
 assertFalse(areEqual.areEqual(null, new Object()));
 }

 @Test
 public void testAreEqual_ObjectsAreNotEqualToDifferentObjects() {
 AreEqual areEqual = new Equality();
 Object obj1 = new Object();
 Object obj2 = new Object();

 assertFalse(areEqual.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqual_ArraysAreEqualToEachOther() {
 AreEqual areEqual = new Equality();
 Object array1 = new Object[] { 1, 2, 3 };
 Object array2 = new Object[] { 1, 2, 3 };

 assertTrue(areEqual.areEqual(array1, array2));
 }

 @Test
 public void testAreEqual_ArraysAreNotEqualToDifferentArrays() {
 AreEqual areEqual = new Equality();
 Object array1 = new Object[] { 1, 2, 3 };
 Object array2 = new Object[] { 1, 2, 4 };

 assertFalse(areEqual.areEqual(array1, array2));
 }

 @Test(expected = NullPointerException.class)
 public void testAreEqual_ArrayIsNull() {
 AreEqual areEqual = new Equality();
 Object array1 = null;
 Object array2 = new Object[] { 1, 2, 3 };

 areEqual.areArraysEqual(array1, array2);
 }

 @Test(expected = NullPointerException.class)
 public void testAreEqual_ArrayElementIsNull() {
 AreEqual areEqual = new Equality();
 Object array1 = new Object[] { 1, null, 3 };
 Object array2 = new Object[] { 1, 2, 3 };

 areEqual.areArraysEqual(array1, array2);
 }

 @Test(expected = ArrayStoreException.class)
 public void testAreEqual_ArrayElementTypeMismatch() {
 AreEqual areEqual = new Equality();
 Object array1 = new Object[] { 1, "two" };
 Object array2 = new Object[] { 1, 2 };

 areEqual.areArraysEqual(array1, array2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqual_ArrayLengthsAreNotEqual() {
 AreEqual areEqual = new Equality();
 Object array1 = new Object[] { 1, 2, 3 };
 Object array2 = new Object[] { 1, 2 };

 areEqual.areArrayLengthsEqual(array1, array2);
 }
}