
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
  
  @Test
  public void testEqual() {
    // Test when a and b are equal
    assertTrue(Objects.equal("Hello", "Hello"));
    
    // Test when a and b are not equal
    assertFalse(Objects.equal("Hello", "World"));
    
    // Test when a is null and b is not null
    assertFalse(Objects.equal(null, "Hello"));
    
    // Test when a is not null and b is null
    assertFalse(Objects.equal("Hello", null));
    
    // Test when both a and b are null
    assertTrue(Objects.equal(null, null));
  }
  
  @Test
  public void testHashCode() {
    // Test hashing a single object
    assertEquals(Objects.hashCode("Hello"), "Hello".hashCode());
    
    // Test hashing multiple objects
    assertEquals(Objects.hashCode("Hello", "World"), Arrays.hashCode(new Object[]{"Hello", "World"}));
    
    // Test hashing null objects
    assertEquals(Objects.hashCode(null), 0);
    
    // Test hashing empty array of objects
    assertEquals(Objects.hashCode(), 1);
  }
  
  @Test
  public void testToStringHelper() {
    Objects.ToStringHelper helper = Objects.toStringHelper("TestObject");
    
    // Test adding values and omitting null values
    helper.omitNullValues();
    helper.add("name", "John");
    helper.add("age", 25);
    assertEquals(helper.toString(), "TestObject{name=John, age=25}");
    
    // Test adding values without omitting null values
    helper.omitNullValues();
    helper.add("name", null);
    helper.add("age", null);
    assertEquals(helper.toString(), "TestObject{name=null, age=null}");
  }
  
  @Test
  public void testFirstNonNull() {
    // Test when both first and second are not null
    String result = Objects.firstNonNull("Hello", "World");
    assertEquals(result, "Hello");
    
    // Test when first is null and second is not null
    result = Objects.firstNonNull(null, "World");
    assertEquals(result, "World");
    
    // Test when first is not null and second is null
    result = Objects.firstNonNull("Hello", null);
    assertEquals(result, "Hello");
    
    // Test when both first and second are null
    result = Objects.firstNonNull(null, null);
    assertNull(result);
  }
  
}
