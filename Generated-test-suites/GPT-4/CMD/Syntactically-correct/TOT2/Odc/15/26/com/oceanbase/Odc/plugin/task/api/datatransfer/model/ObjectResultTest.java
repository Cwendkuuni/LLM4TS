package com.oceanbase.odc.plugin.task.api.datatransfer.model;
import static org.junit.Assert.*;
import org.junit.Test;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
  
  @Test
  public void testConstructor() {
     ObjectResult result = new ObjectResult("schema", "name", "type");
     assertNotNull(result);
     assertEquals("schema", result.getSchema());
     assertEquals("name", result.getName());
     assertEquals("type", result.getType());
  }

  @Test
  public void testOf() {
     ObjectStatus status = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
     status.setSchema("schema");
     status.setType("type");
     ObjectResult result = ObjectResult.of(status);
     assertNotNull(result);
     assertEquals("schema", result.getSchema());
     assertEquals("name", result.getName());
     assertEquals("type", result.getType());
  }

  @Test
  public void testGetSummary() {
     ObjectResult result = new ObjectResult("schema", "name", "type");
     String summary = result.getSummary();
     assertEquals("schema.name[type]", summary);
  }
}