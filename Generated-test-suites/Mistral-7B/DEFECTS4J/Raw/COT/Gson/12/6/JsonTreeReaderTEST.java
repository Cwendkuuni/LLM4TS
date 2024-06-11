import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonToken;
import org.junit.Test;

public class JsonTreeReaderTest {

  

  @Test
  public void testJsonTreeReaderConstruction() {
    JsonElement jsonElement = createJsonElement("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertNotNull(reader);
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeReaderEmptyConstructor() {
    new JsonTreeReader();
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonElement jsonElement = createJsonElement("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    JsonToken token = reader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, token);
    reader.endArray();

    reader.beginArray();
    reader.endArray();
    // Test for empty array
    token = reader.peek();
    assertEquals(JsonToken.END_ARRAY, token);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndArray() throws IOException {
    JsonElement jsonElement = createJsonElement("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    reader.endArray();
    reader.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndObject() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.endObject();
    reader.beginObject();
  }

  @Test
  public void testEndArray() throws IOException {
    JsonElement jsonElement = createJsonElement("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    reader.endArray();

    reader.beginArray();
    reader.endArray();
    reader.beginArray();
    reader.endArray();

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_ARRAY, token);
  }

  @Test
  public void testEndObject() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.endObject();

    reader.beginObject();
    reader.endObject();
    reader.beginObject();
    reader.endObject();

    token = reader.peek();
    assertEquals(JsonToken.END_OBJECT, token);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterEndObject() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.endObject();
    reader.endObject();
  }

  @Test
  public void testHasNext() throws IOException {
    JsonElement jsonElement = createJsonElement("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();

    assertTrue(reader.hasNext());
    reader.nextInt();

    assertTrue(reader.hasNext());
    reader.nextInt();

    assertTrue(reader.hasNext());
    reader.nextInt();

    assertFalse(reader.hasNext());
    reader.endArray();
  }

  @Test
  public void testPeek() throws IOException {
    JsonElement jsonElement = createJsonElement("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.nextInt();
    assertEquals(JsonToken.NUMBER, reader.peek());

    reader.nextInt();
    assertEquals(JsonToken.NUMBER, reader.peek());

    reader.nextInt();
    assertEquals(JsonToken.NUMBER, reader.peek());

    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekAfterEndDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(mock(Reader.class));
    reader.close();
    reader.peek();
  }

  @Test
  public void testBeginObject_EmptyObject() throws IOException {
    JsonElement jsonElement = createJsonElement("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.endObject();

    reader.beginObject();
    reader.endObject();

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_OBJECT, token);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndArray() throws IOException {
    JsonElement jsonElement = createJsonElement("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    reader.endArray();
    reader.beginObject();
  }

  @Test
  public void testEndObject_EmptyObject() throws IOException {
    JsonElement jsonElement = createJsonElement("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.endObject();

    reader.beginObject();
    reader.endObject();

    JsonToken token = reader.peek();
    assertEquals(JsonToken.END_OBJECT, token);
  }

  @Test
  public void testEndObject_NonEmptyObject() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextString();
    reader.nextInt();
    reader.endObject();

    reader.beginObject();
    reader.endObject();

    token = reader.peek();
    assertEquals(JsonToken.END_OBJECT, token);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterEndObject() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextString();
    reader.nextInt();
    reader.endObject();
    reader.endObject();
  }

  @Test
  public void testNextName() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();

    assertEquals("name", reader.nextName());
    assertEquals("age", reader.nextName());
    assertNull(reader.nextName());

    reader.beginObject();
    reader.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNameAfterEndObject() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextString();
    reader.nextInt();
    reader.endObject();
    reader.nextName();
  }

  @Test
  public void testNextString() throws IOException {
    JsonElement jsonElement = createJsonElement("[\"hello\", \"world\"]");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();

    assertEquals("hello", reader.nextString());
    assertEquals("world", reader.nextString());
    assertNull(reader.nextString());

    reader.beginArray();
    reader.endArray();
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonElement jsonElement = createJsonElement("{ \"is_employee\": true, \"is_student\": false }");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();

    assertTrue(reader.nextBoolean());
    assertFalse(reader.nextBoolean());
    assertFalse(reader.nextBoolean());

    reader.beginObject();
    reader.endObject();
  }

  @Test
  public void testNextNull() throws IOException {
    JsonElement jsonElement = createJsonElement("{ \"name\": null, \"age\": 30 }");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();

    assertNull(reader.nextNull());
    reader.nextString();
    reader.nextInt();
    reader.endObject();

    reader.beginObject();
    reader.endObject();
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonElement jsonElement = createJsonElement("{ \"price\": 12.5 }");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();

    assertEquals(12.5, reader.nextDouble(), 0.0);

    reader.beginObject();
    reader.endObject();
  }

  @Test
  public void testNextLong() throws IOException {
    JsonElement jsonElement = createJsonElement("{ \"id\": 10 }");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();

    assertEquals(10, reader.nextLong());

    reader.beginObject();
    reader.endObject();
  }

  @Test
  public void testNextInt() throws IOException {
    JsonElement jsonElement = createJsonElement("{ \"age\": 30 }");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();

    assertEquals(30, reader.nextInt());

    reader.beginObject();
    reader.endObject();
  }

  @Test
  public void testClose() throws IOException {
    JsonElement jsonElement = createJsonElement("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.close();

    reader.beginObject();
    reader.endObject();

    try {
      reader.nextInt();
    } catch (IOException e) {
      assertEquals("JsonReader is closed", e.getMessage());
    }
  }

  @Test(expected = AssertionError.class)
  public void testCloseTwice() throws IOException {
    JsonElement jsonElement = createJsonElement("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.close();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.skipValue();

    assertEquals("null", reader.nextName());
    reader.nextInt();
    reader.endObject();

    reader.beginObject();
    reader.skipValue();
    reader.endObject();
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonElement jsonElement = createJsonElement("{\"name\": \"John\"}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.promoteNameToValue();

    assertEquals("John", reader.nextString());
    reader.nextString();
    reader.endObject();

    reader.beginObject();
    reader.promoteNameToValue();
    reader.endObject();
  }

  @Test(expected = AssertionError.class)
  public void testReadFirstTokenBeforeConstruction() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(mock(Reader.class));
    reader.nextToken();
  }

  @Test(expected = AssertionError.class)
  public void testReadFirstTokenAfterConstruction() throws IOException {
    JsonElement jsonElement = createJsonElement("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.nextToken();
  }

  @Test
  public void testReadFirstTokenAfterPeek() throws IOException {
    JsonElement jsonElement = createJsonElement("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.peek();
    reader.nextToken();

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testToString() {
    JsonTreeReader reader = new JsonTreeReader(mock(JsonElement.class));
    assertEquals("JsonTreeReader", reader.toString());
  }

  
}