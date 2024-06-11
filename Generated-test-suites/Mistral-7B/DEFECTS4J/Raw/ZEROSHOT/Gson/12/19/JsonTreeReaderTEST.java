import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonReader;
import com.google.gson.stream.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTreeReaderTest {

  private static JsonElement createTestJson() {
    return new JsonParser().parse("{" +
        "  \"name\": \"John\"," +
        "  \"age\": 30," +
        "  \"hobbies\": [" +
        "    \"reading\"," +
        "    \"programming\"" +
        "  ]," +
        "  \"address\": {" +
        "    \"street\": \"Main St\"," +
        "    \"city\": \"Anytown\"," +
        "    \"state\": \"CA\"" +
        "  }," +
        "  \"education\": [" +
        "    {" +
        "      \"degree\": \"BS\"," +
        "      \"major\": \"Math\"" +
        "    }," +
        "    {" +
        "      \"degree\": \"MS\"," +
        "      \"major\": \"CS\"" +
        "    }" +
        "  ]" +
        "}");
  }

  @org.junit.Test
  public void testJsonTreeReader_basic() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createTestJson());

    // Test basic functionality
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertFalse(reader.hasNext());

    // Test beginObject and endObject
    reader.beginObject();
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.endObject();
    assertFalse(reader.hasNext());

    // Test beginArray and endArray
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals("hobbies", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    JsonElement hobby0 = reader.nextString();
    assertEquals("reading", hobby0);
    JsonElement hobby1 = reader.nextString();
    assertEquals("programming", hobby1);
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();

    // Test nextNull
    reader.nextNull();
    assertNull(reader.nextString());

    // Test nextBoolean
    reader.nextName(); // "address"
    reader.beginObject();
    reader.nextName(); // "city"
    reader.nextBoolean(); // false (not present in JSON)
    reader.endObject();
    reader.endObject();
    assertFalse(reader.hasNext());

    // Test nextDouble and nextLong
    reader.nextName(); // "education"
    reader.beginArray();
    reader.nextName(); // { "degree": "BS" }
    reader.nextName(); // "major"
    assertEquals(2.0, reader.nextDouble(), 0.0); // actually a string, but gets parsed as double
    reader.endObject();
    reader.nextName(); // { "degree": "MS" }
    reader.nextName(); // "major"
    assertEquals(45L, reader.nextLong());
    reader.endObject();
    reader.endArray();

    // Test nextInt
    reader.nextName(); // "education"
    reader.beginArray();
    reader.nextName(); // { "degree": "BS" }
    reader.nextName(); // "major"
    assertEquals(2, reader.nextInt());
    reader.endObject();
    reader.nextName(); // { "degree": "MS" }
    reader.nextName(); // "major"
    assertEquals(45, reader.nextInt());
    reader.endObject();
    reader.endArray();

    // Test close()
    reader.close();
    assertFalse(reader.hasNext());
  }

  @org.junit.Test
  public void testJsonTreeReader_promoteNameToValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createTestJson());

    reader.promoteNameToValue();
    assertEquals("name", reader.nextString());
    assertEquals("John", reader.nextString());
    assertEquals("age", reader.nextInt());
    reader.promoteNameToValue();
    assertEquals("hobbies", reader.nextString());
    reader.beginArray();
    assertEquals("reading", reader.nextString());
    assertEquals("programming", reader.nextString());
    reader.endArray();
    reader.promoteNameToValue();
    assertEquals("address", reader.nextString());
    reader.beginObject();
    reader.nextName(); // "street"
    assertEquals("Main St", reader.nextString());
    reader.nextName(); // "city"
    assertEquals("Anytown", reader.nextString());
    reader.nextName(); // "state"
    assertEquals("CA", reader.nextString());
    reader.endObject();
    reader.promoteNameToValue();
    assertEquals("education", reader.nextString());
    reader.beginArray();
    reader.nextName(); // { "degree": "BS" }
    reader.nextName(); // "major"
    assertEquals("Math", reader.nextString());
    reader.endObject();
    reader.nextName(); // { "degree": "MS" }
    reader.nextName(); // "major"
    assertEquals("CS", reader.nextString());
    reader.endArray();

    assertFalse(reader.hasNext());
  }

  @org.junit.Test
  public void testJsonTreeReader_lenient() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createTestJson());
    reader.setLenient(true);

    // Test nextDouble with NaN and infinities
    reader.nextName(); // "education"
    reader.beginArray();
    reader.nextName(); // { "degree": "BS" }
    reader.nextName(); // "major"
    assertEquals(Double.NaN, reader.nextDouble(), 0.0);
    reader.endObject();
    reader.nextName(); // { "degree": "MS" }
    reader.nextName(); // "major"
    assertEquals(Double.POSITIVE_INFINITY, reader.nextDouble(), 0.0);
    reader.endObject();
    reader.endArray();

    reader.close();
    assertFalse(reader.hasNext());
  }

  @org.junit.Test
  public void testJsonTreeReader_customInput() throws IOException {
    String json = "{\"foo\": [1, 2, 3], \"bar\": [4, 5, 6], \"qux\": [7, 8, 9]}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        return new JsonParser().parse(json).getAsJsonArray();
      }

      @Override
      public JsonObject getAsJsonObject() {
        return null;
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override
      public JsonNull getAsJsonNull() {
        return null;
      }

      @Override
      public List<Map.Entry<String, JsonElement>> getEntrySet() {
        return Arrays.asList(
            new AbstractMap.SimpleEntry<>("foo", getAsJsonArray())
        );
      }
    });

    reader.beginArray();
    assertEquals("foo", reader.nextName());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.nextName(); // "bar"
    reader.beginArray();
    assertEquals(4, reader.nextInt());
    assertEquals(5, reader.nextInt());
    assertEquals(6, reader.nextInt());
    reader.endArray();
    reader.endArray();

    reader.close();
    assertFalse(reader.hasNext());
  }
}