import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonReader.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderBasicUsage() throws IOException {
    String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\", \"hobbies\": [\"reading\", \"swimming\"]}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());

    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());

    assertEquals("city", reader.nextName());
    assertEquals("New York", reader.nextString());

    assertEquals("hobbies", reader.nextName());
    reader.beginArray();
    assertEquals("reading", reader.nextString());
    reader.beginArray();
    assertEquals("swimming", reader.nextString());
    reader.endArray();
    reader.endArray();

    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderClosed() throws IOException {
    String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\", \"hobbies\": [\"reading\", \"swimming\"]}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.close();
    reader.nextName();
  }

  @Test
  public void testJsonTreeReaderEmptyArray() throws IOException {
    String jsonString = "{\"name\": \"John\", \"hobbies\": []}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.beginObject();
    reader.beginArray();
    reader.endArray();
    reader.endObject();

    reader.close();
  }

  @Test
  public void testJsonTreeReaderEmptyObject() throws IOException {
    String jsonString = "{}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.beginObject();
    reader.endObject();

    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderNullPathIndex() throws IOException {
    String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\", \"hobbies\": [\"reading\", \"swimming\"]}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.beginObject();
    reader.beginArray();
    reader.nextInt(); // consume first element

    assertEquals(null, reader.nextName()); // should throw an exception
  }

  @Test
  public void testJsonTreeReaderNamedPath() throws IOException {
    String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\", \"hobbies\": [\"reading\", \"swimming\"]}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    reader.beginObject();
    reader.beginArray();
    assertEquals("hobbies", reader.nextName());

    reader.beginArray();
    reader.nextString(); // consume first element
    reader.beginObject();
    assertEquals("hobbies", reader.nextName());
    assertEquals("reading", reader.nextString());
    reader.endObject();
    reader.endArray();

    reader.close();
  }

  @Test
  public void testJsonTreeReaderJsonNull() throws IOException {
    String jsonString = "{\"name\": null, \"age\": 30, \"city\": \"New York\", \"hobbies\": [\"reading\", null]}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    assertEquals("name", reader.nextName());
    assertNull(reader.nextString());

    reader.nextName(); // consume "age"
    reader.nextInt();

    reader.nextName(); // consume "city"
    reader.nextString();

    reader.nextName(); // consume "hobbies"
    reader.beginArray();

    reader.nextString(); // consume "reading"
    reader.beginArray();
    reader.nextString(); // consume "swimming"
    reader.endArray();

    reader.nextNull();
    reader.endArray();

    reader.close();
  }
}