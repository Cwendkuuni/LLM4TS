import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertNull(result);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertEquals(JsonArray.class, result.getClass());
    assertEquals(0, result.getAsJsonArray().size());
  }

  @Test
  public void testBeginArrayAddValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("hello");
    writer.endArray();
    JsonElement result = writer.get();
    assertEquals(JsonArray.class, result.getClass());
    JsonArray jsonArray = result.getAsJsonArray();
    assertEquals(1, jsonArray.size());
    assertEquals("hello", jsonArray.get(0).getAsString());
  }

  @Test
  public void testBeginArrayAddNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.nullValue();
    writer.endArray();
    JsonElement result = writer.get();
    assertEquals(JsonArray.class, result.getClass());
    JsonArray jsonArray = result.getAsJsonArray();
    assertEquals(1, jsonArray.size());
    assertNull(jsonArray.get(0));
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals("name", jsonObject.keySet().iterator().next().getAsString());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals("age", jsonObject.keySet().iterator().next().getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testBeginObjectAddNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value(null);
    writer.name("age").value(30);
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals("name", jsonObject.keySet().iterator().next().getAsString());
    assertNull(jsonObject.get("name"));
    assertEquals("age", jsonObject.keySet().iterator().next().getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testLenientMode() {
    JsonTreeWriter writer = new JsonTreeWriter(true);
    writer.beginObject();
    writer.name("name").value(Double.NaN);
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals("name", jsonObject.keySet().iterator().next().getAsString());
    assertSame(JsonPrimitive.class, jsonObject.get("name").getClass());
    assertEquals(Double.NaN, jsonObject.get("name").getAsDouble());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBeginArrayAfterBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();    writer.beginObject();
    writer.endObject();
    writer.value("hello");
  }
}