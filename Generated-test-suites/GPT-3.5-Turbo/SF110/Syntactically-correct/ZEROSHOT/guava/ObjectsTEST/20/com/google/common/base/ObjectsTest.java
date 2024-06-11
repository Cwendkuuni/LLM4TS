import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

  @Test
  public void testEqual() {
    // Test when both objects are null
    assertTrue(Objects.equal(null, null));
    
    // Test when one object is null and the other is not
    assertFalse(Objects.equal(null, "Hello"));
    assertFalse(Objects.equal("Hello", null));
    
    // Test when both objects are not null and are equal
    assertTrue(Objects.equal("Hello", "Hello"));
    
    // Test when both objects are not null and are not equal
    assertFalse(Objects.equal("Hello", "World"));
  }

  @Test
  public void testHashCode() {
    // Test when objects array is empty
    assertEquals(0, Objects.hashCode());
    
    // Test when objects array has one element
    assertEquals("Hello".hashCode(), Objects.hashCode("Hello"));
    
    // Test when objects array has multiple elements
    assertEquals(Arrays.hashCode(new Object[]{"Hello", "World"}), Objects.hashCode("Hello", "World"));
  }
  
  @Test
  public void testToStringHelper() {
    // Test toStringHelper(Object)
    Objects.ToStringHelper helper1 = Objects.toStringHelper("Hello");
    helper1.add("name", "John Doe");
    helper1.add("age", 25);
    assertEquals("Hello{name=John Doe, age=25}", helper1.toString());
    
    // Test toStringHelper(Class<?>)
    Objects.ToStringHelper helper2 = Objects.toStringHelper(Objects.class);
    helper2.addValue("Hello");
    helper2.addValue(10);
    assertEquals("com.google.common.base.Objects{Hello, 10}", helper2.toString());
    
    // Test toStringHelper(String)
    Objects.ToStringHelper helper3 = Objects.toStringHelper("Person");
    helper3.addValue("John Doe");
    helper3.addValue(25);
    assertEquals("Person{John Doe, 25}", helper3.toString());
  }
  
  @Test
  public void testFirstNonNull() {
    // Test when both objects are null
    assertNull(Objects.firstNonNull(null, null));
    
    // Test when one object is null and the other is not
    assertEquals("Hello", Objects.firstNonNull("Hello", null));
    assertEquals("Hello", Objects.firstNonNull(null, "Hello"));
    
    // Test when both objects are not null
    assertEquals("Hello", Objects.firstNonNull("Hello", "World"));
  }
}