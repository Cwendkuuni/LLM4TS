import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreEqualResult;

public class EqualityTest {

 @Test
 public void testAreEqual_whenBothObjectsAreNull_thenReturnTrue() {
 AreEqualResult result = Equality.areEqual(null, null);
 assertTrue(result.getAreEqual());
 }

 @Test
 public void testAreEqual_whenBothObjectsAreNotNull_andAreEqual_thenReturnTrue() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 AreEqualResult result = Equality.areEqual(obj1, obj2);
 assertTrue(result.getAreEqual());
 }

 @Test
 public void testAreEqual_whenBothObjectsAreNotNull_butAreNotEqual_thenReturnFalse() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 AreEqualResult result = Equality.areEqual(obj1, obj2);
 assertFalse(result.getAreEqual());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqual_whenOneObjectIsNull_andOtherIsNotNull_thenThrowIllegalArgumentException() {
 Object obj1 = new Object();
 AreEqualResult result = Equality.areEqual(obj1, null);
 fail("Expected IllegalArgumentException but got " + result.getAreEqual());
 }

 @Test
 public void testAreEqual_whenOneObjectIsArray_andOtherIsNot_thenReturnFalse() {
 Object obj1 = new Object[1];
 Object obj2 = new Object();
 AreEqualResult result = Equality.areEqual(obj1, obj2);
 assertFalse(result.getAreEqual());
 }

 @Test
 public void testAreEqual_whenBothObjectsAreArrays_andHaveDifferentLength_thenReturnFalse() {
 Object obj1 = new Object[1];
 Object obj2 = new Object[2];
 AreEqualResult result = Equality.areEqual(obj1, obj2);
 assertFalse(result.getAreEqual());
 }

 @Test
 public void testAreEqual_whenBothObjectsAreArrays_andHaveSameLength_butDifferentElements_thenReturnFalse() {
 Object obj1 = new Object[]{1, 2};
 Object obj2 = new Object[]{1, 3};
 AreEqualResult result = Equality.areEqual(obj1, obj2);
 assertFalse(result.getAreEqual());
 }

 @Test
 public void testAreEqual_whenBothObjectsAreArrays_andHaveSameLength_andSameElements_thenReturnTrue() {
 Object obj1 = new Object[]{1, 2};
 Object obj2 = new Object[]{1, 2};
 AreEqualResult result = Equality.areEqual(obj1, obj2);
 assertTrue(result.getAreEqual());
 }

 @Test(expected = ArrayStoreException.class)
 public void testAreArraysEqual_whenPassedDifferentTypesArrays_thenThrowArrayStoreException() {
 Object[] intArray = new Object[]{1, 2, 3};
 String[] stringArray = new String[]{"a", "b", "c"};
 Equality.areArraysEqual(intArray, stringArray);
 fail("Expected ArrayStoreException but got no exception");
 }

 @Test
 public void testAreArraysEqual_whenPassedIdenticalArrays_thenReturnTrue() {
 Object[] intArray = new Object[]{1, 2, 3};
 AreEqualResult result = Equality.areArraysEqual(intArray, intArray);
 assertTrue(result.getAreEqual());
 }

 @Test
 public void testAreArraysEqual_whenPassedEqualArrays_butWithDifferentVariableNames_thenReturnTrue() {
 Object[] intArray1 = new Object[]{1, 2, 3};
 Object[] intArray2 = new Object[]{1, 2, 3};
 AreEqualResult result = Equality.areArraysEqual(intArray1, intArray2);
 assertTrue(result.getAreEqual());
 }

 @Test
 public void testAreArraysEqual_whenPassedEqualArrays_butWithSwappedVariables_thenReturnTrue() {
 Object[] intArray1 = new Object[]{1, 2, 3};
 Object[] intArray2 = new Object[]{3, 2, 1};
 AreEqualResult result = Equality.areArraysEqual(intArray1, intArray2);
 assertTrue(result.getAreEqual());
 }

 @Test
 public void testIsArray_whenObjectIsArray_thenReturnTrue() {
 Object array = new Object[1];
 assertTrue(Equality.isArray(array));
 }

 @Test
 public void testIsArray_whenObjectIsNotArray_thenReturnFalse() {
 Object obj = new Object();
 assertFalse(Equality.isArray(obj));
 }

}