import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.After;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonTreeReader reader;
  private JsonElement jsonElement;

  @Test
  public void testJsonTreeReaderConstructor() {
    JsonObject jsonObject = new JsonObject();
    jsonElement = jsonObject;
    reader = new JsonTreeReader(jsonElement);
    assertEquals("JsonTreeReader", reader.toString());
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeReaderEmptyConstructor() {
    new JsonTreeReader();
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonArray jsonArray = mock(JsonArray.class);
    when(jsonArray.iterator()).thenReturn(mock(Iterator.class));
    reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithoutEndArray() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginArray();
    reader.nextInt();
  }

  @Test
  public void testEndArray() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginArray();
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutEndObject() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.nextInt();
  }

  @Test
  public void testEndObject() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.add("name", mock(JsonPrimitive.class));
    reader.add("array", mock(JsonArray.class));

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());

    reader.nextName();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.beginArray();
    reader.nextInt();

    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek());

    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.add("name", mock(JsonPrimitive.class));
    reader.add("array", mock(JsonArray.class));

    assertEquals(JsonToken.NAME, reader.peek());
    reader.nextName();

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertEquals(JsonToken.NUMBER, reader.peek());
    reader.nextInt();

    reader.endArray();

    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.nextName();

    assertNull(reader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", "value");

    assertEquals("name", reader.nextName());
    assertEquals("value", reader.nextString());
  }

  @Test
  public void testNextString() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", "value");

    assertEquals("value", reader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", true);

    assertTrue(reader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", null);

    assertNull(reader.nextInt());
  }

  @Test
  public void testNextDouble() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", 3.14);

    assertEquals(3.14, reader.nextDouble(), 0.0);
  }

  @Test
  public void testNextLong() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", 42L);

    assertEquals(42L, reader.nextLong());
  }

  @Test(expected = NumberFormatException.class)
  public void testNextIntWithNaN() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", Double.NaN);

    reader.nextInt();
  }

  @Test(expected = NumberFormatException.class)
  public void testNextIntWithInfinity() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", Double.POSITIVE_INFINITY);

    reader.nextInt();
  }

  @Test(expected = NumberFormatException.class)
  public void testNextIntWithNegativeInfinity() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", Double.NEGATIVE_INFINITY);

    reader.nextInt();
  }

  @Test
  public void testClose() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.close();
    assertNull(reader.peek());
  }

  @Test
  public void testSkipValue() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", "value");

    reader.nextName();
    reader.skipValue();

    assertEquals("null", reader.nextString());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    reader = new JsonTreeReader(mock(JsonElement.class));
    reader.beginObject();
    reader.addProperty("name", "value");

    reader.promoteNameToValue();

    assertEquals("value", reader.nextString());
  }

  @After
  public void cleanup() throws IOException {
    if (reader != null) {
      reader.close();
    }
  }
}