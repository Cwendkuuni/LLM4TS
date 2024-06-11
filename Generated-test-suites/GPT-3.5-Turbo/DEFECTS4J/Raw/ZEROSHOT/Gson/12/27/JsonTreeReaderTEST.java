import org.junit.Test;
import static org.junit.Assert.*;
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
  
  private JsonTreeReader jsonTreeReader;
  
  @Test
  public void testBeginArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.beginArray();
    assertEquals(jsonTreeReader.peek(), JsonToken.BEGIN_ARRAY);
  }
  
  @Test
  public void testEndArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonTreeReader = new JsonTreeReader(jsonArray);
    jsonTreeReader.endArray();
    assertEquals(jsonTreeReader.peek(), JsonToken.END_ARRAY);
  }
  
  @Test
  public void testBeginObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    assertEquals(jsonTreeReader.peek(), JsonToken.BEGIN_OBJECT);
  }
  
  @Test
  public void testEndObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.endObject();
    assertEquals(jsonTreeReader.peek(), JsonToken.END_OBJECT);
  }
  
  @Test
  public void testHasNext() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonObject);
    assertFalse(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testPeek() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonObject);
    assertEquals(jsonTreeReader.peek(), JsonToken.END_DOCUMENT);
  }
  
  @Test
  public void testNextName() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonTreeReader = new JsonTreeReader(jsonObject);
    String name = jsonTreeReader.nextName();
    assertEquals(name, "name");
  }
  
  @Test
  public void testNextString() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonTreeReader = new JsonTreeReader(jsonObject);
    String value = jsonTreeReader.nextString();
    assertEquals(value, "John");
  }
  
  @Test
  public void testNextBoolean() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("isAdult", true);
    jsonTreeReader = new JsonTreeReader(jsonObject);
    boolean value = jsonTreeReader.nextBoolean();
    assertTrue(value);
  }
  
  @Test
  public void testNextNull() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("name", JsonNull.INSTANCE);
    jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.nextNull();
    assertEquals(jsonTreeReader.peek(), JsonToken.END_OBJECT);
  }
  
  @Test
  public void testNextDouble() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("rating", 4.5);
    jsonTreeReader = new JsonTreeReader(jsonObject);
    double value = jsonTreeReader.nextDouble();
    assertEquals(value, 4.5, 0.01);
  }
  
  @Test
  public void testNextLong() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("count", 100000000000L);
    jsonTreeReader = new JsonTreeReader(jsonObject);
    long value = jsonTreeReader.nextLong();
    assertEquals(value, 100000000000L);
  }
  
  @Test
  public void testNextInt() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("age", 25);
    jsonTreeReader = new JsonTreeReader(jsonObject);
    int value = jsonTreeReader.nextInt();
    assertEquals(value, 25);
  }
  
  @Test
  public void testClose() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.close();
    assertEquals(jsonTreeReader.peek(), JsonToken.END_DOCUMENT);
  }
  
  @Test
  public void testSkipValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.skipValue();
    assertEquals(jsonTreeReader.peek(), JsonToken.END_OBJECT);
  }
  
  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.promoteNameToValue();
    assertEquals(jsonTreeReader.peek(), JsonToken.STRING);
  }
  
  @Test
  public void testGetPath() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonTreeReader = new JsonTreeReader(jsonObject);
    String path = jsonTreeReader.getPath();
    assertEquals(path, "$");
  }
  
}