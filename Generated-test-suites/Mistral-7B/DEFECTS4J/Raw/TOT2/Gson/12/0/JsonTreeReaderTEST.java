import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {
  @Test
  public void testJsonTreeReader_EmptyJsonArray() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();

    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_SingleJsonObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    jsonElement.addProperty("name", "value");

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("value", jsonTreeReader.nextString());
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_NestedJsonObjects() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "value");
    jsonArray.add(jsonObject);
    jsonElement.add("array", jsonArray);

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    assertEquals("array", jsonTreeReader.nextName());
    jsonTreeReader.beginArray();

    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("value", jsonTreeReader.nextString());
    jsonTreeReader.endObject();

    jsonTreeReader.endArray();
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_EmptyJsonObject() throws IOException {
    JsonElement jsonElement = new JsonObject();

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_EmptyJsonArrayWithNullName() throws IOException {
    JsonElement jsonElement = null;
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();

    assertNull(jsonTreeReader.nextName());
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_EmptyJsonObjectWithNullName() throws IOException {
    JsonElement jsonElement = null;
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();

    assertNull(jsonTreeReader.nextName());
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_NullJsonElement() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(null);

    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeReader_ClosedJsonReader() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive(true));
    jsonTreeReader.close();
    jsonTreeReader.nextInt();
  }
}