import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {
  
  private JsonTreeReader jsonTreeReader;
  private JsonElement jsonElement;
  
  @Before
  public void setUp() {
    jsonElement = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonElement);
  }
  
  @Test
  public void testBeginArray() throws IOException {
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
  }
  
  @Test
  public void testEndArray() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }
  
  @Test
  public void testBeginObject() throws IOException {
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }
  
  @Test
  public void testEndObject() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }
  
  @Test
  public void testHasNext() throws IOException {
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endObject();
    assertFalse(jsonTreeReader.hasNext());
  }
  
  @Test
  public void testPeek() throws IOException {
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }
  
  @Test
  public void testNextName() throws IOException {
    jsonTreeReader.beginObject();
    String name = jsonTreeReader.nextName();
    assertEquals("", name);
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testNextString() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    String value = jsonTreeReader.nextString();
    assertEquals("", value);
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testNextBoolean() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    boolean value = jsonTreeReader.nextBoolean();
    assertFalse(value);
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testNextNull() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextNull();
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testNextDouble() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    double value = jsonTreeReader.nextDouble();
    assertEquals(0.0, value, 0.0);
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testNextLong() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    long value = jsonTreeReader.nextLong();
    assertEquals(0L, value);
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testNextInt() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    int value = jsonTreeReader.nextInt();
    assertEquals(0, value);
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testClose() throws IOException {
    jsonTreeReader.close();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }
  
  @Test
  public void testSkipValue() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.skipValue();
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testToString() {
    assertEquals("JsonTreeReader", jsonTreeReader.toString());
  }
  
  @Test
  public void testPromoteNameToValue() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.promoteNameToValue();
    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    jsonTreeReader.endObject();
  }
  
  @Test
  public void testGetPath() {
    String path = jsonTreeReader.getPath();
    assertEquals("$", path);
  }
  
}