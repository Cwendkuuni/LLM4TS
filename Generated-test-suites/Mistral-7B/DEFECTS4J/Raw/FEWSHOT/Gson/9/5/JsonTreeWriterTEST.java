package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    assertNull(result);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement array = writer.get();
    assertNotNull(array);
    assertEquals(JsonArray.class, array.getClass());
    assertEquals(0, array.size());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement obj = writer.get();
    assertNotNull(obj);
    assertEquals(JsonObject.class, obj.getClass());
    assertTrue(obj.entrySet().isEmpty());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement array = writer.get();
    assertNotNull(array);
    assertEquals(JsonArray.class, array.getClass());
    assertEquals(0, array.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement obj = writer.get();
    assertNotNull(obj);
    assertEquals(JsonObject.class, obj.getClass());
    assertTrue(obj.entrySet().isEmpty());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    JsonElement obj = writer.get();
    assertNotNull(obj);
    assertEquals(JsonObject.class, obj.getClass());
    JsonElement key = obj.get("key");
    assertNotNull(key);
    assertEquals("value", key.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("key");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals("value", result.getAsString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonPrimitive.class, result.getClass());
    assertTrue(result.isJsonPrimitive());
    assertEquals(JsonPrimitive.booleanValue(result), true);
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(123);
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals(JsonPrimitive.getNumber(result), 123);
  }

  @Test
  public void testNestedArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.value("inner");
    writer.endArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonArray.class, result.getClass());
    JsonArray outerArray = result.getAsJsonArray();
    assertEquals(1, outerArray.size());
    JsonArray innerArray = outerArray.get(0).getAsJsonArray();
    assertEquals(1, innerArray.size());
    assertEquals("inner", innerArray.get(0).getAsString());
  }

  @Test
  public void testNestedObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.beginObject();
    writer.name("innerKey");
    writer.value("innerValue");
    writer.endObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonObject.class, result.getClass());
    JsonObject outerObj = result.getAsJsonObject();
    assertEquals(1, outerObj.size());
    JsonObject innerObj = outerObj.get("key").getAsJsonObject();
    assertEquals(1, innerObj.size());
    assertEquals("innerValue", innerObj.get("innerKey").getAsString());
  }
}