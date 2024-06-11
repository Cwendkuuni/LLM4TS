import static org.junit.Assert.*;
import static com.google.gson.stream.JsonTokens.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testNullJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30}"));
    assertEquals(BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals(String.class, reader.peekValue().getClass());
    reader.endObject();
    reader.beginObject();
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[ ]"));
    assertEquals(BEGIN_ARRAY, reader.peek());
    reader.beginArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testSimpleArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    assertEquals(BEGIN_ARRAY, reader.peek());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.beginArray();
    assertEquals(2, reader.nextInt());
    reader.endArray();
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testMalformedJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{name: John}")); // missing quotes
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testMalformedJson2() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{name: John, age:")); // missing colon
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testMalformedJson3() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{name: John, age: 30,}")); // extra comma
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testMalformedJson4() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{name: John, age: 30, name: 'Mike'}")); // duplicate name
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testMalformedJson5() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3, 4]")); // extra element
    reader.close();
  }
}