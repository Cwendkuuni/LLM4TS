import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {
  
  private JsonTreeReader reader;
  
  @Before
  public void setup() {
    // Initialize a JsonElement for testing
    reader = new JsonTreeReader(createJsonElement());
  }
  
  private JsonElement createJsonElement() {
    // Create a JsonElement object for testing
    JsonElement element = new JsonElement();
    element.addProperty("name", "John");
    element.addProperty("age", 30);
    element.addProperty("isStudent", true);
    
    JsonArray hobbies = new JsonArray();
    hobbies.add("reading");
    hobbies.add("writing");
    hobbies.add("coding");
    element.add("hobbies", hobbies);
    
    return element;
  }
  
  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    // Assert that the current token is BEGIN_ARRAY
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }
  
  @Test
  public void testEndArray() throws IOException {
    reader.endArray();
    // Assert that the current token is END_ARRAY
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }
  
  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    // Assert that the current token is BEGIN_OBJECT
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }
  
  @Test
  public void testEndObject() throws IOException {
    reader.endObject();
    // Assert that the current token is END_OBJECT
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }
  
  @Test
  public void testHasNext() throws IOException {
    // Assert that hasNext returns true for the initial JsonElement
    assertTrue(reader.hasNext());
    
    // Move to the next token
    reader.peek();
    
    // Assert that hasNext returns false for the last token
    assertFalse(reader.hasNext());
  }
  
  @Test
  public void testPeek() throws IOException {
    // Assert that the initial token is END_DOCUMENT
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    
    // Move to the next token
    reader.peek();
    
    // Assert that the token is not END_DOCUMENT anymore
    assertNotEquals(JsonToken.END_DOCUMENT, reader.peek());
  }
  
  @Test
  public void testNextName() throws IOException {
    // Assert that the next name is "name"
    assertEquals("name", reader.nextName());
  }
  
  @Test
  public void testNextString() throws IOException {
    // Assert that the next string is "John"
    assertEquals("John", reader.nextString());
  }
  
  @Test
  public void testNextBoolean() throws IOException {
    // Assert that the next boolean is true
    assertTrue(reader.nextBoolean());
  }
  
  @Test
  public void testNextNull() throws IOException {
    // Assert that the next value is null
    reader.nextNull();
    assertEquals(JsonToken.NULL, reader.peek());
  }
  
  @Test
  public void testNextDouble() throws IOException {
    // Assert that the next double is 30.0
    assertEquals(30.0, reader.nextDouble(), 0.001);
  }
  
  @Test
  public void testNextLong() throws IOException {
    // Assert that the next long is 30
    assertEquals(30, reader.nextLong());
  }
  
  @Test
  public void testNextInt() throws IOException {
    // Assert that the next int is 30
    assertEquals(30, reader.nextInt());
  }
  
  @Test
  public void testClose() throws IOException {
    // Close the reader
    reader.close();
    
    // Assert that the current token is END_DOCUMENT
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }
  
  @Test
  public void testSkipValue() throws IOException {
    // Skip the next value
    reader.skipValue();
    
    // Assert that the current token is null
    assertEquals("null", reader.getPath());
  }
  
  @Test
  public void testGetPath() throws IOException {
    // Assert that the initial path is "$"
    assertEquals("$", reader.getPath());
    
    // Skip the next name and value
    reader.nextName();
    reader.skipValue();
    
    // Assert that the current path is "$.name"
    assertEquals("$.name", reader.getPath());
  }
  
  @Test
  public void testPromoteNameToValue() throws IOException {
    // Promote the next name to value
    reader.promoteNameToValue();
    
    // Assert that the current token is STRING
    assertEquals(JsonToken.STRING, reader.peek());
  }
  
  @Test
  public void testToString() {
    // Assert that toString returns "JsonTreeReader"
    assertEquals("JsonTreeReader", reader.toString());
  }
}