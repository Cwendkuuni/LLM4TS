import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;

public class JsonTreeReaderTest {

  

  @Test
  public void testEmptyJsonElement() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testEmptyJsonArray() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("[]");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.endArray();
    jsonTreeReader.close();
  }

  @Test
  public void testEmptyJsonObject() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testValidJsonObject() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("{\"name\": \"John\", \"age\": 30}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());
    assertEquals("age", jsonTreeReader.nextName());
    jsonTreeReader.skipValue(); // age (30)
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testValidJsonArray() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("[1, 2, 3, \"four\"]");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    assertEquals(1, jsonTreeReader.nextInt());
    jsonTreeReader.endArray();
    jsonTreeReader.beginArray();
    assertEquals(2, jsonTreeReader.nextInt());
    jsonTreeReader.endArray();
    assertEquals("four", jsonTreeReader.nextString());
    jsonTreeReader.endArray();
    jsonTreeReader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonReaderClosed() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("{\"name\": \"John\"}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.close();
    jsonTreeReader.nextName();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonReaderEndDocument() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.close();
    jsonTreeReader.beginArray();
  }

  @Test(expected = AssertionError.class)
  public void testJsonReaderReadChars() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("{\"name\": \"John\"}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.read(new char[10]);
  }

  @Test(expected = AssertionError.class)
  public void testJsonReaderClose() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("{\"name\": \"John\"}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.close();
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReaderPromoteNameToValue() throws IOException {
    JsonElement jsonElement = JsonParser.parseString("{\"name\": null}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.promoteNameToValue();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    jsonTreeReader.nextNull();
    jsonTreeReader.nextNull(); // name
    jsonTreeReader.close();
  }

  
}