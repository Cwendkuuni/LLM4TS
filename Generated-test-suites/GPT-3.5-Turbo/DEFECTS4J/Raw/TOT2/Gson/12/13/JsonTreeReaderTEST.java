import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));
    
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.beginArray();
    
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();

    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key1", new JsonPrimitive("value1"));
    jsonObject.add("key2", new JsonPrimitive("value2"));

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();

    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key1", new JsonPrimitive("value1"));
    jsonObject.add("key2", new JsonPrimitive("value2"));

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();

    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.beginArray();

    assertTrue(jsonTreeReader.hasNext());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key1", new JsonPrimitive("value1"));
    JsonObject nestedObject = new JsonObject();
    nestedObject.add("nestedKey", new JsonPrimitive("nestedValue"));
    jsonObject.add("key2", nestedObject);

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.promoteNameToValue();

    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key1", new JsonPrimitive("value1"));
    jsonObject.add("key2", new JsonPrimitive("value2"));

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    String name = jsonTreeReader.nextName();

    assertEquals("key1", name);
    assertEquals(JsonToken.NAME, jsonTreeReader.peek());
  }

  @Test
  public void testNextString() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("key", new JsonPrimitive("value"));

    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    String value = jsonTreeReader.nextString();

    assertEquals("value", value);
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

}