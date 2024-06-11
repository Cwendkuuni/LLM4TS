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
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement result = writer.get();
    assertEquals(JsonArray.class, result.getClass());
    assertEquals(0, ((JsonArray) result).size());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.beginArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.beginObject();
  }

  @Test
  public void testName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value("bar");
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value("bar");
    writer.name("foo");
  }

  @Test
  public void testValueString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value("bar");
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test
  public void testValueBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value(true);
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.get("foo").getAsJsonPrimitive().isBoolean());
    assertTrue(jsonObject.get("foo").getAsBoolean());
  }

  @Test
  public void testValueDouble() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value(3.14);
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(3.14, jsonObject.get("foo").getAsJsonPrimitive().getAsDouble(), 0.0);
  }

  @Test
  public void testValueLong() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value(42);
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(42L, jsonObject.get("foo").getAsJsonPrimitive().getAsLong());
  }

  @Test
  public void testValueNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value(new JsonPrimitive(3.14));
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(3.14, jsonObject.get("foo").getAsJsonPrimitive().getAsDouble(), 0.0);
  }

  @Test
  public void testValueNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.nullValue();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertNull(jsonObject.get("foo"));
  }

  @Test
  public void testBeginArrayValueString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.beginArray();
    writer.value("bar");
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonArray jsonArray = jsonObject.get("foo").getAsJsonArray();
    assertEquals(1, jsonArray.size());
    assertEquals("bar", jsonArray.get(0).getAsString());
  }

  @Test
  public void testBeginArrayValueBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.beginArray();
    writer.value(true);
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonArray jsonArray = jsonObject.get("foo").getAsJsonArray();
    assertEquals(1, jsonArray.size());
    JsonPrimitive jsonPrimitive = jsonArray.get(0).getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isBoolean());
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testBeginArrayValueNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.beginArray();
    writer.value(3.14);
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonArray jsonArray = jsonObject.get("foo").getAsJsonArray();
    assertEquals(1, jsonArray.size());
    JsonPrimitive jsonPrimitive = jsonArray.get(0).getAsJsonPrimitive();
    assertEquals(3.14, jsonPrimitive.getAsDouble(), 0.0);
  }

  @Test
  public void testBeginArrayValueNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.beginArray();
    writer.nullValue();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonArray jsonArray = jsonObject.get("foo").getAsJsonArray();
    assertEquals(0, jsonArray.size());
  }

  @Test
  public void testEndArrayAfterValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.beginArray();
    writer.value("bar");
    writer.endArray();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    JsonArray jsonArray = jsonObject.get("foo").getAsJsonArray();
    assertEquals(1, jsonArray.size());
    assertEquals("bar", jsonArray.get(0).getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayAfterName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value("bar");
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.endObject();
  }

  @Test
  public void testEndObjectAfterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertNull(jsonObject.get(""));
    assertEquals(JsonArray.class, jsonObject.get("").getClass());
  }

  @Test
  public void testEndObjectAfterEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
    writer.endArray();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertNull(jsonObject.get(""));
    assertTrue(stack.isEmpty());
  }

  @Test
  public void testCloseAfterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
    writer.close();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertNull(jsonObject.get(""));
    assertTrue(stack.isEmpty());
  }

  @Test(expected = IOException.class)
  public void testCloseAfterEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
    writer.endArray();
    writer.close();
  }

  @Test(expected = IOException.class)
  public void testCloseAfterBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.close();
  }

  @Test(expected = IOException.class)
  public void testCloseAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.close();
  }

  @Test
  public void testCloseAfterName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.close();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("", jsonObject.entrySet().iterator().next().getKey());
    assertSame(JsonNull.INSTANCE, jsonObject.get(""));
    assertTrue(stack.isEmpty());
  }

  @Test
  public void testCloseAfterValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.value("bar");
    writer.close();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("", jsonObject.entrySet().iterator().next().getKey());
    assertSame(JsonNull.INSTANCE, jsonObject.get(""));
    assertTrue(stack.isEmpty());
  }
}