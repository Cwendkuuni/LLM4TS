import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

 // Test case for areEqual(Object, Object) method
 @Test
 public void testAreEqualObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Assert.assertTrue(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualEqualObjects() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Assert.assertTrue(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualNullObjects() {
 Object obj1 = null;
 Object obj2 = null;
 Assert.assertTrue(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualNullAndObject() {
 Object obj1 = null;
 Object obj2 = new Object();
 Assert.assertFalse(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualObjectAndNull() {
 Object obj1 = new Object();
 Object obj2 = null;
 Assert.assertFalse(Equality.areEqual(obj1, obj2));
 }

 // Test case for areEqual(Object[], Object[]) method
 @Test
 public void testAreArraysEqualEmptyArrays() {
 Object[] array1 = new Object[0];
 Object[] array2 = new Object[0];
 Assert.assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithSameLengthAndSameElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithSameLengthButDifferentElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqualArraysWithDifferentLengths() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAreArraysEqualArrayIndexOutOfBoundsException() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[1];
 Equality.areArraysEqual(array1, array2);
 }

 // Test case for isArray(Object) method
 @Test
 public void testIsArrayTrue() {
 Object array = new Object[0];
 Assert.assertTrue(Equality.isArray(array));
 }

 @Test
 public void testIsArrayFalse() {
 Object obj = new Object();
 Assert.assertFalse(Equality.isArray(obj));
 }
}