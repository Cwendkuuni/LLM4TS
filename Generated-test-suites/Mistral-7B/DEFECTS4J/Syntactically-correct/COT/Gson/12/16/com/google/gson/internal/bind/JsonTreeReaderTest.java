import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonString;
import com.google.gson.JsonToken;
import com.google.gson.internal.bind.JsonTreeReader;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReader_emptyJsonObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testJsonTreeReader_emptyJsonArray() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testJsonTreeReader_simpleJsonObject() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
    JsonElement jsonElement = new JsonObject(new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("John", reader.nextString());

    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("age", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(30, reader.nextInt());

    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("city", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("New York", reader.nextString());

    reader.endObject();
    assertNull(reader.nextName());
    reader.close();
  }

  @Test
  public void testJsonTreeReader_simpleJsonArray() throws IOException {
    String jsonString = "[1, \"two\", 3.0, null, [4, 5]]";
    JsonElement jsonElement = new JsonObject();
    jsonElement.add("items", new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("items", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("two", reader.nextString());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(3.0, reader.nextDouble(), 0.001);

    assertEquals(JsonToken.NULL, reader.peek());
    reader.nextNull();

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(4, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(5, reader.nextInt());

    reader.endArray();
    reader.endArray();
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReader_closed() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.close();
    reader.nextName();
  }

  @Test
  public void testJsonTreeReader_skipValue() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":null}";
    JsonElement jsonElement = new JsonObject(new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.nextName();
    reader.skipValue();
    reader.nextName();
    reader.skipValue();

    assertEquals(JsonToken.NULL, reader.peek());
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testJsonTreeReader_promoteNameToValue() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":null}";
    JsonElement jsonElement = new JsonObject(new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.promoteNameToValue();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("John", reader.nextString());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(30, reader.nextInt());

    reader.nextName();
    reader.promoteNameToValue();
    assertEquals(JsonToken.NULL, reader.peek());
    reader.nextNull();

    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonTreeReader_invalidJsonString() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":null}";
    String invalidJsonString = jsonString.substring(0, jsonString.length() - 1); // remove the last '}'
    JsonElement jsonElement = new JsonObject(new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.push(jsonElement);

    reader.beginArray();
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // read the valid JSON string

    reader.push(new JsonObject());
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // read the invalid JSON string

    try {
      reader.nextName();
    } catch (IllegalStateException e) {
      assertTrue(e.getMessage().contains("Expected START_OBJECT but was NULL"));
    }

    reader.close();
  }

  @Test
  public void testJsonTreeReader_emptyIterator() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.push(Collections.emptyIterator());
    reader.push(Collections.emptyList().iterator());
    reader.beginArray();

    assertFalse(reader.hasNext());
    reader.endArray();
    reader.popStack();
    reader.popStack();
    reader.close();
  }

  @Test
  public void testJsonTreeReader_iteratorWithOneElement() throws IOException {
    String jsonString = "[1]";
    JsonElement jsonElement = new JsonObject();
    jsonElement.add("items", new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.push(new JsonObject());
    reader.push(jsonElement);
    reader.beginObject();

    reader.push(jsonElement.get("items").getAsJsonArray().iterator());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    reader.endArray();
    reader.endObject();
    reader.popStack();
    reader.popStack();
    reader.close();
  }

  @Test
  public void testJsonTreeReader_iteratorWithMultipleElements() throws IOException {
    String jsonString = "[1, 2, 3]";
    JsonElement jsonElement = new JsonObject();
    jsonElement.add("items", new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.push(new JsonObject());
    reader.push(jsonElement);
    reader.beginObject();

    reader.push(jsonElement.get("items").getAsJsonArray().iterator());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(2, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(3, reader.nextInt());

    reader.endArray();
    reader.endObject();
    reader.popStack();
    reader.popStack();
    reader.close();
  }

  @Test
  public void testJsonTreeReader_multipleObjectsAndArrays() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"items\":[1, 2, 3]}";
    JsonElement jsonElement = new JsonObject(new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("John", reader.nextString());

    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("age", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(30, reader.nextInt());

    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("items", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(2, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(3, reader.nextInt());

    reader.endArray();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonTreeReader_invalidJsonArray() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"items\":[1, 2, 3, null]}";
    JsonElement jsonElement = new JsonObject(new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.push(new JsonObject());
    reader.push(jsonElement);
    reader.beginObject();

    reader.push(jsonElement.get("items").getAsJsonArray().iterator());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(2, reader.nextInt());

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(3, reader.nextInt());

    reader.nextNull(); // read the null value

    try {
      reader.nextName();
    } catch (IllegalStateException e) {
      assertTrue(e.getMessage().contains("Expected END_ARRAY but was NULL"));
    }

    reader.close();
  }

  @Test
  public void testJsonTreeReader_recursiveJsonArray() throws IOException {
    String jsonString = "[1, [2, 3], [4, [5, 6], 7]]";
    JsonElement jsonElement = new JsonObject();
    jsonElement.add("items", new JsonParser().parse(new ByteArrayInputStream(jsonString.getBytes())));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.push(jsonElement);
    reader.beginObject();

    reader.push(jsonElement.get("items").getAsJsonArray().iterator());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    reader.beginArray();
    reader.push(jsonElement.get("items").getAsJsonArray().get(1).getAsJsonArray().iterator());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(2, reader.nextInt());

    reader.beginArray();
    reader.push(jsonElement.get("items").getAsJsonArray().get(1).getAsJsonArray().get(1).getAsJsonArray().iterator());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(5, reader.nextInt());

    reader.endArray();
    reader.endArray();
    reader.nextInt(); // read the number 6

    reader.endArray();
    reader.endArray();
    reader.endObject();
    reader.close();
  }
}