import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    String value = jsonObject.get("key").getAsString();
    assertEquals("value", value);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(2);
    JsonArray jsonArray = jsonTreeWriter.get().getAsJsonArray();
    assertEquals(2, jsonArray.size());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(2, jsonArray.get(1).getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    JsonObject jsonObject = jsonTreeWriter.get().getAsJsonObject();
    String key = jsonObject.keySet().iterator().next();
    assertEquals("key", key);
    String value = jsonObject.get(key).getAsString();
    assertEquals("value", value);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value("value");
    jsonTreeWriter.name("key");
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(null);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertNull(jsonElement);
  }

  @Test
  public void testBooleanValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isBoolean());
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testDoubleValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1.23);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(1.23, jsonPrimitive.getAsDouble(), 0.0001);
  }

  @Test
  public void testIntegerValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(1, jsonPrimitive.getAsInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithNaN() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInfinity() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseWithoutBegin() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
  }

  @Test
  public void testClose() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(0, jsonObject.size());
  }

  @Test
  public void testCloseTwice() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(0, jsonObject.size());
  }

  @Test
  public void testCloseAfterValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(1);
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(1, jsonObject.size());
    JsonPrimitive jsonPrimitive = jsonObject.get(JsonTreeWriter.SENTINEL_CLOSED).getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isString());
    assertEquals("closed", jsonPrimitive.getAsString());
  }

  @Test
  public void testCloseAfterArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    assertEquals(0, jsonArray.size());
    JsonPrimitive jsonPrimitive = jsonArray.get(0).getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isString());
    assertEquals("closed", jsonPrimitive.getAsString());
  }

  @Test
  public void testCloseAfterObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(1, jsonObject.size());
    JsonPrimitive jsonPrimitive = jsonObject.get("key").getAsJsonPrimitive();
    assertEquals("value", jsonPrimitive.getAsString());
    JsonPrimitive jsonPrimitive1 = jsonObject.get(JsonTreeWriter.SENTINEL_CLOSED).getAsJsonPrimitive();
    assertTrue(jsonPrimitive1.isString());
    assertEquals("closed", jsonPrimitive1.getAsString());
  }
}