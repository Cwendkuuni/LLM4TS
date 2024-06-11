import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectResultTest {
  
  @Test
  public void testObjectResultConstructor() {
    ObjectResult objectResult = new ObjectResult("schema", "name", "type");
    assertNotNull(objectResult);
    assertEquals("schema", objectResult.getSchema());
    assertEquals("name", objectResult.getName());
    assertEquals("type", objectResult.getType());
    assertEquals(0, objectResult.getCount().get());
    assertEquals(0, objectResult.getTotal().get());
  }
  
  @Test
  public void testStaticOfMethod() {
    ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(2), "type");
    ObjectResult objectResult = ObjectResult.of(objectStatus);
    assertNotNull(objectResult);
    assertEquals("name", objectResult.getName());
    assertEquals(1, objectResult.getCount().get());
    assertEquals(2, objectResult.getTotal().get());
    assertEquals("type", objectResult.getType());
  }
  
  @Test
  public void testGetSummary() {
    ObjectResult objectResult = new ObjectResult("schema", "name", "type");
    String summary = objectResult.getSummary();
    assertEquals("schema.name[type]", summary);
  }
  
  // Additional test cases for edge cases and exception handling can be added here
  
}