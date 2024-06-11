package com.google.gson.internal.bind;

import static com.google.gson.JsonElement.NULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    assertEquals(JsonNull.INSTANCE, writer.get());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement element = writer.get();
    assertEquals(JsonArray.class, element.getClass());
    assertEquals(0, ((JsonArray) element).size());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayBeforeBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
    writer.beginArray();  // should throw an exception
  }

  @Test
  public void testBeginArrayValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    JsonElement element = writer.get();
    assertEquals(JsonArray.class, element.getClass());
    assertEquals(1, ((JsonArray) element).size());
    assertEquals("foo", ((JsonPrimitive) ((JsonArray) element).get(0)).getAsString());
  }

  @Test
  public void testBeginArrayValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.nullValue();
    writer.endArray();
    JsonElement element = writer.get();
    assertEquals(JsonArray.class, element.getClass());
    assertEquals(1, ((JsonArray) element).size());
    assertSame(JsonNull.INSTANCE, ((JsonElement) ((JsonArray) element).get(0)));
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginObject();
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectBeforeBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
    writer.beginObject();  // should throw an exception
  }

  @Test
  public void testBeginObjectName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    assertEquals("value", jsonObject.get("key").getAsString());
  }

  @Test
  public void testBeginObjectNameNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.nullValue();
    writer.endObject();
    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    assertSame(JsonNull.INSTANCE, jsonObject.get("key"));
  }

  @Test
  public void testBeginObjectNameNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.nullValue();
    writer.name("anotherKey").value("value");
    writer.endObject();
    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    assertNull(jsonObject.get("key"));
    assertEquals("value", jsonObject.get("anotherKey").getAsString());
  }

  @Test
  public void testBeginObjectNameSameKeyTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value1");
    writer.name("key");
    writer.value("value2");
    writer.endObject();
    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    assertEquals(2, jsonObject.size());
    assertEquals("value1", jsonObject.get("key").getAsString());
    assertEquals("value2", jsonObject.get("key").getAsString());
  }

  @Test
  public void testBeginObjectArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
    writer.value("foo");
    writer.value("bar");
    writer.endArray();
    writer.endObject();
    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    JsonObject jsonObject = (JsonObject) element;
    JsonArray array = (JsonArray) jsonObject.get("");
    assertEquals(2, array.size());
    assertEquals("foo", ((JsonPrimitive) array.get(0)).getAsString());
    assertEquals("bar", ((JsonPrimitive) array.get(1)).getAsString());
  }
}