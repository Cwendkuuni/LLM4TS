import static org.junit.Assert.*;
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
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30}"));
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.nextName(); // skip "age"
    assertEquals(30, reader.nextInt());
    reader.nextName(); // skip null
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testEmptyJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonArrayWithOneElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[\"apple\"]"));
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.nextName(); // skip array name
    reader.nextString();
    reader.nextName(); // skip null
    reader.nextNull();
    reader.expect(JsonToken.END_ARRAY);
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonArrayWithInvalidElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[\"apple\", 5]"));
    reader.nextName(); // skip array name
    reader.nextString();
    reader.nextInt(); // read int instead of string
    reader.nextName(); // skip null
    reader.nextNull();
    reader.expect(JsonToken.END_ARRAY);
  }

  @Test
  public void testJsonArrayWithMultipleElements() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[\"apple\", \"banana\", 5]"));
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.nextName(); // skip array name
    reader.nextString();
    reader.nextString();
    reader.nextName(); // skip null
    reader.nextNull();
    reader.expect(JsonToken.END_ARRAY);
    reader.close();
  }

  @Test
  public void testJsonObjectWithOneName() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": null}"));
    reader.nextName(); // skip object name
    reader.nextNull();
    reader.nextName(); // skip null
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testJsonObjectWithMultipleNames() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30, \"city\": null}"));
    reader.nextName(); // skip object name
    reader.nextString();
    reader.nextName(); // skip "age"
    reader.nextInt();
    reader.nextName(); // skip "city"
    reader.nextNull();
    reader.nextName(); // skip null
    reader.nextNull();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonObjectWithInvalidName() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": 5, \"age\": 30}"));
    reader.nextName(); // read int instead of string
    reader.nextInt();
    reader.nextName(); // skip "age"
    reader.nextInt();
    reader.close();
  }
}