import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ObjectResultTest {

  @Test
  public void testObjectResultConstructor() {
    ObjectResult objectResult = new ObjectResult("schema", "name", "type");
    
    assertNotNull(objectResult);
    assertEquals("schema", objectResult.getSchema());
    assertEquals("name", objectResult.getName());
    assertEquals("type", objectResult.getType());
    assertNotNull(objectResult.getCount());
    assertNotNull(objectResult.getTotal());
  }

  @Test
  public void testObjectResultOf() {
    ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(5), new AtomicLong(10), "type");
    
    ObjectResult objectResult = ObjectResult.of(objectStatus);
    
    assertNotNull(objectResult);
    assertEquals("name", objectResult.getName());
    assertEquals(5, objectResult.getCount().get());
    assertEquals(10, objectResult.getTotal().get());
    assertEquals("type", objectResult.getType());
  }

  @Test
  public void testGetSummary() {
    ObjectResult objectResult = new ObjectResult("schema", "name", "type");
    
    String summary = objectResult.getSummary();
    
    assertEquals("schema.name[type]", summary);
  }
}