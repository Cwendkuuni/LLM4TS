import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.verification.VerificationMode;

public class EqualityTest {

 @Test
 public void testAreEqual_NullNull() {
 final Object o1 = null;
 final Object o2 = null;
 assertTrue(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_NotNullNull() {
 final Object o1 = new Object();
 final Object o2 = null;
 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_NullNotNull() {
 final Object o1 = null;
 final Object o2 = new Object();
 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqual_SameObjects() {
 final Object o1 = new Object();
 final Object o2 = o1;
 assertTrue(Equality.areEqual(o1, o2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqual_InvalidTypes() {
 final Object o1 = new Object();
 final int o2 = 1;
 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_SameArrays() {
 final Object[] o1 = new Object[] {1, 2, 3};
 final Object[] o2 = new Object[] {1, 2, 3};
 assertTrue(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_DifferentLengths() {
 final Object[] o1 = new Object[] {1, 2, 3};
 final Object[] o2 = new Object[] {1, 2, 3, 4};
 assertFalse(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_DifferentElements() {
 final Object[] o1 = new Object[] {1, 2, 3};
 final Object[] o2 = new Object[] {1, 2, 4};
 assertFalse(Equality.areArraysEqual(o1, o2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreArraysEqual_InvalidTypesArray() {
 final Object o1 = new Object();
 final Object[] o2 = {1, 2, 3};
 assertFalse(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_MixedArrayTypes() {
 final Object[] o1 = new Object[] {"1", 2, 3};
 final Object[] o2 = new Object[] {1, "2", 3};
 assertFalse(Equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testIsArray_IsArray() {
 final Object[] o1 = new Object[0];
 assertTrue(Equality.isArray(o1));
 }

 @Test
 public void testIsArray_IsNotArray() {
 final Object o1 = new Object();
 assertFalse(Equality.isArray(o1));
 }

 @Test
 public void testAreEqual_ArrayWithSingleElement() {
 final Object[] o1 = new Object[] {1};
 final Object o2 = 1;
 assertFalse(Equality.areEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqual_ArrayWithSingleElement() {
 final Object o1 = 1;
 final Object[] o2 = new Object[] {1};
 assertFalse(Equality.areArraysEqual(o1, o2));
 }

 @Test(expected = ArrayStoreException.class)
 public void testAreArraysEqual_InvalidElementType() {
 final Object[] o1 = new Object[1];
 o1[0] = new Integer(1);
 final int o2 = 1;
 assertFalse(Equality.areArraysEqual(o1, new Object[] {o2}));
 }

 @Test
 public void testAreArraysEqual_EqualArraysReversed() {
 final Object[] o1 = new Object[] {1, 2, 3};
 final Object[] o2 = new Object[] {3, 2, 1};
 assertTrue(Equality.areArraysEqual(o1, o2));
 }
}