import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderWithEmptyJsonObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertTrue(jsonTreeReader.hasNext());

    // Test endObject()
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());

    // Test close()
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReaderWithEmptyJsonArray() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginArray()
    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());

    // Test endArray()
    jsonTreeReader.endArray();
    assertFalse(jsonTreeReader.hasNext());

    // Test close()
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReaderWithJsonObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    jsonElement.addProperty("name", "John Doe");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginObject()
    jsonTreeReader.beginObject();
    assertTrue(jsonTreeReader.hasNext());

    // Test nextName()
    assertEquals("name", jsonTreeReader.nextName());

    // Test nextString()
    assertEquals("John Doe", jsonTreeReader.nextString());

    // Test endObject()
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());

    // Test close()
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReaderWithJsonArray() throws IOException {
    JsonElement jsonElement = new JsonArray();
    jsonElement.add(new JsonPrimitive(1));
    jsonElement.add(new JsonPrimitive(2));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginArray()
    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());

    // Test nextInt()
    assertEquals(1, jsonTreeReader.nextInt());

    // Test nextInt()
    assertEquals(2, jsonTreeReader.nextInt());

    // Test endArray()
    jsonTreeReader.endArray();
    assertFalse(jsonTreeReader.hasNext());

    // Test close()
    jsonTreeReader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderWithClosedReader() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive(1).getAsJson());
    jsonTreeReader.close();
    jsonTreeReader.nextInt();
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeReaderWithUnexpectedJsonToken() throws IOException {
    JsonElement jsonElement = new JsonObject();
    jsonElement.addProperty("name", "John Doe");
    jsonElement.add(new JsonPrimitive(1));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test peek()
    jsonTreeReader.beginArray();
    jsonTreeReader.nextInt();
    jsonTreeReader.nextInt();
    jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderWithUnexpectedJsonTokenDuringObjectParsing() throws IOException {
    JsonElement jsonElement = new JsonObject();
    jsonElement.addProperty("name", "John Doe");
    jsonElement.addProperty("age", 30);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test nextName()
    jsonTreeReader.nextName();
    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderWithUnexpectedJsonTokenDuringArrayParsing() throws IOException {
    JsonElement jsonElement = new JsonObject();
    jsonElement.addProperty("name", "John Doe");
    jsonElement.addProperty("age", 30);
    jsonElement.add(new JsonPrimitive(1));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test nextInt()
    jsonTreeReader.nextInt();
    jsonTreeReader.nextInt();
    jsonTreeReader.expect(JsonToken.NAME);
  }
}