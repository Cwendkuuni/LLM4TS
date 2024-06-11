package com.google.gson.internal.bind;

import static org.junit.Assert.*;

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
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement result = writer.get();
    assertEquals(JsonArray.class, result.getClass());
    assertEquals(0, ((JsonArray) result).size());

    writer.value("foo");
    result = writer.get();
    assertEquals("foo", ((JsonPrimitive) ((JsonArray) result).get(0)).getAsString());
    assertEquals(1, ((JsonArray) result).size());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    JsonElement result = writer.get();
    assertEquals(JsonArray.class, result.getClass());
    assertEquals(1, ((JsonArray) result).size());
    assertEquals("foo", ((JsonPrimitive) ((JsonArray) result).get(0)).getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayBeforeBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo").value("bar");
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo").value("bar");
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectBeforeBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo");
    writer.name("bar");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertNotNull(jsonObject.get("foo"));
    assertNotNull(jsonObject.get("bar"));
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.name("foo");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value("bar");
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(true);
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(true, jsonObject.get("foo").getAsBoolean());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").nullValue();
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertNull(jsonObject.get("foo"));
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(123);
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(123L, jsonObject.get("foo").getAsNumber().getLong());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("foo").value(3.14);
    JsonElement result = writer.get();
    JsonObject jsonObject = (JsonObject) result;
    assertEquals(3.14, jsonObject.get("foo").getAsDouble(), 0.001);
  }
}