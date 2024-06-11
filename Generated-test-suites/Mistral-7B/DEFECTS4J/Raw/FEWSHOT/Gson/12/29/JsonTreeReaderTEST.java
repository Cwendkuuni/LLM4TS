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
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmpty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.close();
  }

  @Test
  public void testBeginArrayWithOneElement() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive("foo")));
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray(); // should not throw an exception
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals("foo", reader.nextString());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.close();
  }

  @Test
  public void testBeginArrayWithEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.close();
  }

  @Test
  public void testBeginArrayWithArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(new JsonArray().add(new JsonPrimitive("foo"))));
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals("foo", reader.nextString());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.close();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar")));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.close();
  }

  @Test
  public void testBeginObjectWithEmptyObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.close();
  }

  @Test
  public void testBeginObjectWithObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonObject().addProperty("bar", new JsonPrimitive("baz"))));
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    reader.beginObject();
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    assertEquals("baz", reader.nextString());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.close();
  }

  @Test
  public void testHasNext() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar")));
    assertTrue(reader.hasNext());
    reader.nextString();
    assertTrue(reader.hasNext());
    reader.close();
  }

  @Test
  public void testPeek() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray().add(new JsonPrimitive("foo"))).beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    reader.nextString();
    assertEquals(JsonToken.STRING, reader.peek());
    reader.close();
  }

  @Test
  public void testPopStack() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    Object pop = reader.popStack();
    assertSame(reader.peekStack(), reader.stack[reader.stackSize - 1]);
    assertEquals("bar", pop.toString());
    reader.close();
  }

  @Test
  public void testExpect() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    reader.expect(JsonToken.BEGIN_ARRAY);
    reader.expect(JsonToken.STRING);
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosed() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    reader.close();
    reader.nextString();
  }

  @Test
  public void testNextName() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    assertEquals("foo", reader.nextName());
    reader.close();
  }

  @Test
  public void testNextString() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    assertEquals("bar", reader.nextString());
    reader.close();
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive(true))).beginObject();
    assertTrue(reader.nextBoolean());
    reader.close();
  }

  @Test
  public void testNextNull() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", JsonNull.INSTANCE)).beginObject();
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive(3.14))).beginObject();
    assertEquals(3.14, reader.nextDouble(), 0.001);
    reader.close();
  }

  @Test
  public void testNextLong() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive(123))).beginObject();
    assertEquals(123, reader.nextLong());
    reader.close();
  }

  @Test
  public void testNextInt() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive(45))).beginObject();
    assertEquals(45, reader.nextInt());
    reader.close();
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    reader.close();
    reader.nextString(); // should not throw an exception
    reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    reader.skipValue();
    assertEquals(JsonToken.NAME, reader.peek());
    reader.nextName();
    reader.close();
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    reader.promoteNameToValue();
    assertEquals("bar", reader.nextString());
    reader.close();
  }

  @Test
  public void testGetPath() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))).beginObject();
    assertEquals("$", reader.getPath());
    reader.nextName();
    assertEquals("foo", reader.getPath());
    reader.close();
  }
}