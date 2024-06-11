import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {
  @Test
  public void testJsonTreeReader() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    // Test beginArray method
    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endArray();

    // Test beginObject method
    jsonTreeReader.beginObject();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endObject();

    // Test hasNext method
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();
    assertFalse(jsonTreeReader.hasNext());

    // Test peek method with JSON array
    jsonTreeReader = new JsonTreeReader(new JsonArray());
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek().getAsByte());

    // Test peek method with JSON object
    jsonTreeReader.beginObject();
    jsonTreeReader.push(new JsonObject());
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek().getAsByte());

    // Test peek method with JSON primitive
    jsonTreeReader.push(new JsonPrimitive(123));
    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsByte());

    // Test peek method with JSON null
    jsonTreeReader.push(JsonNull.INSTANCE);
    assertEquals(JsonToken.NULL, jsonTreeReader.peek().getAsByte());

    // Test expect method with JSON array
    jsonTreeReader = new JsonTreeReader(new JsonArray());
    jsonTreeReader.beginArray();
    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);

    // Test expect method with JSON object
    jsonTreeReader = new JsonTreeReader(new JsonObject());
    jsonTreeReader.beginObject();
    jsonTreeReader.expect(JsonToken.BEGIN_OBJECT);

    // Test expect method with JSON primitive
    jsonTreeReader.push(new JsonPrimitive(123));
    jsonTreeReader.expect(JsonToken.NUMBER);

    // Test expect method with JSON null
    jsonTreeReader.push(JsonNull.INSTANCE);
    jsonTreeReader.expect(JsonToken.NULL);

    // Test unexpected JSON token
    jsonTreeReader = new JsonTreeReader(new JsonObject());
    jsonTreeReader.beginObject();
    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);

    // Test close method
    jsonTreeReader = new JsonTreeReader(new JsonObject());
    jsonTreeReader.close();
    assertThrows(IllegalStateException.class, jsonTreeReader::nextName);
  }
}