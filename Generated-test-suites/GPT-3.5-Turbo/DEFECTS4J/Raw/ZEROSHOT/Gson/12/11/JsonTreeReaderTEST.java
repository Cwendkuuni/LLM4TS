import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("test"));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("test"));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("test"));
    jsonArray.add(new JsonPrimitive(123));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.nextString();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.nextInt();
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("test"));
    jsonArray.add(new JsonPrimitive(123));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.nextString();
    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    jsonTreeReader.nextInt();
    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key1", new JsonPrimitive("value1"));
    jsonObject.add("key2", new JsonPrimitive("value2"));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    assertEquals("key1", jsonTreeReader.nextName());
    assertEquals("key1", jsonTreeReader.getPath());
    assertEquals("value1", jsonTreeReader.nextString());
    assertEquals("key2", jsonTreeReader.nextName());
    assertEquals("key2", jsonTreeReader.getPath());
    assertEquals("value2", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
  }

  @Test
  public void testNextString() throws IOException {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
    assertEquals("test", jsonTreeReader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
    assertTrue(jsonTreeReader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    JsonNull jsonNull = JsonNull.INSTANCE;
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonNull);
    jsonTreeReader.nextNull();
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123.45);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
    assertEquals(123.45, jsonTreeReader.nextDouble(), 0.0);
  }

  @Test
  public void testNextLong() throws IOException {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(12345L);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
    assertEquals(12345L, jsonTreeReader.nextLong());
  }

  @Test
  public void testNextInt() throws IOException {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonPrimitive);
    assertEquals(123, jsonTreeReader.nextInt());
  }

  @Test
  public void testClose() throws IOException {
    JsonObject jsonObject = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.close();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", "value2");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    assertEquals("key1", jsonTreeReader.nextName());
    jsonTreeReader.skipValue();
    assertEquals("null", jsonTreeReader.getPath());
    assertEquals("key2", jsonTreeReader.nextName());
    assertEquals("value2", jsonTreeReader.nextString());
    jsonTreeReader.skipValue();
    assertEquals("null", jsonTreeReader.getPath());
    jsonTreeReader.endObject();
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", "value2");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.promoteNameToValue();
    assertEquals("value1", jsonTreeReader.nextString());
    assertEquals("key2", jsonTreeReader.nextName());
    assertEquals("value2", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
  }

  @Test
  public void testGetPath() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key1", "value1");
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("test"));
    jsonArray.add(jsonObject);
    JsonObject rootObject = new JsonObject();
    rootObject.add("key2", jsonArray);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(rootObject);
    assertEquals("$", jsonTreeReader.getPath());
    jsonTreeReader.beginObject();
    assertEquals("$.key2", jsonTreeReader.getPath());
    jsonTreeReader.beginArray();
    assertEquals("$.key2[0]", jsonTreeReader.getPath());
    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    assertEquals("test", jsonTreeReader.nextString());
    assertEquals("$.key2[1]", jsonTreeReader.getPath());
    jsonTreeReader.beginObject();
    assertEquals("$.key2[1].key1", jsonTreeReader.getPath());
    assertEquals("value1", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
  }

}