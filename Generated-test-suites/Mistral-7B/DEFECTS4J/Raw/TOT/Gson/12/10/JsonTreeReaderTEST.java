import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginObject();
    assertTrue(reader.hasNext());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSingleNumberJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("42"));
    reader.beginObject();
    reader.nextInt();
    reader.endObject();
    reader.close();
    assertEquals(42, reader.nextInt());
  }

  @Test
  public void testSingleStringJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("\"hello\""));
    reader.beginObject();
    reader.nextString();
    reader.endObject();
    reader.close();
    assertEquals("hello", reader.nextString());
  }

  @Test
  public void testSingleBooleanJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse(new JsonPrimitive(true)));
    reader.beginObject();
    reader.nextBoolean();
    reader.endObject();
    reader.close();
    assertTrue(reader.nextBoolean());
  }

  @Test
  public void testSingleNullJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse(JsonNull.INSTANCE));
    reader.beginObject();
    reader.nextNull();
    reader.endObject();
    reader.close();
    assertNull(reader.nextValue());
  }

  @Test
  public void testArrayJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]")); ;
    reader.beginArray();
    for (int i = 0; i < 3; i++) {
      reader.nextInt();
    }
    reader.endArray();
    reader.close();
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndDocumentJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse(""));
    reader.beginArray();
    reader.nextInt();
    reader.endArray();
    reader.endDocument(); // should throw an exception
  }

  @Test
  public void testComplexJson() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"sleeping\"]}";
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse(json));
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("age", reader.nextName());
    reader.promoteNameToValue(); // promote "age" to a number
    assertEquals(30, reader.nextInt());
    assertEquals("hobbies", reader.nextName());
    reader.beginArray();
    reader.nextString();
    assertEquals("reading", reader.nextString());
    reader.nextString();
    assertEquals("sleeping", reader.nextString());
    reader.endArray();
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testStackOverflowJson() throws IOException {
    String json = "{\"name\": \"John\", \"name\": \"John\", \"name\": \"John\", \"name\": \"John\"}";
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse(json));
    reader.beginObject();
    reader.nextName(); // read "name" once
    reader.nextName(); // read "name" again, causing a stack overflow
  }
}