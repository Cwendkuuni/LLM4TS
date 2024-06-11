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
    JsonElement root = writer.get();
    assertTrue(root.isJsonNull());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement root = writer.get();
    assertTrue(root.isJsonArray());
    assertEquals(0, ((JsonArray) root).size());
  }

  @Test
  public void testBeginArrayAddValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    JsonElement root = writer.get();
    assertTrue(root.isJsonArray());
    assertEquals(1, ((JsonArray) root).size());
    assertEquals("foo", ((JsonPrimitive) ((JsonArray) root).get(0)).getAsString());
  }

  @Test
  public void testBeginArrayAddMultipleValues() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.value("bar");
    writer.value("baz");
    JsonElement root = writer.get();
    assertTrue(root.isJsonArray());
    assertEquals(3, ((JsonArray) root).size());
    assertEquals("foo", ((JsonPrimitive) ((JsonArray) root).get(0)).getAsString());
    assertEquals("bar", ((JsonPrimitive) ((JsonArray) root).get(1)).getAsString());
    assertEquals("baz", ((JsonPrimitive) ((JsonArray) root).get(2)).getAsString());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement root = writer.get();
    assertTrue(root.isJsonObject());
  }

  @Test
  public void testBeginObjectAddKeyValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key").value("value");
    JsonElement root = writer.get();
    assertTrue(root.isJsonObject());
    JsonObject jsonObject = (JsonObject) root;
    assertEquals("value", jsonObject.get("key").getAsString());
  }

  @Test
  public void testBeginObjectAddMultipleKeyValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").value("value2");
    writer.name("key3").value("value3");
    JsonElement root = writer.get();
    assertTrue(root.isJsonObject());
    JsonObject jsonObject = (JsonObject) root;
    assertEquals("value1", jsonObject.get("key1").getAsString());
    assertEquals("value2", jsonObject.get("key2").getAsString());
    assertEquals("value3", jsonObject.get("key3").getAsString());
  }

  @Test
  public void testNestedObjectsAndArrays() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("array");
    writer.beginArray();
    writer.value("foo");
    writer.value("bar");
    writer.endArray();
    writer.name("obj");
    writer.beginObject();
    writer.name("key").value("value");
    writer.endObject();
    writer.endObject();
    JsonElement root = writer.get();
    assertTrue(root.isJsonObject());
    JsonObject jsonObject = (JsonObject) root;
    JsonElement array = jsonObject.get("array");
    assertTrue(array.isJsonArray());
    assertEquals(2, ((JsonArray) array).size());
    assertEquals("foo", ((JsonPrimitive) ((JsonArray) array).get(0)).getAsString());
    assertEquals("bar", ((JsonPrimitive) ((JsonArray) array).get(1)).getAsString());
    JsonElement obj = jsonObject.get("obj");
    assertTrue(obj.isJsonObject());
    JsonObject nestedJsonObject = (JsonObject) obj;
    assertEquals("value", nestedJsonObject.get("key").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionBeginObjectAfterBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionBeginArrayAfterBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionNameAfterBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionNameAfterBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionValueAfterBeginObjectWithoutName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionValueAfterArrayWithName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("name");
    writer.value("value");
  }

  @Test
  public void testCloseWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement root = writer.get();
    writer.close();
    assertNull(writer.get());
    assertEquals(SENTINEL_CLOSED, writer.peek());
  }
}