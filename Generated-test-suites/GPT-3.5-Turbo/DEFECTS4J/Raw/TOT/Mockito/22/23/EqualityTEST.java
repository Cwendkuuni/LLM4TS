import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

  // Test case for areEqual() method

  @Test
  public void testAreEqual_WhenBothNull_ShouldReturnTrue() {
    assertTrue(Equality.areEqual(null, null));
  }

  @Test
  public void testAreEqual_WhenOnlyOneNull_ShouldReturnFalse() {
    assertFalse(Equality.areEqual(null, "hello"));
  }

  @Test
  public void testAreEqual_WhenBothObjectsNonNullAndEqual_ShouldReturnTrue() {
    Integer num1 = new Integer(5);
    Integer num2 = new Integer(5);
    assertTrue(Equality.areEqual(num1, num2));
  }

  @Test
  public void testAreEqual_WhenBothObjectsNonNullButNotEqual_ShouldReturnFalse() {
    Integer num1 = new Integer(5);
    Integer num2 = new Integer(10);
    assertFalse(Equality.areEqual(num1, num2));
  }

  // Test case for areArraysEqual() method

  @Test
  public void testAreArraysEqual_WhenArrayLengthsEqualAndAllElementsEqual_ShouldReturnTrue() {
    int[] arr1 = new int[]{1, 2, 3};
    int[] arr2 = new int[]{1, 2, 3};
    assertTrue(Equality.areArraysEqual(arr1, arr2));
  }

  @Test
  public void testAreArraysEqual_WhenArrayLengthsNotEqual_ShouldReturnFalse() {
    int[] arr1 = new int[]{1, 2, 3};
    int[] arr2 = new int[]{1, 2};
    assertFalse(Equality.areArraysEqual(arr1, arr2));
  }

  @Test
  public void testAreArraysEqual_WhenArrayElementsNotEqual_ShouldReturnFalse() {
    int[] arr1 = new int[]{1, 2, 3};
    int[] arr2 = new int[]{1, 4, 3};
    assertFalse(Equality.areArraysEqual(arr1, arr2));
  }

  // Test case for areArrayLengthsEqual() method

  @Test
  public void testAreArrayLengthsEqual_WhenArrayLengthsEqual_ShouldReturnTrue() {
    int[] arr1 = new int[]{1, 2, 3};
    int[] arr2 = new int[]{4, 5, 6};
    assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
  }

  @Test
  public void testAreArrayLengthsEqual_WhenArrayLengthsNotEqual_ShouldReturnFalse() {
    int[] arr1 = new int[]{1, 2, 3};
    int[] arr2 = new int[]{4, 5};
    assertFalse(Equality.areArrayLengthsEqual(arr1, arr2));
  }

  // Test case for areArrayElementsEqual() method

  @Test
  public void testAreArrayElementsEqual_WhenAllElementsEqual_ShouldReturnTrue() {
    int[] arr1 = new int[]{1, 2, 3};
    int[] arr2 = new int[]{1, 2, 3};
    assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
  }

  @Test
  public void testAreArrayElementsEqual_WhenElementsNotEqual_ShouldReturnFalse() {
    int[] arr1 = new int[]{1, 2, 3};
    int[] arr2 = new int[]{1, 4, 3};
    assertFalse(Equality.areArrayElementsEqual(arr1, arr2));
  }

  // Test case for isArray() method

  @Test
  public void testIsArray_WhenObjectIsArray_ShouldReturnTrue() {
    int[] arr = new int[]{1, 2, 3};
    assertTrue(Equality.isArray(arr));
  }

  
  @Test
  public void testIsArray_WhenObjectIsNotArray_ShouldReturnFalse() {
    Integer num = new Integer(5);
    assertFalse(Equality.isArray(num));
  }
}