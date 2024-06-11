import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    // Basic functionality test
    assertFalse(reader.hasNext());
    reader.close();

    // Edge cases and exception handling tests
    try {
      reader.peek();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    reader = new JsonTreeReader(JsonElement.parse("[]"));
    try {
      reader.nextString();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testSimpleJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));

    // Basic functionality tests
    assertFalse(reader.hasNext());
    reader.close();

    // Edge cases and exception handling tests
    try {
      reader.nextName();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    reader = new JsonTreeReader(JsonElement.parse("[]"));
    // Basic functionality tests
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().token());
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek().token());
    reader.endArray();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30}"));

    // Basic functionality tests
    assertFalse(reader.hasNext());
    reader.close();

    // Edge cases and exception handling tests
    try {
      reader.nextInt();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    reader = new JsonTreeReader(JsonElement.parse("{}"));
    // Basic functionality tests
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().token());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().token());
    reader.endObject();
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testComplexJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
            "\"name\": { \"first\": \"John\", \"last\": \"Doe\" }," +
            "\"age\": 30," +
            "\"hobbies\": [" +
            "  { \"name\": \"football\" }," +
            "  { \"name\": \"reading\", \"books\": [" +
            "    { \"title\": \"War and Peace\" }," +
            "    { \"title\": \"Anna Karenina\" }" +
            "  ]}" +
            "]" +
            "}"));

    // Basic functionality tests
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().token());
    reader.beginObject();

    // Testing nested objects
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("name", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().token());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("first", reader.nextName());
    assertEquals("John", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("last", reader.nextName());
    assertEquals("Doe", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().token());
    reader.endObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());

    // Testing nested arrays
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("hobbies", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().token());
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().token());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("name", reader.nextName());
    assertEquals("football", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().token());
    reader.endObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().token());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("name", reader.nextName());
    assertEquals("reading", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().token());
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().token());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("title", reader.nextName());
    assertEquals("War and Peace", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().token());
    reader.endObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().token());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().token());
    assertEquals("title", reader.nextName());
    assertEquals("Anna Karenina", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek().token());
    reader.endArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().token());
    reader.endObject();
    assertFalse(reader.hasNext());
    reader.close();
  }
}