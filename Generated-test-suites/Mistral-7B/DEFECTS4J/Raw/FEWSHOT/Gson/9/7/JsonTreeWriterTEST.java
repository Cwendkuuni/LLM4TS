package com.google.gson.internal.bind;

import static com.google.gson.JsonElement.NULL;
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
  public void testGetEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertEquals(NULL, writer.get());
  }

  @Test
  public void testGetArrayWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    assertEquals(NULL, writer.get());

    writer = new JsonTreeWriter();
    JsonArray array = writer.beginArray().get();
    JsonElement element = new JsonPrimitive("element");
    writer.value(element).endArray();
    JsonArray result = (JsonArray) writer.get();
    assertEquals(1, result.size());
    assertSame(element, result.get(0));
  }

  @Test
  public void testGetObjectWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("value").endObject();
    JsonObject result = (JsonObject) writer.get();
    assertEquals("value", result.get("name").getAsString());
  }

  @Test
  public void testStackOverflow() {
    JsonTreeWriter writer = new JsonTreeWriter();
    for (int i = 0; i < 1000; i++) {
      writer.beginObject().name("name" + i).value("value" + i).endObject();
    }
    try {
      writer.get();
      fail("Expected IllegalStateException due to stack overflow");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testNestedObjects() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name1").beginObject().name("name2").value("value").endObject().endObject();
    JsonObject result = (JsonObject) writer.get();
    JsonObject nestedObject = result.getAsJsonObject("name1");
    assertEquals("value", nestedObject.get("name2").getAsString());
  }

  @Test
  public void testNestedArrays() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name1").beginArray().value("value1").value("value2").endArray().endObject();
    JsonObject result = (JsonObject) writer.get();
    JsonArray nestedArray = result.getAsJsonObject("name1").getAsJsonArray();
    assertEquals(2, nestedArray.size());
    assertEquals("value1", nestedArray.get(0).getAsString());
    assertEquals("value2", nestedArray.get(1).getAsString());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    JsonPrimitive result = (JsonPrimitive) writer.get();
    assertEquals("value", result.getAsString());
  }

  @Test
  public void testValueBooleanTrue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonPrimitive result = (JsonPrimitive) writer.get();
    assertTrue(result.isBoolean());
    assertTrue(result.getAsBoolean());
  }

  @Test
  public void testValueBooleanFalse() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(false);
    JsonPrimitive result = (JsonPrimitive) writer.get();
    assertFalse(result.isBoolean());
    assertFalse(result.getAsBoolean());
  }

  @Test
  public void testValueNumberInt() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42);
    JsonPrimitive result = (JsonPrimitive) writer.get();
    assertEquals(42, result.getAsInt());
  }

  @Test
  public void testValueNumberLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42L);
    JsonPrimitive result = (JsonPrimitive) writer.get();
    assertEquals(42L, result.getAsLong());
  }

  @Test(expected = IOException.class)
  public void testCloseBeforeEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").close();
  }

  @Test(expected = IOException.class)
  public void testCloseBeforeEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().close();
  }

  @Test(expected = IOException.class)
  public void testCloseBeforeValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    writer.close();
  }

  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("value").endObject();
    writer.close();
    JsonPrimitive result = (JsonPrimitive) writer.get();
    assertEquals("closed", result.getAsString());
  }
}