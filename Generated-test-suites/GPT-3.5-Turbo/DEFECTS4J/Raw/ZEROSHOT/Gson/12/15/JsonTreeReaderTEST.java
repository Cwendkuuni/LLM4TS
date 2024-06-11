import org.junit.Test;
import java.io.IOException;
import java.util.*;
import com.google.gson.*;
import com.google.gson.stream.JsonToken;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {
  
  // Helper method to create a JsonElement from a string
  private JsonElement createJsonElement(String json) {
    return new JsonParser().parse(json);
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    reader.nextString();
    reader.nextString();
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "value");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "value");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNextWithElements() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertTrue(reader.hasNext());
  }

  @Test
  public void testHasNextWithNoElements() throws IOException {
    JsonArray jsonArray = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertFalse(reader.hasNext());
  }

  // Method to test the peek() method with different types of JsonElements
  @Test
  public void testPeek() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add(false);
    jsonArray.add(42);
    jsonArray.addNull();
    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.nextString();
    assertEquals(JsonToken.STRING, reader.peek());

    reader.nextBoolean();
    assertEquals(JsonToken.BOOLEAN, reader.peek());

    reader.nextInt();
    assertEquals(JsonToken.NUMBER, reader.peek());

    reader.nextNull();
    assertEquals(JsonToken.NULL, reader.peek());

    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name1", "value1");
    jsonObject.addProperty("name2", "value2");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    assertEquals("name1", reader.nextName());
  }

  @Test
  public void testNextString() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertEquals("element1", reader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(true);
    jsonArray.add(false);
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertTrue(reader.nextBoolean());
    assertFalse(reader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(JsonNull.INSTANCE);
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    reader.nextNull();
    assertNull(reader.nextString());
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(3.14);
    jsonArray.add(2.718);
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertEquals(3.14, reader.nextDouble(), 0);
    assertEquals(2.718, reader.nextDouble(), 0);
  }

  @Test
  public void testNextLong() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(42L);
    jsonArray.add(123456789L);
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertEquals(42L, reader.nextLong());
    assertEquals(123456789L, reader.nextLong());
  }

  @Test
  public void testNextInt() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(42);
    jsonArray.add(123456789);
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertEquals(42, reader.nextInt());
    assertEquals(123456789, reader.nextInt());
  }

  @Test
  public void testClose() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testSkipValueWithObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name1", "value1");
    jsonObject.addProperty("name2", "value2");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    reader.skipValue();
    assertNull(reader.nextName());
  }

  @Test
  public void testSkipValueWithArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    reader.skipValue();
    assertNull(reader.nextString());
  }

  @Test
  public void testToString() {
    JsonElement jsonElement = createJsonElement("{\"name\":\"value\"}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertEquals("JsonTreeReader", reader.toString());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "value");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    reader.nextName();
    reader.promoteNameToValue();
    assertEquals("value", reader.nextString());
  }

  @Test
  public void testGetPath() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("element1");
    jsonArray.add("element2");
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    reader.nextString();
    reader.nextString();
    assertEquals("$.1", reader.getPath());
  }
  
}