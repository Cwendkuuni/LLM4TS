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
    writer.beginObject().name("foo").value(new JsonPrimitive("bar"));
    writer.endObject();
    JsonElement expected = new JsonPrimitive("bar");
    JsonElement actual = writer.get();
    assertEquals(expected, actual);
  }

  @Test
  public void testPut() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement expected = new JsonPrimitive("foo");
    writer.put(expected);
    JsonElement actual = writer.peek();
    assertEquals(expected, actual);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonArray expected = writer.peek();
    assertEquals(expected.getClass(), JsonArray.class);
    writer.endArray();
    JsonElement actual = writer.peek();
    assertNull(actual);
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement actual = writer.peek();
    assertNull(actual);
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonObject expected = writer.peek();
    assertEquals(expected.getClass(), JsonObject.class);
    writer.endObject();
    JsonElement actual = writer.peek();
    assertNull(actual);
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement actual = writer.peek();
    assertNull(actual);
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo");
    JsonObject expected = writer.peek();
    assertEquals("foo", expected.name());
    writer.endObject();
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    String value = "foo";
    writer.value(value);
    JsonPrimitive expected = new JsonPrimitive(value);
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertEquals(expected.getAsString(), actual.getAsString());
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonNull expected = JsonNull.INSTANCE;
    JsonNull actual = (JsonNull) writer.peek();
    assertEquals(expected, actual);
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    boolean value = true;
    writer.value(value);
    JsonPrimitive expected = new JsonPrimitive(value);
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertEquals(expected.getAsBoolean(), actual.getAsBoolean());
  }

  @Test
  public void testValueNumberInt() {
    JsonTreeWriter writer = new JsonTreeWriter();
    int value = 42;
    writer.value(value);
    JsonPrimitive expected = new JsonPrimitive(value);
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertEquals(expected.getAsInt(), actual.getAsInt());
  }

  @Test
  public void testValueNumberLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    long value = 42L;
    writer.value(value);
    JsonPrimitive expected = new JsonPrimitive(value);
    JsonPrimitive actual = (JsonPrimitive) writer.peek();
    assertEquals(expected.getAsLong(), actual.getAsLong());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(null);
    JsonNull expected = JsonNull.INSTANCE;
    JsonNull actual = (JsonNull) writer.peek();
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.value("foo");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value("foo");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value("foo");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo");
    writer.value("bar");
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(new JsonPrimitive("bar"));
    writer.endObject();
    writer.flush();
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(new JsonPrimitive("bar"));
    writer.endObject();
    writer.close();
    JsonPrimitive sentinel = writer.get();
    assertEquals("closed", sentinel.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseBeforeEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(new JsonPrimitive("bar"));
    writer.close();
  }

  @Test
  public void testCloseMultipleTimes() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(new JsonPrimitive("bar"));
    writer.endObject();
    writer.close();
    writer.close();
    JsonPrimitive sentinel = writer.get();
    assertEquals("closed", sentinel.getAsString());
  }

  @Test
  public void testCloseAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(new JsonPrimitive("bar"));
    writer.endObject();
    writer.close();
    writer.close();
    writer.close();
    assertFalse(writer.hasNext());
  }
}