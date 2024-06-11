import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReader_EmptyJsonElement() {
    JsonElement jsonElement = JsonParser.parseString("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    try {
      jsonTreeReader.beginArray();
      fail("Expected JsonTreeReader to throw an IOException when reading an empty JsonObject.");
    } catch (IOException e) {
      // Expected
    }

    try {
      jsonTreeReader.nextString();
      fail("Expected JsonTreeReader to throw an IllegalStateException when reading an empty JsonObject.");
    } catch (IllegalStateException e) {
      // Expected
    }

    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_NullJsonElement() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(null);

    try {
      jsonTreeReader.nextName();
      fail("Expected JsonTreeReader to throw an IllegalStateException when reading a null JsonElement.");
    } catch (IllegalStateException e) {
      // Expected
    }

    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_EmptyArrayJsonElement() {
    JsonElement jsonElement = JsonParser.parseString("[]");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endArray();
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_SingleStringElementInArrayJsonElement() {
    JsonElement jsonElement = JsonParser.parseString("[\"hello\"]");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    assertEquals("hello", jsonTreeReader.nextString());
    jsonTreeReader.endArray();
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_MultipleStringElementsInArrayJsonElement() {
    JsonElement jsonElement = JsonParser.parseString("[\"hello\", \"world\"]");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    assertEquals("hello", jsonTreeReader.nextString());
    assertEquals("world", jsonTreeReader.nextString());
    jsonTreeReader.endArray();
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_EmptyObjectJsonElement() {
    JsonElement jsonElement = JsonParser.parseString("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    fail("Expected JsonTreeReader to throw an IOException when reading an empty JsonObject.");
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_SingleStringKeySingleStringValueObjectJsonElement() {
    JsonElement jsonElement = JsonParser.parseString("{\"key\": \"value\"}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertEquals("key", jsonTreeReader.nextName());
    assertEquals("value", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_SingleStringKeyMultipleValuesObjectJsonElement() {
    JsonElement jsonElement = JsonParser.parseString("{\"key\": [1, 2, 3]}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertEquals("key", jsonTreeReader.nextName());
    jsonTreeReader.beginArray();
    assertEquals(1, jsonTreeReader.nextInt());
    assertEquals(2, jsonTreeReader.nextInt());
    assertEquals(3, jsonTreeReader.nextInt());
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_MultipleKeyValuePairsObjectJsonElement() {
    JsonElement jsonElement = JsonParser.parseString("{\"key1\": \"value1\", \"key2\": \"value2\"}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertEquals("key1", jsonTreeReader.nextName());
    assertEquals("value1", jsonTreeReader.nextString());
    assertEquals("key2", jsonTreeReader.nextName());
    assertEquals("value2", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }
}