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

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

  private JsonTreeReader jsonTreeReader;

  @Before
  public void setUp() {
    jsonTreeReader = new JsonTreeReader(new JsonObject());
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
    assertEquals(false, jsonTreeReader.hasNext());
  }

  @Test
  public void testNextName() throws IOException {
    String name = jsonTreeReader.nextName();
    assertNotNull(name);
  }

  @Test
  public void testNextString() throws IOException {
    String str = jsonTreeReader.nextString();
    assertNotNull(str);
  }

  @Test
  public void testNextBoolean() throws IOException {
    boolean bool = jsonTreeReader.nextBoolean();
    assertNotNull(bool);
  }

  @Test
  public void testNextNull() throws IOException {
    jsonTreeReader.nextNull();
    assertEquals(JsonToken.NULL, jsonTreeReader.peek());
  }

  @Test
  public void testNextDouble() throws IOException {
    double dbl = jsonTreeReader.nextDouble();
    assertNotNull(dbl);
  }

  @Test
  public void testNextLong() throws IOException {
    long lng = jsonTreeReader.nextLong();
    assertNotNull(lng);
  }

  @Test
  public void testNextInt() throws IOException {
    int num = jsonTreeReader.nextInt();
    assertNotNull(num);
  }

  @Test
  public void testClose() throws IOException {
    jsonTreeReader.close();
    assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
  }

  @Test
  public void testSkipValue() throws IOException {
    jsonTreeReader.skipValue();
    assertNotNull(jsonTreeReader.getPath());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    jsonTreeReader.promoteNameToValue();
    assertNotNull(jsonTreeReader.getPath());
  }
}