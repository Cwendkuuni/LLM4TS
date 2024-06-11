import static org.junit.Assert.*;
import org.junit.Before;
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
import com.google.gson.internal.bind.JsonTreeReader;

public class JsonTreeReaderTest {

  private JsonTreeReader jsonTreeReader;

  @Before
  public void setup() {
    JsonElement jsonElement = new JsonObject();
    jsonTreeReader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeReader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    boolean hasNext = jsonTreeReader.hasNext();
    assertFalse(hasNext);
  }

  @Test
  public void testPeek() throws IOException {
    JsonToken token = jsonTreeReader.peek();
    assertEquals(JsonToken.END_DOCUMENT, token);
  }

  @Test
  public void testNextName() throws IOException {
    String name = jsonTreeReader.nextName();
    assertNull(name);
  }

  @Test
  public void testNextString() throws IOException {
    String value = jsonTreeReader.nextString();
    assertNull(value);
  }

  @Test
  public void testNextBoolean() throws IOException {
    boolean value = jsonTreeReader.nextBoolean();
    assertFalse(value);
  }

  @Test
  public void testNextNull() throws IOException {
    jsonTreeReader.nextNull();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
  }

  @Test
  public void testNextDouble() throws IOException {
    double value = jsonTreeReader.nextDouble();
    assertEquals(0.0, value, 0.01);
  }

  @Test
  public void testNextLong() throws IOException {
    long value = jsonTreeReader.nextLong();
    assertEquals(0L, value);
  }

  @Test
  public void testNextInt() throws IOException {
    int value = jsonTreeReader.nextInt();
    assertEquals(0, value);
  }

  @Test
  public void testClose() throws IOException {
    jsonTreeReader.close();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }

  @Test
  public void testSkipValue() throws IOException {
    jsonTreeReader.skipValue();
    assertNull(jsonTreeReader.getPath());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    jsonTreeReader.promoteNameToValue();
    assertEquals(JsonToken.NAME, jsonTreeReader.peek());
  }

  @Test
  public void testGetPath() throws IOException {
    String path = jsonTreeReader.getPath();
    assertEquals("$", path);
  }
}