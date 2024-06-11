import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJsonElement() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject());
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testSimpleJsonArray() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonArray());
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.endArray();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonObjectWithStringKeyAndValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("key", "value"));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    assertEquals("key", jsonTreeReader.nextName());
    assertEquals("value", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonObjectWithNullValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("key", null));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    assertEquals("key", jsonTreeReader.nextName());
    jsonTreeReader.nextNull();
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonObjectWithBooleanValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("key", true));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    assertEquals("key", jsonTreeReader.nextName());
    assertTrue(jsonTreeReader.nextBoolean());
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonObjectWithNumberValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("key", 1));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    assertEquals("key", jsonTreeReader.nextName());
    assertEquals(1, jsonTreeReader.nextInt());
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonObjectWithArrayValue() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().add("key", new JsonArray()));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    assertEquals("key", jsonTreeReader.nextName());
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonObjectWithNullKey() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject().addProperty("", 1));
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.nextNull();
    jsonTreeReader.nextInt();
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonReaderClosedException() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonObject());
    jsonTreeReader.close();
    jsonTreeReader.nextString();
  }
}