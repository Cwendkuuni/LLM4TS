import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonTreeReader reader;

  @Before
  public void setUp() {
    JsonElement element = new JsonPrimitive("test");
    reader = new JsonTreeReader(element);
  }

  @After
  public void tearDown() {
    reader = null;
  }

  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void testEndArray() throws IOException {
    reader.beginArray();
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
    reader.beginObject();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    assertTrue(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    assertEquals(JsonToken.STRING, reader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    assertEquals("test", reader.nextName());
  }

  @Test
  public void testNextString() throws IOException {
    assertEquals("test", reader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    // Testing for true value
    JsonElement element = new JsonPrimitive(true);
    reader = new JsonTreeReader(element);
    assertTrue(reader.nextBoolean());

    // Testing for false value
    element = new JsonPrimitive(false);
    reader = new JsonTreeReader(element);
    assertFalse(reader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    reader.nextNull();
    assertEquals(JsonToken.NULL, reader.peek());
  }

  @Test
  public void testNextDouble() throws IOException {
    // Testing for positive double value
    JsonElement element = new JsonPrimitive(3.14);
    reader = new JsonTreeReader(element);
    assertEquals(3.14, reader.nextDouble(), 0.0);

    // Testing for negative double value
    element = new JsonPrimitive(-3.14);
    reader = new JsonTreeReader(element);
    assertEquals(-3.14, reader.nextDouble(), 0.0);

    // Testing for NaN
    element = new JsonPrimitive(Double.NaN);
    reader = new JsonTreeReader(element);
    assertTrue(Double.isNaN(reader.nextDouble()));

    // Testing for positive infinity
    element = new JsonPrimitive(Double.POSITIVE_INFINITY);
    reader = new JsonTreeReader(element);
    assertEquals(Double.POSITIVE_INFINITY, reader.nextDouble(), 0.0);

    // Testing for negative infinity
    element = new JsonPrimitive(Double.NEGATIVE_INFINITY);
    reader = new JsonTreeReader(element);
    assertEquals(Double.NEGATIVE_INFINITY, reader.nextDouble(), 0.0);
  }

  @Test
  public void testNextLong() throws IOException {
    // Testing for positive long value
    JsonElement element = new JsonPrimitive(100L);
    reader = new JsonTreeReader(element);
    assertEquals(100L, reader.nextLong());

    // Testing for negative long value
    element = new JsonPrimitive(-100L);
    reader = new JsonTreeReader(element);
    assertEquals(-100L, reader.nextLong());
  }

  @Test
  public void testNextInt() throws IOException {
    // Testing for positive int value
    JsonElement element = new JsonPrimitive(10);
    reader = new JsonTreeReader(element);
    assertEquals(10, reader.nextInt());

    // Testing for negative int value
    element = new JsonPrimitive(-10);
    reader = new JsonTreeReader(element);
    assertEquals(-10, reader.nextInt());
  }

  @Test
  public void testClose() throws IOException {
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testSkipValue() throws IOException {
    reader.skipValue();
    assertEquals(JsonToken.NULL, reader.peek());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    reader.beginObject();
    reader.promoteNameToValue();
    assertEquals(JsonToken.STRING, reader.peek());
  }

  @Test
  public void testGetPath() {
    assertEquals("$", reader.getPath());
  }

}