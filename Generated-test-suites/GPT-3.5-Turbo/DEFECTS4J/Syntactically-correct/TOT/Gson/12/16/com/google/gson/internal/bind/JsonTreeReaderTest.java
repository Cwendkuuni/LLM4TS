import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {
  
  private JsonTreeReader jsonTreeReader;
  
  @Before
  public void setup() {
    // Create a sample JsonElement for testing
    JsonElement jsonElement = new JsonObject();
    jsonElement.getAsJsonObject().add("name", new JsonPrimitive("John"));
    jsonElement.getAsJsonObject().addProperty("age", 25);
    
    // Initialize JsonTreeReader with the sample JsonElement
    jsonTreeReader = new JsonTreeReader(jsonElement);
  }
  
  @Test
  public void testBeginArray() throws Exception {
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
  }
  
  @Test
  public void testEndArray() throws Exception {
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
  }
  
  @Test
  public void testBeginObject() throws Exception {
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
  }
  
  @Test
  public void testEndObject() throws Exception {
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }
  
  @Test
  public void testHasNext() throws Exception {
    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endArray();
    assertFalse(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testPeek() throws Exception {
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
  }
  
  @Test
  public void testNextName() throws Exception {
    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
  }
  
  @Test
  public void testNextString() throws Exception {
    assertEquals("John", jsonTreeReader.nextString());
  }
  
  @Test
  public void testNextBoolean() throws Exception {
    jsonTreeReader.skipValue();
    assertTrue(jsonTreeReader.nextBoolean());
  }
  
  @Test
  public void testNextNull() throws Exception {
    jsonTreeReader.skipValue();
    jsonTreeReader.nextNull();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
  }
  
  @Test
  public void testNextDouble() throws Exception {
    jsonTreeReader.skipValue();
    assertEquals(25.0, jsonTreeReader.nextDouble(), 0);
  }
  
  @Test
  public void testNextLong() throws Exception {
    assertEquals(25L, jsonTreeReader.nextLong());
  }
  
  @Test
  public void testNextInt() throws Exception {
    assertEquals(25, jsonTreeReader.nextInt());
  }
  
  @Test
  public void testClose() throws Exception {
    jsonTreeReader.close();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }
  
  @Test
  public void testSkipValue() throws Exception {
    jsonTreeReader.beginObject();
    jsonTreeReader.skipValue();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
  }
  
  @Test
  public void testGetPath() throws Exception {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    assertEquals("$.name", jsonTreeReader.getPath());
  }
  
  @Test
  public void testPromoteNameToValue() throws Exception {
    jsonTreeReader.beginObject();
    jsonTreeReader.promoteNameToValue();
    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
  }

}