package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testHasNext() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1 }"));
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NUMBER, reader.peek());
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseAndRead() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.close();
    reader.peek();
  }

  @Test
  public void testPeekAndEndArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testPeekAndEndObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1 }"));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndBeginArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndBeginObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1 }"));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndJsonArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.nextInt();
    reader.nextInt();
    reader.nextInt();
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndJsonObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1, \"bar\": 2 }"));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.nextName();
    assertEquals(JsonToken.NAME, reader.peek());
    reader.nextInt();
    reader.nextName();
    assertEquals(JsonToken.NAME, reader.peek());
    reader.nextInt();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndNull() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("null"));
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.nextNull();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndString() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": \"bar\" }"));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.nextName();
    assertEquals(JsonToken.NAME, reader.peek());
    reader.nextString();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndBoolean() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": true }"));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.nextName();
    assertEquals(JsonToken.NAME, reader.peek());
    reader.nextBoolean();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndNumber() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1 }"));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.nextName();
    assertEquals(JsonToken.NAME, reader.peek());
    reader.nextInt();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndEmptyArray() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testPeekAndEmptyObject() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testNextName() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1, \"bar\": 2 }"));
    reader.beginObject();
    reader.nextName();
    assertEquals("foo", reader.nextString());
    reader.nextName();
    assertEquals("bar", reader.nextString());
    reader.endObject();
    assertNull(reader.nextName());
  }

  @Test
  public void testNextString() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": \"bar\" }"));
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    assertEquals("bar", reader.nextString());
    reader.endObject();
    assertNull(reader.nextString());
  }

  @Test
  public void testNextBoolean() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": true }"));
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    assertEquals(true, reader.nextBoolean());
    reader.endObject();
    assertNull(reader.nextBoolean());
  }

  @Test
  public void testNextInt() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1 }"));
    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    assertEquals(1, reader.nextInt());
    reader.endObject();
    assertNull(reader.nextInt());
  }

  @Test
  public void testNextLong() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1 }"));
    reader.beginObject();
    reader.nextName();
    reader.nextLong();
    assertEquals(1L, reader.nextLong());
    reader.endObject();
    assertNull(reader.nextLong());
  }

  @Test
  public void testNextDouble() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": 1.5 }"));
    reader.beginObject();
    reader.nextName();
    reader.nextDouble();
    assertEquals(1.5, reader.nextDouble(), 0.001);
    reader.endObject();
    assertNull(reader.nextDouble());
  }

  @Test
  public void testNextNull() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("null"));
    reader.beginObject();
    reader.nextNull();
    reader.endObject();
    assertNull(reader.nextNull());
  }

  @Test
  public void testNextNullAfterName() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": null }"));
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    assertNull(reader.nextNull());
  }

  @Test
  public void testSkipValue() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": null }"));
    reader.beginObject();
    reader.nextName();
    reader.skipValue();
    reader.nextNull();
    reader.endObject();
  }

  @Test
  public void testPromoteNameToValue() throws Exception {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"foo\": null }"));
    reader.beginObject();
    reader.promoteNameToValue();
    reader.nextNull();
    reader.endObject();
    assertEquals(JsonToken.NULL, reader.peek());
  }
}