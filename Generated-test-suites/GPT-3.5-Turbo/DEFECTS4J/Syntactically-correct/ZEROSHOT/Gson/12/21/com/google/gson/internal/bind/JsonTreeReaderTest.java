import static org.junit.Assert.*;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() throws IOException {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonElement element = new JsonPrimitive(10);
    JsonTreeReader reader = new JsonTreeReader(element);
    assertEquals(JsonToken.NUMBER, reader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("name", "John");
    JsonElement element = object;
    JsonTreeReader reader = new JsonTreeReader(element);
    assertEquals("name", reader.nextName());
  }

  @Test
  public void testNextString() throws IOException {
    JsonElement element = new JsonPrimitive("Hello");
    JsonTreeReader reader = new JsonTreeReader(element);
    assertEquals("Hello", reader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonElement element = new JsonPrimitive(true);
    JsonTreeReader reader = new JsonTreeReader(element);
    assertTrue(reader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    JsonElement element = JsonNull.INSTANCE;
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.nextNull();
    assertEquals(JsonToken.NULL, reader.peek());
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonElement element = new JsonPrimitive(3.14);
    JsonTreeReader reader = new JsonTreeReader(element);
    assertEquals(3.14, reader.nextDouble(), 0.001);
  }

  @Test
  public void testNextLong() throws IOException {
    JsonElement element = new JsonPrimitive(100L);
    JsonTreeReader reader = new JsonTreeReader(element);
    assertEquals(100L, reader.nextLong());
  }

  @Test
  public void testNextInt() throws IOException {
    JsonElement element = new JsonPrimitive(10);
    JsonTreeReader reader = new JsonTreeReader(element);
    assertEquals(10, reader.nextInt());
  }

  @Test
  public void testClose() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.skipValue();
    assertEquals("null", reader.getPath());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("name", "John");
    JsonElement element = object;
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.promoteNameToValue();
    assertEquals(JsonToken.STRING, reader.peek());
  }

  @Test
  public void testGetPath() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("name", "John");
    JsonElement element = object;
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.promoteNameToValue();
    assertEquals("$.name", reader.getPath());
  }

}