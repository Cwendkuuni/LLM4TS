package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement element = writer.get();
    assertEquals(JsonArray.class, element.getClass());
    assertEquals(0, ((JsonArray) element).size());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test
  public void testBeginArrayAddValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    JsonElement element = writer.get();
    JsonArray jsonArray = (JsonArray) element;
    assertEquals(1, jsonArray.size());
    assertEquals("foo", jsonArray.get(0).getAsString());
  }

  @Test
  public void testBeginArrayAddArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    writer.endArray();
    JsonElement element = writer.get();
    JsonArray jsonArray = (JsonArray) element;
    assertEquals(1, jsonArray.size());
    JsonArray innerArray = (JsonArray) jsonArray.get(0);
    assertEquals(1, innerArray.size());
    assertEquals("foo", innerArray.get(0).getAsString());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement element = writer.get();
    assertEquals(JsonObject.class, element.getClass());
    assertTrue(element.isJsonObject());
  }

  @Test
  public void testBeginObjectAddNameValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement element = writer.get();
    JsonObject jsonObject = (JsonObject) element;
    assertEquals("value", jsonObject.get("name").getAsString());
  }

  @Test
  public void testBeginObjectAddNameArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    writer.endObject();
    JsonElement element = writer.get();
    JsonObject jsonObject = (JsonObject) element;
    JsonArray jsonArray = (JsonArray) jsonObject.get("name");
    assertEquals(1, jsonArray.size());
    assertEquals("foo", jsonArray.get(0).getAsString());
  }

  @Test
  public void testBeginObjectAddNameObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.beginObject();
    writer.name("innerName");
    writer.value("innerValue");
    writer.endObject();
    writer.endObject();
    JsonElement element = writer.get();
    JsonObject jsonObject = (JsonObject) element;
    JsonObject innerObject = (JsonObject) jsonObject.get("name");
    assertEquals("innerValue", innerObject.get("innerName").getAsString());
  }

  @Test
  public void testNameTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.name("name");
    writer.endObject();
    JsonElement element = writer.get();
    JsonObject jsonObject = (JsonObject) element;
    assertTrue(jsonObject.hasKey("name"));
    assertEquals(1, jsonObject.get("name").getAsJsonArray().size());
  }

  @Test
  public void testValueTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value("value");
    writer.value("value");
    writer.endObject();
    JsonElement element = writer.get();
    JsonObject jsonObject = (JsonObject) element;
    assertTrue(jsonObject.hasKey("value"));
    assertEquals(2, jsonObject.get("value").getAsJsonArray().size());
  }

  @Test
  public void testCloseWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    JsonElement element = writer.get();
    assertEquals(JsonPrimitive.class, element.getClass());
    assertEquals("closed", element.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseWriterTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.value("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testNullValueAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.nullValue();
  }
}