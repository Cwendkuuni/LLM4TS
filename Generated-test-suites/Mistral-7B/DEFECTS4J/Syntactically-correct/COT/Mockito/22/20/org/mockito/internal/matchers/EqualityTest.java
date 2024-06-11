import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

 // Test areEqual method with null arguments
 @Test
 public void testAreEqualWithNullArguments() {
 Assert.assertTrue(Equality.areEqual(null, null));
 }

 // Test areEqual method with non-null and equal objects
 @Test
 public void testAreEqualWithEqualObjects() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Assert.assertTrue(Equality.areEqual(obj1, obj2));
 }

 // Test areEqual method with non-null and not equal objects
 @Test
 public void testAreEqualWithNotEqualObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Assert.assertFalse(Equality.areEqual(obj1, obj2));
 }

 // Test areEqual method with one array and one object
 @Test
 public void testAreEqualWithArrayAndObject() {
 Object[] array1 = new Object[0];
 Object obj = new Object();
 Assert.assertFalse(Equality.areEqual(array1, obj));
 }

 // Test areEqual method with two arrays of different lengths
 @Test
 public void testAreEqualWithArraysOfDifferentLengths() {
 Object[] array1 = new Object[1];
 Object[] array2 = new Object[2];
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 // Test areEqual method with two arrays of equal lengths but different elements
 @Test
 public void testAreEqualWithArraysOfEqualLengthsButDifferentElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 Assert.assertFalse(Equality.areEqual(array1, array2));
 }

 // Test areEqual method with two arrays of equal lengths and equal elements
 @Test
 public void testAreEqualWithArraysOfEqualLengthsAndEqualElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.areEqual(array1, array2));
 }

 // Test areArraysEqual method with null arguments
 @Test(expected = NullPointerException.class)
 public void testAreArraysEqualWithNullArguments() {
 Equality.areArraysEqual(null, null);
 }

 // Test areArraysEqual method with non-array arguments
 @Test(expected = IllegalArgumentException.class)
 public void testAreArraysEqualWithNonArrayArguments() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Equality.areArraysEqual(obj1, obj2);
 }

 // Test areArraysEqual method with arrays of equal lengths and equal elements
 @Test
 public void testAreArraysEqualWithArraysOfEqualLengthsAndEqualElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 Assert.assertTrue(Equality.areArraysEqual(array1, array2));
 }

 // Test areArraysEqual method with arrays of equal lengths but different elements
 @Test
 public void testAreArraysEqualWithArraysOfEqualLengthsButDifferentElements() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 4};
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 // Test areArraysEqual method with arrays of different lengths
 @Test
 public void testAreArraysEqualWithArraysOfDifferentLengths() {
 Object[] array1 = new Object[1];
 Object[] array2 = new Object[2];
 Assert.assertFalse(Equality.areArraysEqual(array1, array2));
 }

 // Test isArray method with non-array object
 @Test
 public void testIsArrayWithNonArrayObject() {
 Object obj = new Object();
 Assert.assertFalse(Equality.isArray(obj));
 }

 // Test isArray method with array
 @Test
 public void testIsArrayWithArray() {
 Object[] array = new Object[0];
 Assert.assertTrue(Equality.isArray(array));
 }
}