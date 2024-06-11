import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderConstructor() {
    JsonElement element = new JsonElement();
    JsonTreeReader reader = new JsonTreeReader(element);
    assertNotNull(reader);
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonElement arrayElement = new JsonElement().getAsJsonArray();
    JsonTreeReader reader = new JsonTreeReader(arrayElement);
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithoutEndArray() throws IOException {
    JsonElement arrayElement = new JsonElement().getAsJsonArray();
    JsonTreeReader reader = new JsonTreeReader(arrayElement);
    reader.beginArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonElement objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutEndObject() throws IOException {
    JsonElement objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.beginObject();
  }

  @Test
  public void testHasNext() throws IOException {
    JsonElement jsonElement = new JsonElement();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.push(jsonElement);
    assertFalse(reader.hasNext());

    JsonArray arrayElement = new JsonElement().getAsJsonArray();
    reader = new JsonTreeReader(arrayElement);
    reader.push(arrayElement.iterator());
    assertTrue(reader.hasNext());

    JsonObject objectElement = new JsonElement().getAsJsonObject();
    reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    assertTrue(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonElement jsonElement = new JsonElement();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.push(jsonElement);
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());

    JsonArray arrayElement = new JsonElement().getAsJsonArray();
    reader = new JsonTreeReader(arrayElement);
    reader.push(arrayElement.iterator());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.next();
    assertEquals(JsonToken.END_ARRAY, reader.peek());

    JsonObject objectElement = new JsonElement().getAsJsonObject();
    reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

    reader.nextName();
    reader.nextValue();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekAfterEndDocument() throws IOException {
    JsonElement jsonElement = new JsonElement();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.push(jsonElement);
    reader.peek();
  }

  @Test
  public void testNextName() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    assertEquals(JsonToken.NAME, reader.peek());
    String name = reader.nextName();
    assertNotNull(name);

    reader.nextValue();
    reader.nextName();
    assertEquals(JsonToken.NAME, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNameAfterEndObject() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextValue();
    reader.nextName();
  }

  @Test
  public void testNextString() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextString();
    assertEquals(JsonToken.STRING, reader.peek());
    String stringValue = reader.nextString();
    assertNotNull(stringValue);

    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextString();
    assertEquals(JsonToken.STRING, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextStringAfterEndObject() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextString();
    reader.nextName();
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextBoolean();
    assertEquals(JsonToken.BOOLEAN, reader.peek());

    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextBoolean();
    assertEquals(JsonToken.BOOLEAN, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBooleanAfterEndObject() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextBoolean();
    reader.nextName();
  }

  @Test
  public void testNextNull() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextNull();
    assertEquals(JsonToken.NULL, reader.peek());

    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextNull();
    assertEquals(JsonToken.NULL, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNullAfterEndObject() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextNull();
    reader.nextName();
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextDouble();
    assertEquals(JsonToken.NUMBER, reader.peek());

    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextDouble();
    assertEquals(JsonToken.NUMBER, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextDoubleAfterEndObject() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextDouble();
    reader.nextName();
    reader.nextDouble();
  }

  @Test
  public void testNextLong() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextLong();
    assertEquals(JsonToken.NUMBER, reader.peek());

    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextLong();
    assertEquals(JsonToken.NUMBER, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLongAfterEndObject() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextLong();
    reader.nextName();
    reader.nextLong();
  }

  @Test
  public void testNextInt() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextInt();
    assertEquals(JsonToken.NUMBER, reader.peek());

    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextInt();
    assertEquals(JsonToken.NUMBER, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextIntAfterEndObject() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextInt();
    reader.nextName();
    reader.nextInt();
  }

  @Test
  public void testClose() throws IOException {
    JsonElement jsonElement = new JsonElement();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());

    JsonArray arrayElement = new JsonElement().getAsJsonArray();
    reader = new JsonTreeReader(arrayElement);
    reader.push(arrayElement.iterator());
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());

    JsonObject objectElement = new JsonElement().getAsJsonObject();
    reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.close();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseTwice() throws IOException {
    JsonElement jsonElement = new JsonElement();
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.close();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.skipValue();
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.nextValue();
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject objectElement = new JsonElement().getAsJsonObject();
    JsonTreeReader reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.nextName();
    reader.promoteNameToValue();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

    reader.nextName();
    reader.nextValue();
    reader.nextName();
    reader.promoteNameToValue();
    assertEquals(JsonToken.NAME, reader.peek());
  }

  @Test
  public void testPush() {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement());
    reader.push(new JsonPrimitive("test"));
    assertEquals(1, reader.stackSize);

    JsonArray arrayElement = new JsonElement().getAsJsonArray();
    reader = new JsonTreeReader(arrayElement);
    reader.push(arrayElement.iterator());
    reader.push(new JsonPrimitive("test"));
    assertEquals(3, reader.stackSize);

    JsonObject objectElement = new JsonElement().getAsJsonObject();
    reader = new JsonTreeReader(objectElement);
    reader.push(objectElement.entrySet().iterator());
    reader.push(new JsonPrimitive("test"));
    reader.push(new JsonPrimitive("test2"));
    assertEquals(5, reader.stackSize);
  }
}