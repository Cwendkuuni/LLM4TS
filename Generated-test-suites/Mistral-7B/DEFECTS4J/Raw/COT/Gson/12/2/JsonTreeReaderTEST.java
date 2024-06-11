import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonReader;
import com.google.gson.JsonToken;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {
  private JsonElement jsonElement;
  private JsonReader jsonReader;

  @Before
  public void setUp() {
    jsonElement = JsonParser.parseString("{\"foo\": [1, 2, 3], \"bar\": {\"baz\": 4}}");
    jsonReader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testJsonTreeReaderConstructor() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonReader.beginArray();
    JsonToken token = jsonReader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, token);

    jsonReader.endArray();
    token = jsonReader.peek();
    assertEquals(JsonToken.END_ARRAY, token);

    jsonReader.endArray();
    token = jsonReader.peek();
    assertEquals(JsonToken.END_OBJECT, token);
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonReader.beginObject();
    JsonToken token = jsonReader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, token);

    jsonReader.endObject();
    token = jsonReader.peek();
    assertEquals(JsonToken.END_OBJECT, token);

    jsonReader.endObject();
    token = jsonReader.peek();
    assertEquals(JsonToken.END_ARRAY, token);
  }

  @Test
  public void testHasNext() throws IOException {
    assertTrue(jsonReader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonToken expected = JsonToken.BEGIN_OBJECT;
    JsonToken actual = jsonReader.peek();
    assertEquals(expected, actual);
  }

  @Test
  public void testNextName() throws IOException {
    String name = jsonReader.nextName();
    assertEquals("foo", name);
  }

  @Test
  public void testNextString() throws IOException {
    String string = jsonReader.nextString();
    assertEquals("1", string);
  }

  @Test
  public void testNextBoolean() throws IOException {
    boolean booleanValue = jsonReader.nextBoolean();
    assertTrue(booleanValue);
  }

  @Test
  public void testNextNull() throws IOException {
    jsonReader.nextName();
    jsonReader.nextName();
    jsonReader.nextNull();
    assertNull(jsonReader.nextString());
  }

  @Test
  public void testNextDouble() throws IOException {
    double doubleValue = jsonReader.nextDouble();
    assertEquals(4.0, doubleValue, 0.001);
  }

  @Test
  public void testNextLong() throws IOException {
    long longValue = jsonReader.nextLong();
    assertEquals(4, longValue);
  }

  @Test
  public void testNextInt() throws IOException {
    int intValue = jsonReader.nextInt();
    assertEquals(4, intValue);
  }

  @Test
  public void testClose() throws IOException {
    jsonReader.close();
    assertThrows(IllegalStateException.class, jsonReader::nextName);
  }

  @Test
  public void testSkipValue() throws IOException {
    jsonReader.nextName();
    jsonReader.skipValue();
    String nextString = jsonReader.nextString();
    assertEquals("1", nextString);
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    jsonReader.promoteNameToValue();
    JsonToken token = jsonReader.peek();
    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonReader.peekStack();
    assertEquals(JsonToken.STRING, token);
    assertEquals("foo", jsonPrimitive.getAsString());
  }

  @Test
  public void testEmptyArray() throws IOException {
    jsonElement = JsonParser.parseString("[]");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endArray();
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testEmptyObject() throws IOException {
    jsonElement = JsonParser.parseString("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testNullElement() throws IOException {
    jsonElement = JsonParser.parseString("null");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.nextNull();
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testNullReader() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(null);
    assertThrows(NullPointerException.class, jsonTreeReader::nextName);
  }

  @Test
  public void testEmptyString() throws IOException {
    jsonElement = JsonParser.parseString("\"\"");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.nextString();
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testReadFromStringReader() {
    StringReader stringReader = new StringReader("{\"foo\": [1, 2, 3], \"bar\": {\"baz\": 4}}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(stringReader);

    JsonToken nextToken = jsonTreeReader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, nextToken);

    jsonTreeReader.nextName();
    assertEquals("foo", jsonTreeReader.nextName());

    jsonTreeReader.beginArray();
    JsonToken arrayToken = jsonTreeReader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, arrayToken);

    int index = 0;
    while (jsonTreeReader.hasNext()) {
      JsonToken jsonToken = jsonTreeReader.peek();
      if (jsonToken.isNumber()) {
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonTreeReader.peekStack();
        assertEquals(index + 1, jsonPrimitive.getAsInt());
        index++;
        jsonTreeReader.nextInt();
      } else {
        jsonTreeReader.skipValue();
      }
    }

    jsonTreeReader.endArray();

    jsonTreeReader.nextName();
    assertEquals("bar", jsonTreeReader.nextName());
    jsonTreeReader.beginObject();

    jsonTreeReader.nextName();
    assertEquals("baz", jsonTreeReader.nextName());
    JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonTreeReader.peekStack();
    assertEquals(4, jsonPrimitive.getAsInt());
    jsonTreeReader.nextInt();

    jsonTreeReader.endObject();

    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());

    jsonTreeReader.close();
  }

  @Test
  public void testArrayIndexOutOfBounds() throws IOException {
    jsonElement = JsonParser.parseString("[1, 2]");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.nextInt();
    jsonTreeReader.nextInt();
    assertThrows(IndexOutOfBoundsException.class, () -> jsonTreeReader.nextInt());
  }

  @Test
  public void testNestedObjectsAndArrays() throws IOException {
    jsonElement = JsonParser.parseString("{\"foo\": {\"bar\": 1, \"baz\": [2, 3, 4]}}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    assertEquals("foo", jsonTreeReader.nextName());
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    assertEquals("bar", jsonTreeReader.nextName());
    JsonPrimitive bar = (JsonPrimitive) jsonTreeReader.peekStack();
    assertEquals(1, bar.getAsInt());
    jsonTreeReader.nextInt();

    jsonTreeReader.beginArray();
    jsonTreeReader.nextName();
    assertEquals("baz", jsonTreeReader.nextName());

    int index = 0;
    while (jsonTreeReader.hasNext()) {
      JsonToken jsonToken = jsonTreeReader.peek();
      if (jsonToken.isNumber()) {
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonTreeReader.peekStack();
        assertEquals(index + 1, jsonPrimitive.getAsInt());
        index++;
        jsonTreeReader.nextInt();
      } else {
        jsonTreeReader.skipValue();
      }
    }

    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();

    jsonTreeReader.nextNull();
    assertFalse(jsonTreeReader.hasNext());
  }
}