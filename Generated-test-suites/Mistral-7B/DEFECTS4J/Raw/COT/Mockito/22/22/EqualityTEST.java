import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreEqual;

public class EqualityTest {

 @Test
 public void testAreEqual_nullNull() {
 AreEqual areEqual = new AreEqual();
 Assert.assertTrue(areEqual.areEqual(null, null));
 }

 @Test
 public void testAreEqual_nullObject() {
 AreEqual areEqual = new AreEqual();
 Assert.assertFalse(areEqual.areEqual(null, new Object()));
 }

 @Test
 public void testAreEqual_objectNull() {
 AreEqual areEqual = new AreEqual();
 Assert.assertFalse(areEqual.areEqual(new Object(), null));
 }

 @Test
 public void testAreEqual_differentObjects() {
 AreEqual areEqual = new AreEqual();
 Assert.assertFalse(areEqual.areEqual(new Object(), new Object()));
 }

 @Test
 public void testAreEqual_sameObjects() {
 AreEqual areEqual = new AreEqual();
 Object obj1 = new Object();
 Assert.assertTrue(areEqual.areEqual(obj1, obj1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqual_invalidArgument() {
 AreEqual areEqual = new AreEqual();
 Assert.assertThrows(IllegalArgumentException.class, () -> areEqual.areEqual(null, new int[0]));
 }

 @Test
 public void testAreArraysEqual_sameArrays() {
 AreEqual areEqual = new AreEqual();
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Assert.assertTrue(areEqual.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_differentArraysLength() {
 AreEqual areEqual = new AreEqual();
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3, 4};
 Assert.assertFalse(areEqual.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_differentArraysContents() {
 AreEqual areEqual = new AreEqual();
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 Assert.assertFalse(areEqual.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_sameArraysContents() {
 AreEqual areEqual = new AreEqual();
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Assert.assertTrue(areEqual.areArraysEqual(array1, array2));
 }

 @Test
 public void testIsArray_true() {
 Assert.assertTrue(Equality.isArray(new int[0]));
 }

 @Test
 public void testIsArray_false() {
 Assert.assertFalse(Equality.isArray(new String("hello")));
 }
}