package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
    assertNotNull(result);
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithoutEnding() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginning() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutEnding() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginning() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test
  public void testBeginArrayEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonArray.class, result.getClass());
    assertEquals(JsonArray.class, writer.peek().getClass());
  }

  @Test
  public void testBeginObjectNameValueEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    assertEquals(JsonObject.class, result.getClass());
    JsonObject object = (JsonObject) result;
    assertEquals("name", object.name("name").getAsString());
    assertEquals("value", object.get("name").getAsString());
  }

  @Test
  public void testBeginArrayAddValueBeginArrayAddValueEndArrayGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("value1");
    writer.beginArray();
    writer.value("value2");
    writer.endArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertNotNull(result);
    JsonArray array = (JsonArray) result;
    assertEquals(2, array.size());
    assertEquals("value1", array.get(0).getAsString());
    JsonArray subArray = (JsonArray) array.get(0);
    assertEquals(1, subArray.size());
    assertEquals("value2", subArray.get(0).getAsString());
  }

  @Test
  public void testBeginArrayAddValueBeginObjectNameValueEndObjectAddValueEndArrayGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("value1");
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    writer.value("value3");
    writer.endArray();
    JsonElement result = writer.get();
    assertNotNull(result);
    JsonArray array = (JsonArray) result;
    assertEquals(1, array.size());
    JsonElement element = array.get(0);
    assertSame(JsonObject.class, element.getClass());
    JsonObject obj = (JsonObject) element;
    assertEquals("name", obj.name("name").getAsString());
    assertEquals("value", obj.get("name").getAsString());
    assertEquals(2, obj.size());
    assertEquals("value1", array.get(0).getAsJson().get("value1").getAsString());
    assertEquals("value3", array.get(0).getAsJson().get("value3").getAsString());
  }

  @Test
  public void testBeginObjectNameValueBeginArrayAddValueEndArrayNameValueEndObjectGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("value1");
    writer.beginArray();
    writer.value("value2");
    writer.endArray();
    writer.name("anotherName");
    writer.value("value3");
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    JsonObject obj = (JsonObject) result;
    assertEquals("name", obj.name("name").getAsString());
    assertEquals("value1", obj.get("name").getAsString());
    JsonArray array = (JsonArray) obj.get("anotherName");
    assertEquals(1, array.size());
    assertEquals("value2", array.get(0).getAsString());
  }

  @Test
  public void testBeginObjectNameValueBeginObjectNameValueEndObjectGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1");
    writer.value("value1");
    writer.beginObject();
    writer.name("name2");
    writer.value("value2");
    writer.endObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertNotNull(result);
    JsonObject obj = (JsonObject) result;
    JsonObject nestedObj = (JsonObject) obj.get("name1");
    assertEquals("name2", nestedObj.name("name2").getAsString());
    assertEquals("value2", nestedObj.get("name2").getAsString());
  }
}