import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreEqual;

@SuppressWarnings("unchecked")
public class EqualityTest {

 // Test areEqual method with null inputs
 @Test
 public void testAreEqualWithNullInputs() {
AreEqual areEqual = new AreEqual();
 Truth result = areEqual.areEqual(null, null);
 Truth expected = Truth.TRUE;
 assertTrue(result.getValue());
 assertEquals(expected, result);
 }

 // Test areEqual method with same non-null input
 @Test
 public void testAreEqualWithSameInput() {
 Object input = new Object();
AreEqual areEqual = new AreEqual();
 Truth result = areEqual.areEqual(input, input);
 Truth expected = Truth.TRUE;
 assertTrue(result.getValue());
 assertEquals(expected, result);
 }

 // Test areEqual method with different types
 @Test
 public void testAreEqualWithDifferentTypes() {
 Object obj1 = new Object();
 int obj2 = 1;
AreEqual areEqual = new AreEqual();
 Truth result = areEqual.areEqual(obj1, obj2);
 Truth expected = Truth.FALSE;
 assertFalse(result.getValue());
 assertEquals(expected, result);
 }

 // Test areEqual method with different non-null inputs
 @Test
 public void testAreEqualWithDifferentInputs() {
 Object obj1 = new Object();
 Object obj2 = new Object();
AreEqual areEqual = new AreEqual();
 Truth result = areEqual.areEqual(obj1, obj2);
 Truth expected = Truth.FALSE;
 assertFalse(result.getValue());
 assertEquals(expected, result);
 }

 // Test areArraysEqual method with arrays of same size and equal elements
 @Test
 public void testAreArraysEqualWithSameArrays() {
 Object[] arr1 = new Object[] { 1, 2, 3 };
 Object[] arr2 = new Object[] { 1, 2, 3 };
Equality equality = new Equality();
 Truth result = equality.areArraysEqual(arr1, arr2);
 Truth expected = Truth.TRUE;
 assertTrue(result.getValue());
 assertEquals(expected, result);
 }

 // Test areArraysEqual method with arrays of same size but different elements
 @Test
 public void testAreArraysEqualWithDifferentArraysSameSize() {
 Object[] arr1 = new Object[] { 1, 2, 3 };
 Object[] arr2 = new Object[] { 2, 2, 3 };
Equality equality = new Equality();
 Truth result = equality.areArraysEqual(arr1, arr2);
 Truth expected = Truth.FALSE;
 assertFalse(result.getValue());
 assertEquals(expected, result);
 }

 // Test areArraysEqual method with arrays of different sizes
 @Test
 public void testAreArraysEqualWithDifferentSizes() {
 Object[] arr1 = new Object[] { 1, 2, 3 };
 Object[] arr2 = new Object[] { 1, 2, 3, 4 };
Equality equality = new Equality();
 Truth result = equality.areArraysEqual(arr1, arr2);
 Truth expected = Truth.FALSE;
 assertFalse(result.getValue());
 assertEquals(expected, result);
 }

 // Test areArraysEqual method with arrays with null elements
 @Test
 public void testAreArraysEqualWithArraysContainingNulls() {
 Object[] arr1 = new Object[] { 1, null, 3 };
 Object[] arr2 = new Object[] { 1, null, 3, null };
Equality equality = new Equality();
 Truth result = equality.areArraysEqual(arr1, arr2);
 Truth expected = Truth.FALSE;
 assertFalse(result.getValue());
 assertEquals(expected, result);
 }

 // Test areArraysEqual method with multidimensional arrays
 @Test
 public void testAreArraysEqualWithMultidimensionalArrays() {
 Object[][] arr1 = new Object[][] { { 1, 2 }, { 3, 4 } };
 Object[][] arr2 = new Object[][] { { 1, 2 }, { 3, 4 } };
Equality equality = new Equality();
 Truth result = equality.areArraysEqual(arr1, arr2);
 Truth expected = Truth.TRUE;
 assertTrue(result.getValue());
 assertEquals(expected, result);
 }

 // Test areArraysEqual method with multidimensional arrays of different sizes
 @Test
 public void testAreArraysEqualWithMultidimensionalArraysDifferentSizes() {
 Object[][] arr1 = new Object[][] { { 1, 2 }, { 3, 4 } };
 Object[][] arr2 = new Object[][] { { 1, 2 }, { 3, 4 }, { 5, 6 } };
Equality equality = new Equality();
 Truth result = equality.areArraysEqual(arr1, arr2);
 Truth expected = Truth.FALSE;
 assertFalse(result.getValue());
 assertEquals(expected, result);
 }
}