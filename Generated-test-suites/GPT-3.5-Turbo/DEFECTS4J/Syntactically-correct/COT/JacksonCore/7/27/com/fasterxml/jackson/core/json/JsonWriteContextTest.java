
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.json.JsonWriteContext;

public class JsonWriteContextTest {

  @Test
  public void testWriteFieldName() {
    JsonWriteContext context = JsonWriteContext.createRootContext();
    try{
      assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name1"));
      assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name2"));
    } catch (JsonGenerationException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testWriteValue() {
    JsonWriteContext context = JsonWriteContext.createRootContext();
    assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
  }

  @Test
  public void testCreateChildArrayContext() {
    JsonWriteContext context = JsonWriteContext.createRootContext();
    JsonWriteContext child = context.createChildArrayContext();
    assertNotNull(child);
    assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
  }

  @Test
  public void testCreateChildObjectContext() {
    JsonWriteContext context = JsonWriteContext.createRootContext();
    JsonWriteContext child = context.createChildObjectContext();
    assertNotNull(child);
    assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
  }

  @Test
  public void testGetParent() {
    JsonWriteContext parent = JsonWriteContext.createRootContext();
    JsonWriteContext child = parent.createChildArrayContext();
    assertEquals(parent, child.getParent());
  }

  @Test
  public void testGetCurrentName() {
    JsonWriteContext context = JsonWriteContext.createRootContext();
    try {
      context.writeFieldName("name");
      assertEquals("name", context.getCurrentName());
    } catch (JsonGenerationException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testGetDupDetector() {
    JsonWriteContext context = JsonWriteContext.createRootContext();
    assertNull(context.getDupDetector());
  }

  @Test
  public void testSetCurrentValue() {
    JsonWriteContext context = JsonWriteContext.createRootContext();
    String value = "test";
    context.setCurrentValue(value);
    assertEquals(value, context.getCurrentValue());
  }

}
