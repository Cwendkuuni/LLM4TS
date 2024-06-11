import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {
  
  @Test
  public void testAreEqual() {
    // Test case: o1 and o2 are null
    assertTrue(Equality.areEqual(null, null));
  
    // Test case: o1 is null and o2 is not null
    assertFalse(Equality.areEqual(null, new Object()));
  
    // Test case: o1 is not null and o2 is null
    assertFalse(Equality.areEqual(new Object(), null));
  
    // Test case: o1 and o2 are equal objects
    assertTrue(Equality.areEqual(new Object(), new Object()));
  
    // Test case: o1 and o2 are different objects
    assertFalse(Equality.areEqual(new Object(), new Object()));
  }
  
  @Test
  public void testAreArraysEqual() {
    // Test case: o1 and o2 are arrays with equal lengths and elements
    assertTrue(Equality.areArraysEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
  
    // Test case: o1 and o2 are arrays with equal lengths and different elements
    assertFalse(Equality.areArraysEqual(new Object[]{1, 2, 3}, new Object[]{4, 5, 6}));
  
    // Test case: o1 and o2 are arrays with different lengths and elements
    assertFalse(Equality.areArraysEqual(new Object[]{1, 2, 3}, new Object[]{1, 2}));
  }
  
  @Test
  public void testAreArrayLengthsEqual() {
    // Test case: o1 and o2 are arrays with equal lengths
    assertTrue(Equality.areArrayLengthsEqual(new Object[]{1, 2, 3}, new Object[]{4, 5, 6}));
  
    // Test case: o1 and o2 are arrays with different lengths
    assertFalse(Equality.areArrayLengthsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2}));
  }
  
  @Test
  public void testAreArrayElementsEqual() {
    // Test case: o1 and o2 are arrays with equal elements
    assertTrue(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
  
    // Test case: o1 and o2 are arrays with different elements
    assertFalse(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{4, 5, 6}));
  }
  
  @Test
  public void testIsArray() {
    // Test case: o is an array
    assertTrue(Equality.isArray(new Object[]{1, 2, 3}));
  
    // Test case: o is not an array
    assertFalse(Equality.isArray(new Object()));
  }
  
}