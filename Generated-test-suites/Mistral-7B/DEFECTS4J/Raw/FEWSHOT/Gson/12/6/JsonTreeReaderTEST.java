package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonElement createJsonElement(String json) {
    JsonReader reader = new JsonReader(new StringReader(json));
    JsonElement element = new JsonTreeReader(element -> element).readTree(reader);
    reader.close();
    return element;
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonElement json = new JsonPrimitive("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek(1));
  }

  @Test
  public void testEndArray() throws IOException {
    JsonElement json = new JsonPrimitive("[1, 2, 3]");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginArray();
    reader.nextElement();
    reader.nextElement();
    reader.nextElement();
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    assertEquals(JsonToken.END_ARRAY, reader.peek(1));
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3]}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek(1));
  }

  @Test
  public void testEndObject() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3]}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    reader.nextName();
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    reader.nextInt();
    reader.endArray();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    assertEquals(JsonToken.END_OBJECT, reader.peek(1));
  }

  @Test
  public void testHasNext() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3]}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertTrue(reader.hasNext());
    reader.nextName();
    assertTrue(reader.hasNext());
    reader.nextInt();
    assertTrue(reader.hasNext());
    reader.nextName();
    assertTrue(reader.hasNext());
    reader.beginArray();
    assertTrue(reader.hasNext());
    reader.nextInt();
    assertTrue(reader.hasNext());
    reader.nextInt();
    assertTrue(reader.hasNext());
    reader.nextInt();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3]}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("a", reader.nextName());
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1.0, reader.nextDouble());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("b", reader.nextName());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3], \"c\": \"foo\"}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertEquals("a", reader.nextName());
    assertEquals("1", reader.nextInt());
    assertEquals("b", reader.nextName());
    assertEquals("1", reader.nextInt());
    reader.nextName();
    assertEquals("c", reader.nextName());
    assertEquals("foo", reader.nextString());
  }

  @Test
  public void testNextString() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": \"foo\", \"b\": [\"bar\", \"baz\"], \"c\": 1}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertEquals("foo", reader.nextString());
    reader.nextName();
    assertEquals("b", reader.nextName());
    assertEquals("bar", reader.nextString());
    reader.nextName();
    reader.beginArray();
    reader.nextInt();
    assertEquals("baz", reader.nextString());
    reader.endArray();
    reader.nextName();
    assertEquals("c", reader.nextName());
    assertEquals(1, reader.nextInt());
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": true, \"b\": false}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertEquals(true, reader.nextBoolean());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("b", reader.nextName());
    assertEquals(false, reader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": null, \"b\": null, \"c\": 1}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.nextName();
    reader.nextNull();
    reader.nextName();
    assertEquals(1, reader.nextInt());
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1.2, \"b\": [1.1, 2.2], \"c\": -3.4}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertEquals(1.2, reader.nextDouble(), 0.0);
    reader.nextName();
    reader.beginArray();
    assertEquals(1.1, reader.nextDouble(), 0.0);
    reader.nextDouble();
    assertEquals(-3.4, reader.nextDouble(), 0.0);
    reader.endArray();
    reader.nextName();
    assertEquals(1, reader.nextInt());
  }

  @Test
  public void testNextLong() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3], \"c\": 4}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertEquals(1, reader.nextInt());
    reader.nextName();
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.nextInt();
    reader.nextInt();
    reader.nextInt();
    assertEquals(4, reader.nextLong());
    reader.endArray();
    reader.nextName();
    assertEquals(1, reader.nextInt());
  }

  @Test
  public void testNextInt() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3], \"c\": 4}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    assertEquals(1, reader.nextInt());
    reader.nextName();
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.nextInt();
    assertEquals(4, reader.nextInt());
    reader.endArray();
    reader.nextName();
    assertEquals(1, reader.nextInt());
  }

  @Test
  public void testClose() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3]}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    reader.nextName();
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    reader.nextInt();
    reader.endArray();
    reader.endObject();
    reader.close();

    assertNull(reader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testReadAfterClose() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3]}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    reader.nextName();
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    reader.nextInt();
    reader.endArray();
    reader.endObject();
    reader.close();
    reader.nextName();
  }

  @Test
  public void testGetPath() throws IOException {
    JsonElement json = new JsonPrimitive("{\"a\": 1, \"b\": [1, 2, 3]}");
    JsonTreeReader reader = new JsonTreeReader(json);

    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    reader.nextName();
    reader.beginArray();
    reader.nextInt();
    assertEquals("[0]", reader.getPath());
    reader.nextInt();
    assertEquals("[1]", reader.getPath());
    reader.nextInt();
    assertEquals("[2]", reader.getPath());
    reader.endArray();
    reader.endObject();
    assertEquals("", reader.getPath());
  }
}