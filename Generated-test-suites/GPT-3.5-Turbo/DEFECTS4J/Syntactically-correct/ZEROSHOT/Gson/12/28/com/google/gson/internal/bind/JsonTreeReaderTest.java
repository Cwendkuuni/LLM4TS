import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

  private JsonTreeReader reader;

  @Before
  public void setUp() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("hello"));
    jsonArray.add(new JsonPrimitive("world"));

    JsonElement jsonElement = new JsonObject();
    ((JsonObject) jsonElement).add("name", new JsonPrimitive("John Doe"));
    ((JsonObject) jsonElement).add("age", new JsonPrimitive(25));
    ((JsonObject) jsonElement).add("isStudent", new JsonPrimitive(false));
    ((JsonObject) jsonElement).add("grades", jsonArray);

    reader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void testEndArray() throws IOException {
    reader.beginArray();
    reader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testEndObject() throws IOException {
    reader.beginObject();
    reader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    assertTrue(reader.hasNext());
    reader.beginArray();
    assertTrue(reader.hasNext());
    reader.beginObject();
    assertTrue(reader.hasNext());
    reader.endObject();
    assertTrue(reader.hasNext());
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek());
  }

  @Test
  public void testNextString() throws IOException {
    reader.beginObject();
    reader.nextName();
    assertEquals("John Doe", reader.nextString());
    assertEquals(JsonToken.NUMBER, reader.peek());
  }

  @Test
  public void testNextBoolean() throws IOException {
    reader.beginObject();
    for (int i = 0; i < 2; i++) {
      reader.nextName();
      reader.skipValue();
    }
    assertEquals(false, reader.nextBoolean());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void testNextNull() throws IOException {
    reader.beginObject();
    for (int i = 0; i < 3; i++) {
      reader.nextName();
      reader.skipValue();
    }
    reader.nextNull();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testNextDouble() throws IOException {
    reader.beginObject();
    for (int i = 0; i < 3; i++) {
      reader.nextName();
      reader.skipValue();
    }
    assertEquals(25.0, reader.nextDouble(), 0);
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void testNextLong() throws IOException {
    reader.beginObject();
    for (int i = 0; i < 2; i++) {
      reader.nextName();
      reader.skipValue();
    }
    assertEquals(25L, reader.nextLong());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testNextInt() throws IOException {
    reader.beginObject();
    for (int i = 0; i < 2; i++) {
      reader.nextName();
      reader.skipValue();
    }
    assertEquals(25, reader.nextInt());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testClose() throws IOException {
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testSkipValue() throws IOException {
    reader.beginArray();
    reader.skipValue();
    assertEquals(JsonToken.STRING, reader.peek());
  }

  @Test
  public void testGetPath() throws IOException {
    assertEquals("$.name", reader.nextName());
    assertEquals("$.age", reader.nextName());
    assertEquals("$.isStudent", reader.nextName());
    assertEquals("$.grades[0]", reader.nextName());
    assertEquals("$.grades[1]", reader.nextName());
    reader.beginObject();
    reader.nextName();
    reader.skipValue();
    assertEquals("$.grades[2].null", reader.getPath());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    reader.beginObject();
    reader.promoteNameToValue();
    assertEquals("name", reader.nextString());
    assertEquals("John Doe", reader.nextString());
    assertEquals(JsonToken.NUMBER, reader.peek());
  }
}