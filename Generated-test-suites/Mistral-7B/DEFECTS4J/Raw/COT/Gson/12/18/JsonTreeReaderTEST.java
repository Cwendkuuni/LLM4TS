import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

public class JsonTreeReaderTest {
  

  @Test
  public void testJsonTreeReaderConstructor() {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    assertNotNull(jsonTreeReader);
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeReaderConstructorWithNullJsonElement() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(null);
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonElement jsonElement = new JsonArray();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();

    JsonToken jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonToken);

    jsonTreeReader.endArray();
    jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.END_ARRAY, jsonToken);
  }

  @Test(expected = IOException.class)
  public void testBeginArrayWithoutEndArray() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();

    jsonTreeReader.nextInt(); // Consume the first element in the array
  }

  @Test
  public void testEndArray() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();

    JsonToken jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.END_ARRAY, jsonToken);
  }

  @Test(expected = IOException.class)
  public void testEndArrayWithoutBeginArray() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.endArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();

    JsonToken jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonToken);

    jsonTreeReader.endObject();
    jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.END_OBJECT, jsonToken);
  }

  @Test(expected = IOException.class)
  public void testBeginObjectWithoutEndObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();

    jsonTreeReader.nextName();
  }

  @Test
  public void testEndObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();

    JsonToken jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.END_OBJECT, jsonToken);
  }

  @Test(expected = IOException.class)
  public void testEndObjectWithoutBeginObject() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.endObject();
  }

  @Test
  public void testHasNext() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();

    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.nextInt();

    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.nextInt();

    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.endArray(); // Consume the second array

    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name", new JsonPrimitive("value"));
    jsonTreeReader.endObject();

    JsonToken jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, jsonToken);

    jsonTreeReader.nextName();
    jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.NAME, jsonToken);
    assertEquals("name", jsonTreeReader.nextName());

    jsonTreeReader.nextString();
    jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.STRING, jsonToken);
    assertEquals("value", jsonTreeReader.nextString());

    jsonTreeReader.endArray(); // Consume the second array
    jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.END_ARRAY, jsonToken);

    jsonTreeReader.endArray(); // Consume the first array
    jsonToken = jsonTreeReader.peek();
    assertEquals(JsonToken.END_ARRAY, jsonToken);

    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testExpect() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
    jsonTreeReader.beginArray();

    jsonTreeReader.expect(JsonToken.NAME);
    jsonTreeReader.nextName();

    jsonTreeReader.expect(JsonToken.BOOLEAN);
    jsonTreeReader.nextBoolean();

    jsonTreeReader.expect(JsonToken.NULL);
    jsonTreeReader.nextNull();

    jsonTreeReader.expect(JsonToken.NUMBER);
    jsonTreeReader.nextInt();

    jsonTreeReader.expect(JsonToken.STRING);
    jsonTreeReader.nextString();

    jsonTreeReader.expect(JsonToken.END_ARRAY);
    jsonTreeReader.endArray();

    jsonTreeReader.expect(JsonToken.END_OBJECT);
    jsonTreeReader.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testClose() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.close();
    jsonTreeReader.nextString();
  }

  @Test
  public void testNextName() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name1", new JsonPrimitive("value1"));
    jsonTreeReader.add("name2", new JsonPrimitive("value2"));

    assertEquals("name1", jsonTreeReader.nextName());
    assertEquals("value1", jsonTreeReader.nextString());

    assertEquals("name2", jsonTreeReader.nextName());
    assertEquals("value2", jsonTreeReader.nextString());

    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testNextString() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name", new JsonPrimitive("value"));
    jsonTreeReader.endObject();

    assertEquals("value", jsonTreeReader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name", new JsonPrimitive(true));
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();

    assertTrue(jsonTreeReader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name", JsonNull.INSTANCE);
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();

    assertNull(jsonTreeReader.nextString());
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name", new JsonPrimitive(3.14));
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();

    assertEquals(3.14, jsonTreeReader.nextDouble(), 0.01);
  }

  @Test
  public void testNextLong() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name", new JsonPrimitive(42));
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();

    assertEquals(42L, jsonTreeReader.nextLong());
  }

  @Test
  public void testNextInt() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name", new JsonPrimitive(5));
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();

    assertEquals(5, jsonTreeReader.nextInt());
  }

  @Test
  public void testCloseAndReopen() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name1", new JsonPrimitive("value1"));
    jsonTreeReader.add("name2", new JsonPrimitive("value2"));
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();

    jsonTreeReader.close();
    jsonTreeReader.open();

    assertEquals("name1", jsonTreeReader.nextName());
    assertEquals("value1", jsonTreeReader.nextString());

    assertEquals("name2", jsonTreeReader.nextName());
    assertEquals("value2", jsonTreeReader.nextString());

    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonElement jsonElement = new JsonObject();
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    jsonTreeReader.beginArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.add("name1", new JsonPrimitive("value1"));
    jsonTreeReader.add("name2", new JsonPrimitive("value2"));
    jsonTreeReader.endObject();
    jsonTreeReader.endArray();

    jsonTreeReader.promoteNameToValue();

    assertEquals("value1", jsonTreeReader.nextString());
    assertEquals("name1", jsonTreeReader.nextName());

    assertEquals("value2", jsonTreeReader.nextString());
    assertEquals("name2", jsonTreeReader.nextName());

    assertFalse(jsonTreeReader.hasNext());
  }

  
}