import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {
  
  private JsonTreeReader reader;
  
  @Before
  public void setUp() {
    JsonElement element = new JsonObject();
    reader = new JsonTreeReader(element);
  }
  
  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }
  
  @Test
  public void testEndArray() throws IOException {
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }
  
  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }
  
  @Test
  public void testEndObject() throws IOException {
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }
  
  @Test
  public void testHasNext() throws IOException {
    assertFalse(reader.hasNext());
  }
  
  @Test
  public void testPeek() throws IOException {
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }
  
  @Test
  public void testNextName() throws IOException {
    assertNull(reader.nextName());
  }
  
  @Test
  public void testNextString() throws IOException {
    assertNull(reader.nextString());
  }
  
  @Test
  public void testNextBoolean() throws IOException {
    assertFalse(reader.nextBoolean());
  }
  
  @Test
  public void testNextNull() throws IOException {
    reader.nextNull();
    assertEquals(JsonToken.NULL, reader.peek());
  }
  
  @Test
  public void testNextDouble() throws IOException {
    assertEquals(0.0, reader.nextDouble(), 0.0);
  }
  
  @Test
  public void testNextLong() throws IOException {
    assertEquals(0L, reader.nextLong());
  }
  
  @Test
  public void testNextInt() throws IOException {
    assertEquals(0, reader.nextInt());
  }
  
  @Test
  public void testClose() throws IOException {
    reader.close();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }
  
  @Test
  public void testSkipValue() throws IOException {
    reader.skipValue();
    assertNull(reader.nextName());
  }
  
  @Test
  public void testPromoteNameToValue() throws IOException {
    reader.promoteNameToValue();
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("null", reader.nextString());
  }
  
  @Test
  public void testGetPath() {
    assertEquals("$", reader.getPath());
  }
  
}