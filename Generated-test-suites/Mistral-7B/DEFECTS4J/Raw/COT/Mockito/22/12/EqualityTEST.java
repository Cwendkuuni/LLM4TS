import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreArraysEqualArgumentMatcher;

public class EqualityTest {

 @Test
 public void testAreEqual_whenBothNull_thenReturnTrue() {
 // Given: both objects are null
 Assert.assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqual_whenBothNotArray_thenCallEqualsMethod() {
 // Given: both objects are not arrays
 Object obj1 = new Object();
 Object obj2 = new Object();

 // When: call areEqual method
 boolean result = Equality.areEqual(obj1, obj2);

 // Then: check if equals method of objects was called
 verify(obj1).equals(obj2);
 // and assert that the result is false because we assume equals method returns false for different objects
 Assert.assertFalse(result);
 }

 @Test
 public void testAreEqual_whenBothArraysOfSameElements_thenReturnTrue() {
 // Given: both objects are arrays of the same elements
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};

 // When: call areEqual method
 boolean result = Equality.areEqual(array1, array2);

 // Then: check that result is true
 Assert.assertTrue(result);
 }

 @Test
 public void testAreEqual_whenBothArraysOfDifferentLength_thenReturnFalse() {
 // Given: both objects are arrays of different lengths
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2};

 // When: call areEqual method
 boolean result = Equality.areEqual(array1, array2);

 // Then: check that result is false
 Assert.assertFalse(result);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAreEqual_whenArray1ShorterThanArray2_thenThrowArrayIndexOutOfBoundsException() {
 // Given: array1 is shorter than array2
 Object[] array1 = new Object[]{1, 2};
 Object[] array2 = new Object[]{1, 2, 3};

 // When: call areEqual method
 Equality.areArraysEqual(array1, array2);

 // Then: exception should be thrown
 //NB: JUnit 4 does not support testing exceptions directly, so we use try-catch block in the test method itself
 }

 @Test(expected = ArrayStoreException.class)
 public void testAreEqual_whenArray1ContainsNonMatchingTypes_thenThrowArrayStoreException() {
 // Given: array1 contains elements of different types than array2
 Object[] array1 = new Object[]{"1", new Integer(1)};
 Object[] array2 = new Object[]{new Integer(1), 1};

 // When: call areEqual method
 try {
 Equality.areArraysEqual(array1, array2);
 } catch (AssertionError e) {
 // Then: check that correct exception is thrown
 Assert.assertSame(e.getCause(), new ArrayStoreException());
 }
 }

 @Test
 public void testAreArraysEqual_whenArrayLengthsEqual_thenCallAreArrayElementsEqual() {
 // Given: array lengths are equal
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{1, 2, 3};

 AreArraysEqualArgumentMatcher areArraysEqualMatcher = new AreArraysEqualArgumentMatcher();
 // When: call areArraysEqual method
 boolean result = Equality.areArraysEqual(array1, array2);

 // Then: check that areArrayElementsEqual method was called
 verify(areArraysEqualMatcher).areEqual(array1, array2);
 // and assert that the result is true because we assume areArrayElementsEqual returns true for arrays with equal elements
 Assert.assertTrue(result);
 }

 @Test
 public void testAreArrayElementsEqual_whenAllElementsEqual_thenReturnTrue() {
 // Given: all elements are equal
 Object[] array = new Object[]{1, 1, 1};

 AreArraysEqualArgumentMatcher areArraysEqualMatcher = new AreArraysEqualArgumentMatcher();
 // When: call areArrayElementsEqual method
 boolean result = Equality.areArrayElementsEqual(array, array);

 // Then: check that result is true
 Assert.assertTrue(result);
 }

 @Test
 public void testAreArrayElementsEqual_whenElementsNotEqual_thenReturnFalse() {
 // Given: elements are not equal
 Object[] array = new Object[]{1, 1, 2};

 AreArraysEqualArgumentMatcher areArraysEqualMatcher = new AreArraysEqualArgumentMatcher();
 // When: call areArrayElementsEqual method
 boolean result = Equality.areArrayElementsEqual(array, array);

 // Then: check that result is false
 Assert.assertFalse(result);
 }

 @Test
 public void testIsArray_whenObjectIsArray_thenReturnTrue() {
 // Given: object is an array
 Object[] array = new Object[]{1, 2, 3};

 // When: call isArray method
 boolean result = Equality.isArray(array);

 // Then: check that result is true
 Assert.assertTrue(result);
 }

 @Test
 public void testIsArray_whenObjectIsNotArray_thenReturnFalse() {
 // Given: object is not an array
 Object obj = new Object();

 // When: call isArray method
 boolean result = Equality.isArray(obj);

 // Then: check that result is false
 Assert.assertFalse(result);
 }
}