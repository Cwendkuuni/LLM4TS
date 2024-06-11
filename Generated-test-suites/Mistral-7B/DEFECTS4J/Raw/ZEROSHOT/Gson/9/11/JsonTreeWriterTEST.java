import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
    JsonElement result = writer.get();
    JsonArray jsonArray = (JsonArray) result;
    assertEquals(0, jsonArray.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = (JsonArray) result;
    assertEquals(0, jsonArray.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(0, jsonObject.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginObject();
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(0, jsonObject.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(1, jsonObject.size());
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertEquals("value", jsonPrimitive.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithoutObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("key");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertEquals("value", jsonPrimitive.getAsString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value(true);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertTrue(jsonPrimitive.isBoolean());
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value(123);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertEquals(123, jsonPrimitive.getAsLong());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.nullValue();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonNull jsonNull = jsonObject.get("key");
    assertNull(jsonNull.isJsonNull());
  }

  @Test
  public void testValueArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = (JsonArray) result;
    assertEquals(3, jsonArray.size());
    JsonPrimitive jsonPrimitive1 = jsonArray.get(0).getAsJsonPrimitive();
    assertEquals(1, jsonPrimitive1.getAsInt());
    JsonPrimitive jsonPrimitive2 = jsonArray.get(1).getAsJsonPrimitive();
    assertEquals(2, jsonPrimitive2.getAsInt());
    JsonPrimitive jsonPrimitive3 = jsonArray.get(2).getAsJsonPrimitive();
    assertEquals(3, jsonPrimitive3.getAsInt());
  }

  @Test
  public void testValueObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 2);
    writer.beginObject();
    writer.name("key3");
    writer.value(jsonObject);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObjectResult = (JsonObject) result;
    JsonPrimitive jsonPrimitive = jsonObjectResult.get("key3");
    assertSame(jsonObject, jsonPrimitive.getAsJsonObject());
  }
}