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

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(new JsonPrimitive(1)).endObject();
    assertEquals(new JsonPrimitive(1), writer.get());
  }

  @Test
  public void testGet_empty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertEquals(JsonNull.INSTANCE, writer.get());
  }

  @Test
  public void testPeek() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(new JsonPrimitive(1));
    writer.value(new JsonPrimitive(2));
    writer.endArray();

    JsonElement expected = new JsonPrimitive(2);
    JsonElement actual = writer.peek();

    assertEquals(expected, actual);
  }

  @Test
  public void testPeek_empty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement actual = writer.peek();
    assertNull(actual);
  }

  @Test
  public void testPut() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.put(new JsonPrimitive(1));
    JsonElement actual = writer.peek();
    assertEquals(new JsonPrimitive(1), actual);
  }

  @Test
  public void testPutObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").endObject();
    JsonObject actual = (JsonObject) writer.peek();
    assertTrue(actual.entrySet().isEmpty());
  }

  @Test
  public void testPutArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(new JsonPrimitive(1));
    writer.value(new JsonPrimitive(2));
    writer.endArray();
    JsonArray actual = (JsonArray) writer.peek();
    List<JsonElement> elements = actual.asList();
    assertEquals(2, elements.size());
    assertEquals(new JsonPrimitive(1), elements.get(0));
    assertEquals(new JsonPrimitive(2), elements.get(1));
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(new JsonPrimitive(1));
    writer.value(new JsonPrimitive(2));
    writer.endArray();
    JsonArray actual = (JsonArray) writer.peek();
    List<JsonElement> elements = actual.asList();
    assertEquals(2, elements.size());
    assertEquals(new JsonPrimitive(1), elements.get(0));
    assertEquals(new JsonPrimitive(2), elements.get(1));
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(new JsonPrimitive(1)).endObject();
    JsonObject actual = (JsonObject) writer.peek();
    assertTrue(actual.entrySet().containsKey("foo"));
    assertEquals(new JsonPrimitive(1), actual.get("foo"));
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(new JsonPrimitive(1));
    writer.value(new JsonPrimitive(2));
    writer.endArray();
    JsonElement actual = writer.peek();
    assertSame(JsonNull.INSTANCE, actual);
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(new JsonPrimitive(1)).endObject();
    JsonElement actual = writer.peek();
    assertSame(JsonNull.INSTANCE, actual);
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").name("bar").value(new JsonPrimitive(1)).endObject();
    JsonObject actual = (JsonObject) writer.peek();
    assertTrue(actual.entrySet().containsKey("foo"));
    assertEquals(new JsonObject(), actual.get("foo"));
    assertTrue(actual.entrySet().containsKey("bar"));
    assertEquals(new JsonPrimitive(1), actual.get("bar"));
  }

  @Test(expected = IllegalStateException.class)
  public void testName_empty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("foo");
  }

  @Test(expected = IllegalStateException.class)
  public void testValue_null() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(null);
  }

  @Test
  public void testValue_string() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("foo");
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertEquals("foo", actual.getAsString());
  }

  @Test
  public void testValue_boolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertTrue(actual.isBoolean());
    assertEquals(true, actual.getAsBoolean());
  }

  @Test
  public void testValue_number() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1.0);
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertEquals(1.0, actual.getAsDouble(), 0.0);
  }

  @Test
  public void testValue_number_int() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1);
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertEquals(1, actual.getAsInt());
  }

  @Test
  public void testValue_nullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertSame(JsonNull.INSTANCE, actual);
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().value(new JsonPrimitive(1)).value(new JsonPrimitive(2)).endArray();
    writer.close();
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertSame(JsonNull.INSTANCE, actual);
    assertFalse(writer.hasFlushed());
    assertFalse(writer.hasClosed());
  }

  @Test(expected = IllegalStateException.class)
  public void testClose_notClosed() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().value(new JsonPrimitive(1)).value(new JsonPrimitive(2)).endArray();
    writer.flush();
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertSame(JsonNull.INSTANCE, actual);
    assertTrue(writer.hasFlushed());
    assertFalse(writer.hasClosed());
  }
}