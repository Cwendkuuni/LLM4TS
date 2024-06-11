import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValuedEnumTest {
  
  @Test
  public void testGetValue() {
    // Typical use case
    JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
    assertEquals(100, java1_0.getValue());
    
    // Edge case: Enum with negative value
    JavaVersionEnum javaNegativeValue = new JavaVersionEnum("Java Negative Value", -1);
    assertEquals(-1, javaNegativeValue.getValue());
  }
  
  @Test
  public void testGetEnum() {
    // Typical use case
    JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
    assertEquals(100, java1_0.getValue());
    
    // Edge case: Enum not found
    JavaVersionEnum javaNotFound = JavaVersionEnum.getEnum("Java 1.5");
    assertNull(javaNotFound);
  }
  
  @Test
  public void testCompareTo() {
    // Typical use case
    JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
    JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
    assertTrue(java1_0.compareTo(java1_1) < 0);
    
    // Edge case: Comparing with null
    try {
      java1_0.compareTo(null);
    } catch (NullPointerException e) {
      // Expected NullPointerException
      assertNotNull(e);
    }
  }
  
  @Test
  public void testToString() {
    // Typical use case
    JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
    assertEquals("JavaVersion[JAVA1_0=100]", java1_0.toString());
  }

}