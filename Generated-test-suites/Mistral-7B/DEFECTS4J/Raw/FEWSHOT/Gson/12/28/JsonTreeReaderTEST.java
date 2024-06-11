package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonReader;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmpty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSingleObject() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals("name1", reader.nextName());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("value1", reader.nextString());

    assertFalse(reader.hasNext());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSingleArray() throws IOException {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("value1", reader.nextString());

    assertFalse(reader.hasNext());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testMultiLevelObjectArray() throws IOException {
    String json = "{\"name1\":{\"name2\":[1, 2, 3], \"name3\":[\"foo\", \"bar\"]}}";
    JsonElement element = new JsonParser().parse(json);
    JsonTreeReader reader = new JsonTreeReader(element);

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals("name1", reader.nextName());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals("name2", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(1, reader.nextInt());

    assertTrue(reader.hasNext());
    assertEquals(2, reader.nextInt());

    assertTrue(reader.hasNext());
    assertEquals(3, reader.nextInt());

    assertFalse(reader.hasNext());
    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals("name3", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals("foo", reader.nextString());

    assertTrue(reader.hasNext());
    assertEquals("bar", reader.nextString());

    assertFalse(reader.hasNext());
    reader.endArray();

    assertFalse(reader.hasNext());
    reader.endObject();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testMultiLevelObjectArrayWithNull() throws IOException {
    String json = "{\"name1\":{\"name2\":[1, null, 3], \"name3\":[\"foo\", null, \"bar\"]}}";
    JsonElement element = new JsonParser().parse(json);
    JsonTreeReader reader = new JsonTreeReader(element);

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals("name1", reader.nextName());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals("name2", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(1, reader.nextInt());

    assertNull(reader.nextInt());

    assertTrue(reader.hasNext());
    assertEquals(3, reader.nextInt());

    assertFalse(reader.hasNext());
    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals("name3", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals("foo", reader.nextString());

    assertNull(reader.nextString());

    assertTrue(reader.hasNext());
    assertEquals("bar", reader.nextString());

    assertFalse(reader.hasNext());
    reader.endArray();

    assertFalse(reader.hasNext());
    reader.endObject();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    String json = "{\"name1\":null}";
    JsonElement element = new JsonParser().parse(json);
    JsonTreeReader reader = new JsonTreeReader(element);

    reader.skipValue();
    reader.close();
  }

  @Test(expected = AssertionError.class)
  public void testClosed() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
    reader.peek();
  }
}