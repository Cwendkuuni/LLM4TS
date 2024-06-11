import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {
  @Test
  public void testJsonTreeReaderWithJsonObject() throws Exception {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());

    // Test endObject()
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test peek()
    jsonTreeReader.push(new JsonPrimitive("name"));
    jsonTreeReader.push(new JsonObject());
    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testJsonTreeReaderWithJsonArray() throws Exception {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginArray()
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());

    // Test endArray()
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test peek()
    jsonTreeReader.push(new JsonPrimitive(1));
    jsonTreeReader.push(new JsonPrimitive(2));
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek(1));
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderWithClosedReader() throws Exception {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(null);
    jsonTreeReader.close();
    jsonTreeReader.nextString();
  }

  @Test
  public void testJsonTreeReaderWithEmptyJsonObject() throws Exception {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());

    // Test endObject()
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test peek()
    jsonTreeReader.push(new JsonPrimitive(""));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    assertNull(jsonTreeReader.nextName());
  }

  @Test
  public void testJsonTreeReaderWithEmptyJsonArray() throws Exception {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginArray()
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());

    // Test endArray()
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test peek()
    jsonTreeReader.push(new JsonPrimitive(null));
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
  }

  @Test
  public void testJsonTreeReaderWithJsonObjectWithStringKey() throws Exception {
    JsonElement jsonElement = new JsonObject();
    jsonElement.addProperty("key", "value");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());

    // Test endObject()
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test nextName()
    assertEquals("key", jsonTreeReader.nextName());
    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    assertEquals("value", jsonTreeReader.nextString());
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testJsonTreeReaderWithJsonObjectWithNumberKey() throws Exception {
    JsonElement jsonElement = new JsonObject();
    jsonElement.addProperty(1, "value");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());

    // Test endObject()
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test nextName()
    jsonTreeReader.push(new JsonPrimitive(1));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
    assertEquals(1, jsonTreeReader.nextInt());
    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    assertEquals("value", jsonTreeReader.nextString());
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testJsonTreeReaderWithJsonObjectWithBooleanKey() throws Exception {
    JsonElement jsonElement = new JsonObject();
    jsonElement.addProperty("key", true);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());

    // Test endObject()
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test nextName()
    assertEquals("key", jsonTreeReader.nextName());
    assertEquals(JsonToken.BOOLEAN, jsonTreeReader.peek());
    assertTrue(jsonTreeReader.nextBoolean());
  }

  @Test
  public void testJsonTreeReaderWithJsonObjectWithNullKey() throws Exception {
    JsonElement jsonElement = new JsonObject();
    jsonElement.add(null, new JsonPrimitive("value"));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());

    // Test endObject()
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test nextName()
    jsonTreeReader.push(new JsonPrimitive(null));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    assertNull(jsonTreeReader.nextName());
    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    assertEquals("value", jsonTreeReader.nextString());
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testJsonTreeReaderWithJsonObjectWithArrayKey() throws Exception {
    JsonElement jsonElement = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    jsonElement.add("key", jsonArray);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());

    // Test endObject()
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());

    // Test hasNext()
    assertFalse(jsonTreeReader.hasNext());

    // Test nextName()
    jsonTreeReader.push(new JsonPrimitive("key"));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    assertEquals("key", jsonTreeReader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    assertNull(jsonTreeReader.nextName());
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }
}