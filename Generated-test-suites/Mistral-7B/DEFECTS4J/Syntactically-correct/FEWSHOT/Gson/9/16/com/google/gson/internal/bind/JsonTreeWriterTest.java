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
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.beginObject();
    writer.name("foo").value("bar");
    writer.endObject();
    assertNotNull(writer.get());
    assertSame(JsonElement.class, writer.get().getClass());
    assertEquals(JsonObject.class, writer.get().getClass());
    JsonObject jsonObject = (JsonObject) writer.get();
    assertEquals("foo", jsonObject.getName(0));
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonArray array = writer.beginArray();
    writer.value("element1");
    writer.value("element2");
    writer.endArray();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonArray.class, jsonElement.getClass());
    assertEquals(2, ((JsonArray) jsonElement).size());
    assertEquals("element1", ((JsonPrimitive) ((JsonArray) jsonElement).get(0)).getAsString());
    assertEquals("element2", ((JsonPrimitive) ((JsonArray) jsonElement).get(1)).getAsString());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonArray array = writer.beginArray();
    writer.endArray();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonArray.class, jsonElement.getClass());
    assertEquals(0, ((JsonArray) jsonElement).size());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonArray array = writer.beginArray();
    writer.name("name");
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name");
    writer.endArray();
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo").value("bar");
    writer.endObject();
    assertNotNull(writer.get());
    assertSame(JsonElement.class, writer.get().getClass());
    assertEquals(JsonObject.class, writer.get().getClass());
    JsonObject jsonObject = (JsonObject) writer.get();
    assertEquals("foo", jsonObject.getName(0));
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("foo");
    writer.beginObject();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo");
    writer.value("bar");
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("foo", jsonObject.getName(0));
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("foo");
    writer.value("bar");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo").value("bar");
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("foo", jsonObject.getName(0));
    assertEquals("bar", jsonObject.get("foo").getAsString());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo").nullValue();
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("foo", jsonObject.getName(0));
    assertNull(jsonObject.get("foo"));
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo").value(true);
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("foo", jsonObject.getName(0));
    assertTrue(jsonObject.get("foo").isJsonPrimitive());
    assertTrue(((JsonPrimitive) jsonObject.get("foo")).isBoolean());
    assertTrue(((JsonPrimitive) jsonObject.get("foo")).getAsBoolean());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo").value(1.23);
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("foo", jsonObject.getName(0));
    assertTrue(jsonObject.get("foo").isJsonPrimitive());
    assertTrue(((JsonPrimitive) jsonObject.get("foo")).isNumber());
    assertEquals(1.23, ((JsonPrimitive) jsonObject.get("foo")).getAsDouble(), 0.0);
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo").value(123);
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("foo", jsonObject.getName(0));
    assertTrue(jsonObject.get("foo").isJsonPrimitive());
    assertTrue(((JsonPrimitive) jsonObject.get("foo")).isNumber());
    assertEquals(123, ((JsonPrimitive) jsonObject.get("foo")).getAsLong());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo").value(new Long(123));
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("foo", jsonObject.getName(0));
    assertTrue(jsonObject.get("foo").isJsonPrimitive());
    assertTrue(((JsonPrimitive) jsonObject.get("foo")).isNumber());
    assertEquals(123, ((JsonPrimitive) jsonObject.get("foo")).getAsLong());
  }

  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = writer.beginObject();
    writer.name("foo").value("bar");
    writer.endObject();
    writer.close();
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertSame(JsonElement.class, jsonElement.getClass());
    JsonElement sentinelClosed = writer.get();
    assertNotNull(sentinelClosed);
    assertSame(JsonPrimitive.class, sentinelClosed.getClass());
    assertEquals("closed", sentinelClosed.getAsString());
  }

  @Test(expected = IOException.class)
  public void testCloseTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
  }
}