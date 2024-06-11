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
  public void testBasicUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo").value("bar");
    writer.endObject();

    JsonElement json = writer.get();
    assertNotNull(json);
    assertEquals(JsonElement.class, json.getClass());
    JsonObject obj = json.getAsJsonObject();
    assertEquals("bar", obj.get("foo").getAsString());
  }

  @Test
  public void testArrayUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.beginArray();
    writer.value("bar");
    writer.value("baz");
    writer.endArray();
    writer.endArray();

    JsonElement json = writer.get();
    assertNotNull(json);
    assertEquals(JsonElement.class, json.getClass());

    JsonArray arr = json.getAsJsonArray();
    assertEquals(2, arr.size());

    JsonElement arr1 = arr.get(0);
    assertEquals(JsonArray.class, arr1.getClass());
    JsonArray subArr = arr1.getAsJsonArray();
    assertEquals(2, subArr.size());

    JsonElement elem1 = subArr.get(0);
    assertEquals("foo", elem1.getAsString());

    JsonElement elem2 = subArr.get(1);
    assertEquals("bar", elem2.getAsString());

    JsonElement elem3 = arr.get(1);
    assertEquals("baz", elem3.getAsString());
  }

  @Test
  public void testNestedObjects() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("foo").beginObject();
    writer.name("bar").value("baz");
    writer.endObject();
    writer.name("qux").beginArray();
    writer.value("quux");
    writer.endArray();
    writer.endObject();

    JsonElement json = writer.get();
    assertNotNull(json);
    assertEquals(JsonElement.class, json.getClass());

    JsonObject obj = json.getAsJsonObject();
    assertEquals("baz", obj.get("foo").getAsJsonObject().get("bar").getAsString());
    JsonArray arr = obj.get("qux").getAsJsonArray();
    assertEquals(1, arr.size());
    assertEquals("quux", arr.get(0).getAsString());
  }

  @Test
  public void testInvalidUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      writer.beginObject().name(null);
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.beginObject().value(null);
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.beginArray().name("foo");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.endObject().name("foo");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.value(null).name("foo");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.beginArray().endArray().name("foo");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.endArray().name("foo");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testInvalidCallsAfterClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();

    try {
      writer.beginObject();
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.beginArray();
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.name("foo");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      writer.value("foo");
      throw new AssertionError();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testLenientMode() {
    JsonTreeWriter writer = new JsonTreeWriter(true);
    writer.beginArray();
    writer.value(Double.NaN);
    writer.endArray();

    JsonElement json = writer.get();
    assertNotNull(json);
    assertEquals(JsonElement.class, json.getClass());
    JsonArray arr = json.getAsJsonArray();
    assertEquals(1, arr.size());
    JsonElement elem = arr.get(0);
    assertTrue(elem.isJsonPrimitive());
    JsonPrimitive prim = elem.getAsJsonPrimitive();
    assertEquals(Double.NaN, prim.getAsDouble(), 0.0);
  }
}