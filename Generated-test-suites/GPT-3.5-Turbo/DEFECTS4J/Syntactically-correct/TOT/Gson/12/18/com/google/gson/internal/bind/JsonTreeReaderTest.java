import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.StringReader;

public class JsonTreeReaderTest {
  
  private JsonTreeReader jsonTreeReader;
  
  @Before
  public void setup() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    jsonObject.addProperty("age", 30);
    
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));
    jsonArray.add(new JsonPrimitive(3));
    
    jsonObject.add("numbers", jsonArray);
    
    jsonTreeReader = new JsonTreeReader(jsonObject);
  }
  
  @Test
  public void testBeginArray() throws IOException {
    jsonTreeReader.beginArray();
    assertEquals(JsonArray.class, jsonTreeReader.peek().getClass());
    assertTrue(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testEndArray() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    assertEquals(JsonElement.class, jsonTreeReader.peek().getClass());
    assertFalse(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testBeginObject() throws IOException {
    jsonTreeReader.beginObject();
    assertEquals(JsonObject.class, jsonTreeReader.peek().getClass());
    assertTrue(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testEndObject() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    assertEquals(JsonElement.class, jsonTreeReader.peek().getClass());
    assertFalse(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testHasNext() throws IOException {
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testPeek() throws IOException {
    assertEquals(JsonElement.class, jsonTreeReader.peek().getClass());
    jsonTreeReader.beginArray();
    JsonElement peekedElement = jsonTreeReader.peek();
    assertEquals(JsonArray.class, peekedElement.getClass());
    assertTrue(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testNextName() throws IOException {
    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("age", jsonTreeReader.nextName());
  }
  
  @Test
  public void testNextString() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    assertEquals("John", jsonTreeReader.nextString());
  }
  
  @Test
  public void testNextBoolean() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    assertEquals(false, jsonTreeReader.nextBoolean());
  }
  
  @Test
  public void testNextNull() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    assertNull(jsonTreeReader.nextNull());
  }
  
  @Test
  public void testNextDouble() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    assertEquals(1.0, jsonTreeReader.nextDouble(), 0.0);
  }
  
  @Test
  public void testNextLong() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    assertEquals(2L, jsonTreeReader.nextLong());
  }
  
  @Test
  public void testNextInt() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    assertEquals(3, jsonTreeReader.nextInt());
  }
  
  @Test
  public void testClose() throws IOException {
    jsonTreeReader.close();
    assertEquals(JsonElement.class, jsonTreeReader.peek().getClass());
    assertFalse(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testSkipValue() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.skipValue();
    jsonTreeReader.nextName();
    jsonTreeReader.skipValue();
    assertEquals(JsonPrimitive.class, jsonTreeReader.peek().getClass());
  }
  
  @Test
  public void testToString() {
    assertEquals("JsonTreeReader", jsonTreeReader.toString());
  }
  
  @Test
  public void testGetPath() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.beginArray();
    jsonTreeReader.skipValue();
    assertEquals("$.name[0]", jsonTreeReader.getPath());
  }
  
  @Test
  public void testPromoteNameToValue() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.promoteNameToValue();
    assertEquals(JsonPrimitive.class, jsonTreeReader.peek().getClass());
  }

}