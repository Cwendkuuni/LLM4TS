import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_DefaultConstructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.get();
    assertNull(result);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonArray);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArray_AfterEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArray_BeforeBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("test");
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonArray);
    assertEquals(0, ((JsonArray) result).size());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_AfterEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_BeforeEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.name("test");
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObject_AfterEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObject_BeforeBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
    assertEquals(0, result.entrySet().size());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_AfterEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_BeforeEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.name("subTest");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("test", jsonObject.name("test").getAsString());
    assertEquals("subTest", jsonObject.name("subTest").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testName_AfterEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.name("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testName_BeforeBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.name("test");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray().value("test").endArray();
    JsonElement result = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) result;
    assertEquals("test", jsonArray.get(0).getAsString());
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject().nullValue().endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) result;
    assertNull(jsonObject.entrySet().iterator().next().getValue());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject().value(true).endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.entrySet().iterator().next().getValue().isJsonPrimitive()
        && jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive().isBoolean());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject().value(3.14).endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.entrySet().iterator().next().getValue().isJsonPrimitive()
        && jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive().isNumber());
    assertEquals(3.14, jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive().getAsDouble(), 0.001);
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject().value(42).endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.entrySet().iterator().next().getValue().isJsonPrimitive()
        && jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive().isNumber());
    assertEquals(42, jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive().getAsLong());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonNumber = new JsonPrimitive(3.1415926535897932384626433832795);
    jsonTreeWriter.beginObject().value(jsonNumber).endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.entrySet().iterator().next().getValue().isJsonPrimitive()
        && jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive().isNumber());
    assertEquals(jsonNumber.getAsDouble(), jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive().getAsDouble(), 0.001);
  }
}