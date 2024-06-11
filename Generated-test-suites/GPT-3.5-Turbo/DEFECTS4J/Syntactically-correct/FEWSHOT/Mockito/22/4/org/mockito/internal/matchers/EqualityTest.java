import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
  
  @Test
  public void testAreEqualObjects() {
    Object o1 = new Object();
    Object o2 = new Object();
    assertFalse(Equality.areEqual(o1, o2));
    
    Object o3 = null;
    Object o4 = null;
    assertTrue(Equality.areEqual(o3, o4));
    
    Object o5 = "test";
    Object o6 = "test";
    assertTrue(Equality.areEqual(o5, o6));
    
    Object o7 = "test";
    Object o8 = "test1";
    assertFalse(Equality.areEqual(o7, o8));
  }
  
  @Test
  public void testAreEqualArrays() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};
    assertTrue(Equality.areEqual(arr1, arr2));
    
    String[] arr3 = {"test1", "test2"};
    String[] arr4 = {"test1", "test2"};
    assertTrue(Equality.areEqual(arr3, arr4));
    
    String[] arr5 = {"test1", "test2"};
    String[] arr6 = {"test1", "test3"};
    assertFalse(Equality.areEqual(arr5, arr6));
  }
    
  @Test
  public void testAreArrayLengthsEqual() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};
    assertTrue(Equality.areArrayLengthsEqual(arr1, arr2));
    
    String[] arr3 = {"test1", "test2"};
    String[] arr4 = {"test1", "test2", "test3"};
    assertFalse(Equality.areArrayLengthsEqual(arr3, arr4));
  }
  
  @Test
  public void testAreArrayElementsEqual() {
    int[] arr1 = {1, 2, 3};
    int[] arr2 = {1, 2, 3};
    assertTrue(Equality.areArrayElementsEqual(arr1, arr2));
    
    String[] arr3 = {"test1", "test2"};
    String[] arr4 = {"test1", "test2", "test3"};
    assertFalse(Equality.areArrayElementsEqual(arr3, arr4));
  }
  
  @Test
  public void testIsArray() {
    int[] arr1 = {1, 2, 3};
    assertTrue(Equality.isArray(arr1));
    
    String str = "test";
    assertFalse(Equality.isArray(str));
  }
}