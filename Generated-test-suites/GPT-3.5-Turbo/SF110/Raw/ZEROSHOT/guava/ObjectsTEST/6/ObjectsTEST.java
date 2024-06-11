import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
  
  @Test
  public void testEqual() {
    // Test when both objects are null
    assertTrue(Objects.equal(null, null));
    
    // Test when one object is null and the other is not
    assertFalse(Objects.equal(null, "test"));
    
    // Test when both objects are the same instance
    String obj1 = "test";
    assertTrue(Objects.equal(obj1, obj1));
    
    // Test when both objects are different instances but have the same value
    String obj2 = new String("test");
    assertTrue(Objects.equal(obj1, obj2));
    
    // Test when both objects are different instances and have different values
    assertFalse(Objects.equal(obj1, "hello"));
  }
  
  @Test
  public void testHashCode() {
    // Test when no objects are passed
    assertEquals(0, Objects.hashCode());
    
    // Test when a single object is passed
    assertEquals(42, Objects.hashCode(42));
    
    // Test when multiple objects are passed
    assertEquals(137, Objects.hashCode(1, 2, 3, 4));
  }
  
  @Test
  public void testToStringHelper() {
    // Test toStringHelper method with Object parameter
    Objects.ToStringHelper helper1 = Objects.toStringHelper("Test");
    assertNotNull(helper1);
    
    // Test toStringHelper method with Class parameter
    Objects.ToStringHelper helper2 = Objects.toStringHelper(String.class);
    assertNotNull(helper2);
    
    // Test toStringHelper method with String parameter
    Objects.ToStringHelper helper3 = Objects.toStringHelper("ClassName");
    assertNotNull(helper3);
    
    // Test omitNullValues method
    assertFalse(helper1.omitNullValues);
    Objects.ToStringHelper helper4 = helper1.omitNullValues();
    assertTrue(helper4.omitNullValues);
    
    // Test add method with name and value parameters
    Objects.ToStringHelper helper5 = helper1.add("Name", "Value");
    assertNotNull(helper5);
    
    // Test add method with name and boolean value parameters
    Objects.ToStringHelper helper6 = helper1.add("Name", true);
    assertNotNull(helper6);
    
    // Test add method with name and char value parameters
    Objects.ToStringHelper helper7 = helper1.add("Name", 'c');
    assertNotNull(helper7);
    
    // Test add method with name and double value parameters
    Objects.ToStringHelper helper8 = helper1.add("Name", 3.14);
    assertNotNull(helper8);
    
    // Test add method with name and float value parameters
    Objects.ToStringHelper helper9 = helper1.add("Name", 3.14f);
    assertNotNull(helper9);
    
    // Test add method with name and int value parameters
    Objects.ToStringHelper helper10 = helper1.add("Name", 42);
    assertNotNull(helper10);
    
    // Test add method with name and long value parameters
    Objects.ToStringHelper helper11 = helper1.add("Name", 123456789L);
    assertNotNull(helper11);
    
    // Test addValue method with value parameter
    Objects.ToStringHelper helper12 = helper1.addValue("Value");
    assertNotNull(helper12);
    
    // Test addValue method with boolean value parameter
    Objects.ToStringHelper helper13 = helper1.addValue(true);
    assertNotNull(helper13);
    
    // Test addValue method with char value parameter
    Objects.ToStringHelper helper14 = helper1.addValue('c');
    assertNotNull(helper14);
    
    // Test addValue method with double value parameter
    Objects.ToStringHelper helper15 = helper1.addValue(3.14);
    assertNotNull(helper15);
    
    // Test addValue method with float value parameter
    Objects.ToStringHelper helper16 = helper1.addValue(3.14f);
    assertNotNull(helper16);
    
    // Test addValue method with int value parameter
    Objects.ToStringHelper helper17 = helper1.addValue(42);
    assertNotNull(helper17);
    
    // Test addValue method with long value parameter
    Objects.ToStringHelper helper18 = helper1.addValue(123456789L);
    assertNotNull(helper18);
    
    // Test toString method
    assertEquals("Test{Value}", helper5.toString());
  }
  
  @Test
  public void testFirstNonNull() {
    // Test when both objects are null
    assertNull(Objects.firstNonNull(null, null));
    
    // Test when the first object is null and the second is not
    String obj1 = "test";
    assertEquals(obj1, Objects.firstNonNull(null, obj1));
    
    // Test when the first object is not null
    String obj2 = "hello";
    assertEquals(obj2, Objects.firstNonNull(obj2, obj1));
  }
  
}