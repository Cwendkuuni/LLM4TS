
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeReaderTest {
  
  @Test
  public void testBeginArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertTrue(reader.hasNext());
  }
  
  // ...continue writing basic test cases for all public methods
  
  @Test
  public void testNextString() throws IOException {
    JsonPrimitive stringPrimitive = new JsonPrimitive("hello");
    JsonTreeReader reader = new JsonTreeReader(stringPrimitive);
    assertEquals("hello", reader.nextString());
  }
  
  // ...continue writing basic test cases for all public methods
  
  @Test
  public void testNextBoolean() throws IOException {
    JsonPrimitive booleanPrimitive = new JsonPrimitive(true);
    JsonTreeReader reader = new JsonTreeReader(booleanPrimitive);
    assertTrue(reader.nextBoolean());
  }
  
  // ...continue writing basic test cases for all public methods
  
  @Test
  public void testNextNull() throws IOException {
    JsonNull jsonNull = JsonNull.INSTANCE;
    JsonTreeReader reader = new JsonTreeReader(jsonNull);
    reader.nextNull();
    assertEquals(JsonToken.NULL, reader.peek());
  }
  
  // ...continue writing basic test cases for all public methods
  
  @Test
  public void testNextDouble() throws IOException {
    JsonPrimitive doublePrimitive = new JsonPrimitive(3.14);
    JsonTreeReader reader = new JsonTreeReader(doublePrimitive);
    assertEquals(3.14, reader.nextDouble(), 0.0);
  }
  
  // ...continue writing basic test cases for all public methods
  
  @Test
  public void testPath() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    reader.nextName();
    assertEquals("$.name", reader.getPath());
  }
  
  // ...continue writing basic test cases for all public methods
  
  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.promoteNameToValue();
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("name", reader.nextString());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("John", reader.nextString());
  }
  
}

