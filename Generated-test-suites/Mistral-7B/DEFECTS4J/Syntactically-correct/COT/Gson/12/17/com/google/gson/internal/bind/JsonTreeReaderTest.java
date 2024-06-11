import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderConstruction() {
    JsonElement jsonElement = new JsonPrimitive("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertNotNull(reader);
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeReaderWithNullElement() {
    JsonTreeReader reader = new JsonTreeReader(null);
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithoutEndArray() throws IOException {
    JsonElement jsonElement = new JsonPrimitive("[]");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
  }

  @Test
  public void testEndArray() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutEndObject() throws IOException {
    JsonElement jsonElement = new JsonPrimitive("{}");
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
  }

  @Test
  public void testEndObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    reader.next();
    assertTrue(reader.hasNext());
    reader.next();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.nextString();
    assertEquals(JsonToken.STRING, reader.peek());
    reader.nextBoolean();
    assertEquals(JsonToken.BOOLEAN, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekWithoutOpeningReader() {
    JsonTreeReader reader = new JsonTreeReader(null);
    reader.peek();
  }

  @Test
  public void testNextName() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    assertEquals("null", reader.nextName());
    reader.nextName();
    assertEquals("someKey", reader.nextName());
    reader.nextName();
    assertNull(reader.nextName());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNameWithoutOpeningReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(null);
    reader.nextName();
  }

  @Test
  public void testNextString() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName("someKey");
    reader.nextString();
    assertEquals("someValue", reader.nextString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextStringWithoutOpeningReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(null);
    reader.nextString();
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName("someKey");
    reader.nextBoolean();
    assertEquals(true, reader.nextBoolean());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBooleanWithoutOpeningReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(null);
    reader.nextBoolean();
  }

  @Test
  public void testNextNull() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName("someKey");
    reader.nextNull();
    assertNull(reader.nextNull());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNullWithoutOpeningReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(null);
    reader.nextNull();
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName("someKey");
    reader.nextDouble();
    assertEquals(1.23, reader.nextDouble(), 0.001);
  }

  @Test(expected = IllegalStateException.class)
  public void testNextDoubleWithoutOpeningReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(null);
    reader.nextDouble();
  }

  @Test
  public void testNextLong() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName("someKey");
    reader.nextLong();
    assertEquals(123, reader.nextLong());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLongWithoutOpeningReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(null);
    reader.nextLong();
  }

  @Test
  public void testNextInt() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName("someKey");
    reader.nextInt();
    assertEquals(123, reader.nextInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextIntWithoutOpeningReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(null);
    reader.nextInt();
  }

  @Test
  public void testClose() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseTwice() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.close();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName("someKey");
    reader.nextString();
    reader.nextName("someOtherKey");
    reader.nextNull();
    reader.skipValue();
    assertEquals("null", reader.nextName());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName("someKey");
    reader.promoteNameToValue();
    reader.nextName("someOtherKey");
    assertEquals("someKey", reader.nextString());
  }
}