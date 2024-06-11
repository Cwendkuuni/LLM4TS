import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import org.junit.Test;

public class JsonTreeReaderTest {
  @Test
  public void testJsonTreeReader_EmptyJsonObject() {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();
    assertEquals(null, reader.nextName());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();
    assertFalse(reader.hasNext());

    reader.close();
  }

  @Test
  public void testJsonTreeReader_EmptyJsonArray() {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();
    assertFalse(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();
    assertFalse(reader.hasNext());

    reader.close();
  }

  @Test
  public void testJsonTreeReader_SimpleJsonObject() {
    String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("John", reader.nextString());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());

    assertEquals("age", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(30, reader.nextInt());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());

    assertEquals("city", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("New York", reader.nextString());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    assertFalse(reader.hasNext());

    reader.close();
  }

  @Test
  public void testJsonTreeReader_NestedJsonObjects() {
    String jsonString = "{\"name\": { \"first\": \"John\", \"last\": \"Doe\" }, \"age\": 30}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals("first", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("John", reader.nextString());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());

    assertEquals("last", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("Doe", reader.nextString());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    assertEquals("age", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(30, reader.nextInt());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    assertFalse(reader.hasNext());

    reader.close();
  }

  @Test
  public void testJsonTreeReader_NestedJsonArrays() {
    String jsonString = "{\"name\": [{\"first\": \"John\"}, {\"last\": \"Doe\"}], \"age\": 30}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals("first", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("John", reader.nextString());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertEquals("last", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("Doe", reader.nextString());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    assertEquals("age", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(30, reader.nextInt());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    assertFalse(reader.hasNext());

    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReader_ClosedReader() {
    String jsonString = "{}";
    JsonElement jsonElement = new JsonParser().parse(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.close();

    reader.next(); // should fail with IllegalStateException
  }
}