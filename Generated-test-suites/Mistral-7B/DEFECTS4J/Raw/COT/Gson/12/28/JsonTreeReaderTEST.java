import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonTreeReader reader;
  private JsonElement jsonElement;

  @Before
  public void setup() {
    jsonElement = createJsonElement();
    reader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testConstructor() throws IOException {
    assertNotNull(reader);
    assertEquals(jsonElement, reader.peek());
  }

  @Test(expected = AssertionError.class)
  public void testEmptyReader() throws IOException {
    new JsonTreeReader(JsonNull.INSTANCE);
  }

  @Test
  public void testBeginArray() throws IOException {
    beginArrayTest(reader, "[]");
  }

  @Test
  public void testEndArray() throws IOException {
    beginArrayTest(reader, "[1,2,3]");
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObject() throws IOException {
    beginObjectTest(reader, "{}");
  }

  @Test
  public void testEndObject() throws IOException {
    beginObjectTest(reader, "{\"key\":\"value\"}");
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNext() throws IOException {
    beginArrayTest(reader, "[1,2,3]");
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    reader.beginArray();
    reader.endArray();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    beginArrayTest(reader, "[]");
    assertEquals(JsonToken.END_ARRAY, reader.peek());

    beginArrayTest(reader, "[1,2,3]");
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    beginObjectTest(reader, "{}");
    assertEquals(JsonToken.END_OBJECT, reader.peek());

    beginObjectTest(reader, "{\"key\":\"value\"}");
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

    beginArrayTest(reader, "[1,[2,[3]],4]]");
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

    beginObjectTest(reader, "{\"key\":[\"value1\",{\"key2\":\"value2\"}]}");
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

    JsonTreeReader readerWithNull = new JsonTreeReader(JsonNull.INSTANCE);
    assertEquals(JsonToken.NULL, readerWithNull.peek());
  }

  @Test
  public void testNextName() throws IOException {
    beginObjectTest(reader, "{\"key1\":1,\"key2\":2,\"key3\":3}");
    assertEquals("key1", reader.nextName());
    assertEquals("key2", reader.nextName());
    assertEquals("key3", reader.nextName());
    assertNull(reader.nextName());
  }

  @Test
  public void testNextString() throws IOException {
    beginArrayTest(reader, "[\"abc\",\"def\"]");
    assertEquals("abc", reader.nextString());
    assertEquals("def", reader.nextString());
    assertNull(reader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    beginObjectTest(reader, "{\"key\":true}");
    assertTrue(reader.nextBoolean());
    assertFalse(reader.nextBoolean());
    assertFalse(reader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    reader = new JsonTreeReader(JsonNull.INSTANCE);
    assertNull(reader.nextNull());
  }

  @Test
  public void testNextDouble() throws IOException {
    beginArrayTest(reader, "[3.14, 2.718]");
    assertEquals(3.14, reader.nextDouble(), 0.001);
    assertEquals(2.718, reader.nextDouble(), 0.001);
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextLong() throws IOException {
    beginArrayTest(reader, "[1, 2, 3]");
    assertEquals(1, reader.nextLong());
    assertEquals(2, reader.nextLong());
    assertEquals(3, reader.nextLong());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextInt() throws IOException {
    beginArrayTest(reader, "[1, 2, 3]");
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testClose() throws IOException {
    beginArrayTest(reader, "[1,2,3]");
    reader.close();
    assertSame(SENTINEL_CLOSED, reader.peekStack());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testSkipValue() throws IOException {
    beginObjectTest(reader, "{\"key\":\"value\"}");
    reader.skipValue();
    reader.promoteNameToValue();
    assertEquals("value", reader.nextString());
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    beginObjectTest(reader, "{\"key\":\"value\"}");
    reader.promoteNameToValue();
    assertEquals("value", reader.nextString());
  }

  @Test
  public void testToString() throws IOException {
    assertEquals("JsonTreeReader", reader.toString());
  }

  private void beginArrayTest(JsonTreeReader reader, String jsonString) throws IOException {
    reader.beginArray();
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals(JsonToken.BEGIN_ARRAY, jsonReader.peek());
    reader.beginArray();
    JsonTreeReader newReader = new JsonTreeReader(reader.peek());
    assertEquals(JsonToken.BEGIN_ARRAY, newReader.peek());
    assertEquals(reader.getPath(), newReader.getPath());
    jsonReader.beginArray();
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals(JsonToken.BEGIN_ARRAY, jsonReader.peek());
    JsonToken jsonToken = jsonReader.peek();
    while (jsonToken != JsonToken.END_ARRAY) {
      JsonElement jsonElement = jsonReader.nextElement();
      reader.nextElement();
      assertEquals(jsonElement, reader.peek());
      jsonToken = jsonReader.peek();
    }
    reader.endArray();
    jsonReader.endArray();
  }

  private void beginObjectTest(JsonTreeReader reader, String jsonString) throws IOException {
    reader.beginObject();
    JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    assertEquals(JsonToken.BEGIN_OBJECT, jsonReader.peek());
    reader.beginObject();
    JsonTreeReader newReader = new JsonTreeReader(reader.peek());
    assertEquals(JsonToken.BEGIN_OBJECT, newReader.peek());
    assertEquals(reader.getPath(), newReader.getPath());
    jsonReader.beginObject();
    JsonElement jsonObject = jsonReader.peek();
    reader.beginObject();
    JsonObject readerJsonObject = (JsonObject) reader.peekStack();
    assertEquals(jsonObject.entrySet().size(), readerJsonObject.entrySet().size());
    List<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet().stream()
        .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
        .collect(Collectors.toList());
    List<Map.Entry<String, JsonElement>> readerEntrySet = readerJsonObject.entrySet().stream()
        .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
        .collect(Collectors.toList());
    for (int i = 0; i < entrySet.size(); i++) {
      Map.Entry<String, JsonElement> entry = entrySet.get(i);
      Map.Entry<String, JsonElement> readerEntry = readerEntrySet.get(i);
      assertEquals(entry.getKey(), readerEntry.getKey());
      assertEquals(entry.getValue(), readerEntry.getValue());
    }
    jsonReader.endObject();
    reader.endObject();
  }

  private JsonElement createJsonElement() {
    return new JsonObject() {{
      add("array", new JsonArray() {{
        add(new JsonPrimitive(1));
        add(new JsonPrimitive(2));
        add(new JsonPrimitive(3));
      }});
      add("object", new JsonObject() {{
        addProperty("key1", new JsonPrimitive(1));
        addProperty("key2", new JsonPrimitive(2));
        addProperty("key3", new JsonPrimitive(3));
      }});
    }};
  }
}