import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class JsonTreeReaderTest {

  private JsonTreeReader jsonTreeReader;

  @Before
  public void setUp() {
    JsonElement element = new JsonObject();
    jsonTreeReader = new JsonTreeReader(element);
  }

  @Test
  public void testBeginArray() throws Exception {
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testEndArray() throws Exception {
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
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testHasNext() throws Exception {
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testPeek() throws Exception {
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }

  @Test
  public void testNextName() throws Exception {
    assertNull(jsonTreeReader.nextName());
  }

  @Test
  public void testNextString() throws Exception {
    assertNull(jsonTreeReader.nextString());
  }

  @Test
  public void testNextBoolean() throws Exception {
    assertFalse(jsonTreeReader.nextBoolean());
  }

  @Test
  public void testNextNull() throws Exception {
    jsonTreeReader.nextNull();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
  }

  @Test
  public void testNextDouble() throws Exception {
    assertEquals(0.0, jsonTreeReader.nextDouble(), 0);
  }

  @Test
  public void testNextLong() throws Exception {
    assertEquals(0, jsonTreeReader.nextLong());
  }

  @Test
  public void testNextInt() throws Exception {
    assertEquals(0, jsonTreeReader.nextInt());
  }

  @Test
  public void testClose() throws Exception {
    jsonTreeReader.close();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }

  @Test
  public void testSkipValue() throws Exception {
    jsonTreeReader.skipValue();
    assertTrue(jsonTreeReader.hasNext());
  }

  @Test
  public void testPromoteNameToValue() throws Exception {
    jsonTreeReader.promoteNameToValue();
    assertEquals(JsonToken.STRING, jsonTreeReader.peek());
  }

  @Test
  public void testGetPath() throws Exception {
    assertEquals("$", jsonTreeReader.getPath());
  }
}