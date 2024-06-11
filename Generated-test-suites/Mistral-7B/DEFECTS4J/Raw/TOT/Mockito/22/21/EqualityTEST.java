import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.areEqual; // static import for readability
import org.mockito.internal.matchers.Equality.isArray;
import org.mockito.internal.matchers.Equality.areArrayElementsEqual;
import org.mockito.internal.matchers.Equality.areArrayLengthsEqual;

import static org.junit.Assert.*;

public class EqualityTest {

 @Test
 public void testEqualsObjects() {
 String str1 = new String("mockito");
 String str2 = new String("mockito");
 Object obj1 = new Object();
 Object obj2 = new Object();

 assertTrue(areEqual(str1, str2));
 assertTrue(areEqual(obj1, obj1));
 assertTrue(areEqual(obj1, obj2));
 }

 @Test
 public void testEqualsNullObjects() {
 Object obj1 = null;
 Object obj2 = null;

 assertTrue(areEqual(obj1, obj2));
 }

 @Test
 public void testEqualsNullAndNonNullObject() {
 Object obj1 = null;
 String str1 = new String("mockito");

 assertFalse(areEqual(obj1, str1));
 }

 @Test
 public void testArraysEqualsSameObjects() {
 Object[] arr1 = new Object[]{new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object()};

 assertTrue(areArraysEqual(arr1, arr2));
 }

 @Test
 public void testArraysEqualsDifferentLengths() {
 Object[] arr1 = new Object[]{new Object(), new Object(), new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object()};

 assertFalse(areArraysEqual(arr1, arr2));
 }

 @Test
 public void testArraysEqualsDifferentTypes() {
 Object[] arr1 = new Object[]{new Object(), new Object(), new Object()};
 int[] arr2 = new int[]{1, 2, 3};

 assertFalse(areArraysEqual(arr1, arr2));
 }

 @Test
 public void testArraysEqualsSameTypeDifferentValues() {
 Object[] arr1 = new Object[]{1, 2, 3};
 Object[] arr2 = new Object[]{2, 3, 1};

 assertFalse(areArraysEqual(arr1, arr2));
 }

 @Test
 public void testArraysEqualsSameTypeSameValues() {
 Object[] arr1 = new Object[]{1, 2, 3};
 Object[] arr2 = new Object[]{1, 2, 3};

 assertTrue(areArraysEqual(arr1, arr2));
 }

 @Test
 public void testArraysEqualsNullArray() {
 Object[] arr1 = null;
 Object[] arr2 = new Object[]{1, 2, 3};

 assertFalse(areArraysEqual(arr1, arr2));
 }

 @Test
 public void testArraysEqualsNullElement() {
 Object[] arr1 = new Object[]{new Object(), null, new Object()};
 Object[] arr2 = new Object[]{new Object(), new Object(), new Object()};

 assertFalse(areArraysEqual(arr1, arr2));
 }
}