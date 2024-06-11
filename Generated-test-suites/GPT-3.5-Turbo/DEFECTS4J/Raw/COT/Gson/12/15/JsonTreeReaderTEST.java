import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals(0, reader.getPath().length());
    assertEquals(1, reader.nextInt());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    reader.beginArray();
    reader.nextInt();
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    assertEquals("", reader.getPath());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonObject.addProperty("age", 25);

    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    assertEquals(0, reader.getPath().length());
    assertEquals("name", reader.nextName());
    assertEquals("name", reader.getPath());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonObject.addProperty("age", 25);

    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    assertEquals("", reader.getPath());
  }

  @Test
  public void testHasNext() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    reader.beginArray();
    assertEquals(true, reader.hasNext());
    reader.nextInt();
    assertEquals(true, reader.hasNext());
    reader.nextInt();
    assertEquals(false, reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonObject());

    JsonTreeReader reader = new JsonTreeReader(jsonArray);

    reader.beginArray();
    assertEquals(JsonToken.NUMBER, reader.peek());
    reader.nextInt();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonObject.addProperty("age", 25);

    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    reader.beginObject();
    assertEquals("name", reader.nextName());
  }

  @Test
  public void testNextString() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("Hello"));

    assertEquals("Hello", reader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));

    assertEquals(true, reader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);

    reader.nextNull();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(3.14));

    assertEquals(3.14, reader.nextDouble(), 0.0001);
  }

  @Test
  public void testNextLong() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(100L));

    assertEquals(100L, reader.nextLong());
  }

  @Test
  public void testNextInt() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(100));

    assertEquals(100, reader.nextInt());
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);

    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonObject.addProperty("age", 25);

    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    reader.beginObject();
    reader.nextName();
    reader.skipValue();
    assertEquals("null", reader.nextName());
  }

  @Test
  public void testToString() {
    JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);

    assertEquals("JsonTreeReader", reader.toString());
  }

  @Test
  public void testGetPath() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonObject.addProperty("age", 25);

    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    assertEquals("$", reader.getPath());

    reader.beginObject();
    assertEquals("$.name", reader.getPath());
    assertEquals("name", reader.nextName());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonObject.addProperty("age", 25);

    JsonTreeReader reader = new JsonTreeReader(jsonObject);

    reader.beginObject();
    reader.promoteNameToValue();
    assertEquals("name", reader.nextString());
  }
}