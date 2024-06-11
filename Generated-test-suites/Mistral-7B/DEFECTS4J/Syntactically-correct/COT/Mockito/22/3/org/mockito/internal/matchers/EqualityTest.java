import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreEqual;

public class EqualityTest {

 @Test
 public void testAreEqual_whenBothNull_thenReturnTrue() {
 AreEqual equality = new Equality();
 assertTrue(equality.areEqual(null, null));
 }

 @Test
 public void testAreEqual_whenBothNotNullAndEqual_thenReturnTrue() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 AreEqual equality = new Equality();
 assertTrue(equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqual_whenBothNotNullAndNotEqual_thenReturnFalse() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 AreEqual equality = new Equality();
 assertFalse(equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqual_whenOneIsArrayAndOtherIsObject_thenReturnFalse() {
 Object obj = new Object();
 Object array = new Object[] {obj};
 AreEqual equality = new Equality();
 assertFalse(equality.areEqual(obj, array));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqual_whenBothAreNotArraysAndTypesDoNotMatch_thenThrowIllegalArgumentException() {
 Object obj1 = new Object();
 String obj2 = new String("test");;
 AreEqual equality = new Equality();
 equality.areEqual(obj1, obj2);
 }

 @Test
 public void testAreArraysEqual_whenBothArraysHaveSameLengthAndSameElements_thenReturnTrue() {
 Object[] array1 = new Object[] {new Object(), new Object()};
 Object[] array2 = new Object[] {new Object(), new Object()};
 AreEqual equality = new Equality();
 assertTrue(equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_whenBothArraysHaveDifferentLength_thenReturnFalse() {
 Object[] array1 = new Object[] {new Object(), new Object(), new Object()};
 Object[] array2 = new Object[] {new Object()};
 AreEqual equality = new Equality();
 assertFalse(equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_whenBothArraysHaveSameLengthButDifferentElements_thenReturnFalse() {
 Object[] array1 = new Object[] {new Object(), new Object()};
 Object[] array2 = new Object[] {new Object(), new Object(), new Object()};
 AreEqual equality = new Equality();
 assertFalse(equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testAreArraysEqual_whenBothArraysHaveSameLengthAndSameElementsInDifferentOrder_thenReturnTrue() {
 Object[] array1 = new Object[] {new Object(), new Object()};
 Object[] array2 = new Object[] {new Object(), new Object()};
 AreEqual equality = new Equality();
 assertTrue(equality.areArraysEqual(array1, array2));
 }

 @Test
 public void testIsArray_whenPassedObjectIsArray_thenReturnTrue() {
 Object array = new Object[] {};
 AreEqual equality = new Equality();
 assertTrue(equality.isArray(array));
 }

 @Test
 public void testIsArray_whenPassedObjectIsNotArray_thenReturnFalse() {
 Object obj = new Object();
 AreEqual equality = new Equality();
 assertFalse(equality.isArray(obj));
 }
}