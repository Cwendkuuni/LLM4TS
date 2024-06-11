package com.google.gson.internal.bind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonToken;

import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testNullJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testBooleanJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"bool\": true }"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("bool", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BOOLEAN, reader.peek());
    assertSame(reader.nextBoolean(), Boolean.TRUE);
    reader.close();
  }

  @Test
  public void testStringJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"str\": \"test\" }"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("str", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("test", reader.nextString());
    reader.close();
  }

  @Test
  public void testNumberJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"num\": 1 }"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("num", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());
    reader.close();
  }

  @Test
  public void testArrayJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[ 1, \"test\", false ]"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("test", reader.nextString());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BOOLEAN, reader.peek());
    assertFalse(reader.nextBoolean());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.close();
  }

  @Test
  public void testObjectJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"arr\": [ 1, \"test\", false ], \"str\": \"test\", \"bool\": true }"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("arr", reader.nextName());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("test", reader.nextString());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BOOLEAN, reader.peek());
    assertFalse(reader.nextBoolean());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("str", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("test", reader.nextString());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("bool", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BOOLEAN, reader.peek());
    assertSame(reader.nextBoolean(), Boolean.TRUE);

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    reader.close();
  }

  @Test
  public void testNestedJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"arr\": [ { \"num\": 1 }, \"test\": { \"str\": \"test\" } ] }"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("arr", reader.nextName());
    reader.beginArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("num", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NUMBER, reader.peek());
    assertEquals(1, reader.nextInt());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("test", reader.nextName());
    reader.beginObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek());
    assertEquals("str", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek());
    assertEquals("test", reader.nextString());

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();

    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    reader.close();
  }
}