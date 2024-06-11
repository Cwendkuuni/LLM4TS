import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_empty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testJsonTreeWriter_beginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonArray.class, result.getClass());
    assertNull(writer.peek());
  }

  @Test
  public void testJsonTreeWriter_endArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testJsonTreeWriter_beginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonObject.class, result.getClass());
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("value", jsonObject.get("name").getAsString());
  }

  @Test
  public void testJsonTreeWriter_endObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testJsonTreeWriter_name() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("value", jsonObject.get("name").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_name_emptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_value_emptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
  }
}