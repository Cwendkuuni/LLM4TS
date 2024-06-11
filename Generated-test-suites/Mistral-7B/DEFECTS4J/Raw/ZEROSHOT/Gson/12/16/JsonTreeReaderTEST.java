import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonElement jsonElement;

  @Before
  public void setup() {
    String jsonString = "{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"]}";
    jsonElement = new JsonParser().parse(jsonString);
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testHasNextWhenReaderIsClosed() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.close();
    jsonTreeReader.hasNext();
  }

  @Test
  public void testPeek() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();

    assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.close();
  }

  @Test
  public void testExpect() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
    jsonTreeReader.beginArray();

    jsonTreeReader.expect(JsonToken.NAME);
    jsonTreeReader.nextName();

    jsonTreeReader.expect(JsonToken.NUMBER);
    jsonTreeReader.nextInt();

    jsonTreeReader.expect(JsonToken.END_ARRAY);
    jsonTreeReader.endArray();

    jsonTreeReader.expect(JsonToken.END_OBJECT);
    jsonTreeReader.endObject();

    jsonTreeReader.expect(JsonToken.END_DOCUMENT);
    jsonTreeReader.close();
  }

  @Test
  public void testNextName() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();

    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("age", jsonTreeReader.nextName());
    assertEquals("hobbies", jsonTreeReader.nextName());

    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testNextString() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals("John", jsonTreeReader.nextString());

    jsonTreeReader.beginArray();
    jsonTreeReader.nextString();
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
    jsonTreeReader.close();
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonParser().parse("{ \"bool\": true }"));

    assertTrue(jsonTreeReader.nextBoolean());

    jsonTreeReader.close();
  }

  @Test
  public void testNextNull() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonParser().parse("{ \"null\": null }"));

    jsonTreeReader.nextNull();

    jsonTreeReader.close();
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonParser().parse("{ \"double\": 3.14 }"));

    assertEquals(3.14, jsonTreeReader.nextDouble(), 0.0);

    jsonTreeReader.close();
  }

  @Test
  public void testNextLong() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonParser().parse("{ \"long\": 42 }"));

    assertEquals(42L, jsonTreeReader.nextLong());

    jsonTreeReader.close();
  }

  @Test
  public void testNextInt() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonParser().parse("{ \"int\": 5 }"));

    assertEquals(5, jsonTreeReader.nextInt());

    jsonTreeReader.close();
  }
}