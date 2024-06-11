import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {
  
  @Test
  public void testBeginArray() throws IOException {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.beginArray();
    JsonToken token = reader.peek();
    assertTrue(token == JsonToken.END_ARRAY);
  }
  
  @Test
  public void testEndArray() throws IOException {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.endArray();
    JsonToken token = reader.peek();
    assertTrue(token == JsonToken.END_DOCUMENT);
  }
  
  @Test
  public void testBeginObject() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.beginObject();
    JsonToken token = reader.peek();
    assertTrue(token == JsonToken.END_OBJECT);
  }
  
  @Test
  public void testEndObject() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.endObject();
    JsonToken token = reader.peek();
    assertTrue(token == JsonToken.END_DOCUMENT);
  }
  
  @Test
  public void testHasNext() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    boolean hasNext = reader.hasNext();
    assertFalse(hasNext);
  }
  
  @Test
  public void testNextName() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("name", "value");
    JsonTreeReader reader = new JsonTreeReader(object);
    String name = reader.nextName();
    assertEquals("name", name);
  }
  
  @Test
  public void testNextString() throws IOException {
    JsonElement element = new JsonPrimitive("value");
    JsonTreeReader reader = new JsonTreeReader(element);
    String value = reader.nextString();
    assertEquals("value", value);
  }
  
  @Test
  public void testNextBoolean() throws IOException {
    JsonElement element = new JsonPrimitive(true);
    JsonTreeReader reader = new JsonTreeReader(element);
    boolean value = reader.nextBoolean();
    assertTrue(value);
  }
  
  @Test
  public void testNextNull() throws IOException {
    JsonElement element = JsonNull.INSTANCE;
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.nextNull();
    JsonToken token = reader.peek();
    assertTrue(token == JsonToken.END_DOCUMENT);
  }
  
  @Test
  public void testNextDouble() throws IOException {
    JsonElement element = new JsonPrimitive(1.5);
    JsonTreeReader reader = new JsonTreeReader(element);
    double value = reader.nextDouble();
    assertEquals(1.5, value, 0.001);
  }
  
  @Test
  public void testNextLong() throws IOException {
    JsonElement element = new JsonPrimitive(10L);
    JsonTreeReader reader = new JsonTreeReader(element);
    long value = reader.nextLong();
    assertEquals(10L, value);
  }
  
  @Test
  public void testNextInt() throws IOException {
    JsonElement element = new JsonPrimitive(5);
    JsonTreeReader reader = new JsonTreeReader(element);
    int value = reader.nextInt();
    assertEquals(5, value);
  }
  
  @Test
  public void testClose() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.close();
    JsonToken token = reader.peek();
    assertTrue(token == JsonToken.END_DOCUMENT);
  }
  
  @Test
  public void testSkipValue() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.skipValue();
    JsonToken token = reader.peek();
    assertTrue(token == JsonToken.END_DOCUMENT);
  }
  
  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("name", "value");
    JsonTreeReader reader = new JsonTreeReader(object);
    reader.promoteNameToValue();
    String name = reader.nextString();
    assertEquals("name", name);
  }
  
  @Test
  public void testGetPath() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    String path = reader.getPath();
    assertEquals("$", path);
  }
}