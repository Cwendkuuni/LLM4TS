import static org.junit.Assert.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement element = writer.get();
    assertTrue(element.isJsonArray());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.get();
    assertFalse(element.isJsonArray());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement element = writer.get();
    assertTrue(element.isJsonObject());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement element = writer.get();
    assertFalse(element.isJsonObject());
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    JsonElement element = writer.get();
    assertTrue(element.isJsonObject());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    JsonElement element = writer.get();
    assertTrue(element.isJsonPrimitive());
    assertEquals(JsonPrimitive.class, element.getClass());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement element = writer.get();
    assertTrue(element.isJsonPrimitive());
    assertEquals(JsonPrimitive.class, element.getClass());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10.5);
    JsonElement element = writer.get();
    assertTrue(element.isJsonPrimitive());
    assertEquals(JsonPrimitive.class, element.getClass());
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10L);
    JsonElement element = writer.get();
    assertTrue(element.isJsonPrimitive());
    assertEquals(JsonPrimitive.class, element.getClass());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10);
    JsonElement element = writer.get();
    assertTrue(element.isJsonPrimitive());
    assertEquals(JsonPrimitive.class, element.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testIncompleteDocument() throws Exception {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.flush();
    writer.close();
    writer.get();
  }

}