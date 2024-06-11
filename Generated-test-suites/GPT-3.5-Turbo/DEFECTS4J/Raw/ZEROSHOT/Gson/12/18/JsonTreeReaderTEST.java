import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeReaderTest {

  private JsonTreeReader jsonTreeReader;

  @Before
  public void setUp() {
    JsonElement element = jsonObj(); // create an example json element here
    jsonTreeReader = new JsonTreeReader(element);
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeReader.beginArray();
    // verify beginArray was called correctly
    assertTrue(jsonTreeReader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.beginArray();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    // verify endArray was called correctly
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.endArray();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeReader.beginObject();
    // verify beginObject was called correctly
    assertTrue(jsonTreeReader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.beginObject();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    // verify endObject was called correctly
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.endObject();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testHasNext() throws IOException {
    // verify hasNext returns correct value
    assertTrue(jsonTreeReader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonToken token = jsonTreeReader.peek();
    // verify peek returns correct token
    assertEquals(JsonToken.BEGIN_OBJECT, token);
  }

  @Test
  public void testNextName() throws IOException {
    String name = jsonTreeReader.nextName();
    // verify nextName returns correct name
    assertEquals("name", name);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNameWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.nextName();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testNextString() throws IOException {
    String value = jsonTreeReader.nextString();
    // verify nextString returns correct value
    assertEquals("value", value);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextStringWhenNotExpected() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.nextString();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testNextBoolean() throws IOException {
    boolean value = jsonTreeReader.nextBoolean();
    // verify nextBoolean returns correct value
    assertTrue(value);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBooleanWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.nextBoolean();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testNextNull() throws IOException {
    jsonTreeReader.nextNull();
    // verify nextNull was called correctly
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNullWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.nextNull();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testNextDouble() throws IOException {
    double value = jsonTreeReader.nextDouble();
    // verify nextDouble returns correct value
    assertEquals(1.23, value, 0.01);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextDoubleWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.nextDouble();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testNextLong() throws IOException {
    long value = jsonTreeReader.nextLong();
    // verify nextLong returns correct value
    assertEquals(123, value);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLongWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.nextLong();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testNextInt() throws IOException {
    int value = jsonTreeReader.nextInt();
    // verify nextInt returns correct value
    assertEquals(123, value);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextIntWhenNotExpected() throws IOException {
    jsonTreeReader.nextString();
    jsonTreeReader.nextInt();
    // expecting an IllegalStateException to be thrown
  }

  @Test
  public void testClose() throws IOException {
    jsonTreeReader.close();
    // verify close was called correctly
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testSkipValue() throws IOException {
    jsonTreeReader.skipValue();
    // verify skipValue was called correctly
  }

  @Test
  public void testToString() {
    String className = jsonTreeReader.toString();
    // verify toString returns correct class name
    assertEquals("JsonTreeReader", className);
  }

  @Test
  public void testGetPath() throws IOException {
    String path = jsonTreeReader.getPath();
    // verify getPath returns correct path
    assertEquals("$", path);
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    jsonTreeReader.promoteNameToValue();
    // verify promoteNameToValue was called correctly
    assertEquals("value", jsonTreeReader.nextString());
  }

  private JsonElement jsonObj() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "value");
    return jsonObject;
  }
}