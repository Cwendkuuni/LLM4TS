package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

 @Test
 public void testAreEqual() {
 Object o1 = new Object();
 Object o2 = new Object();
 assertFalse(Equality.areEqual(o1, o2));

 Object o3 = new Object();
 Object o3Copy = new Object(); // ensure they are different objects
 assertFalse(Equality.areEqual(o3, o3Copy));

 Object o4 = "equal string";
 Object o5 = "equal string";
 assertTrue(Equality.areEqual(o4, o5));

 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertTrue(Equality.areEqual(array1, array2));

 Object[] array3 = new Object[]{1, 2, 3};
 Object[] array4 = new Object[]{1, 2, 4};
 assertFalse(Equality.areEqual(array3, array4));

 Object[] array5 = null;
 Object[] array6 = null;
 assertTrue(Equality.areEqual(array5, array6));
 }

 @Test
 public void testAreArraysEqual() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertTrue(Equality.areArraysEqual(array1, array2));

 Object[] array3 = new Object[]{1, 2, 3};
 Object[] array4 = new Object[]{1, 2, 4};
 assertFalse(Equality.areArraysEqual(array3, array4));

 Object[] array5 = null;
 Object[] array6 = null;
 assertTrue(Equality.areArraysEqual(array5, array6));
 }

 @Test
 public void testAreArrayLengthsEqual() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};
 assertTrue(Equality.areArrayLengthsEqual(array1, array2));

 Object[] array3 = new Object[]{1, 2, 3};
 Object[] array4 = new Object[]{1, 2, 4};
 assertFalse(Equality.areArrayLengthsEqual(array3, array4));

 Object[] array5 = null;
 Object[] array6 = null;
 assertTrue(Equality.areArrayLengthsEqual(array5, array6));
 }

 @Test
 public void testIsArray() {
 assertTrue(Equality.isArray(new int[0][0]));
 assertTrue(Equality.isArray(new String[0]));
 assertTrue(Equality.isArray(new boolean[0]));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAreArrayElementsEqualThrowsExceptionWhenArraysHaveDifferentLengths() {
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};
 Equality.areArrayElementsEqual(array1, array2);
 }
}